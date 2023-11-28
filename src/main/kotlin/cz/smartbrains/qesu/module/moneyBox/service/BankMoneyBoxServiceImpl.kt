package cz.smartbrains.qesu.module.moneyBox.service

import cz.smartbrains.qesu.module.common.exception.ServiceRuntimeException
import cz.smartbrains.qesu.module.common.service.OrderableEntityService
import cz.smartbrains.qesu.module.company.dto.CompanyDto
import cz.smartbrains.qesu.module.company.repository.CompanyBranchRepository
import cz.smartbrains.qesu.module.bank.repository.BankAccountRepository
import cz.smartbrains.qesu.module.bank.service.BankAccountService
import cz.smartbrains.qesu.module.common.exception.RecordNotFoundException
import cz.smartbrains.qesu.module.moneyBox.dto.BankMoneyBoxDto
import cz.smartbrains.qesu.module.moneyBox.mapper.BankMoneyBoxMapper
import cz.smartbrains.qesu.module.moneyBox.repository.BankMoneyBoxRepository
import cz.smartbrains.qesu.module.moneyBox.repository.MoneyBoxRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Transactional
@Service
class BankMoneyBoxServiceImpl(private val mapper: BankMoneyBoxMapper,
                              private val bankMoneyBoxRepository: BankMoneyBoxRepository,
                              private val moneyBoxRepository: MoneyBoxRepository,
                              private val companyBranchRepository: CompanyBranchRepository,
                              private val bankAccountService: BankAccountService,
                              private val bankAccountRepository: BankAccountRepository,
                              private val orderableEntityService: OrderableEntityService) : BankMoneyBoxService {
    override fun create(moneyBoxDto: BankMoneyBoxDto): BankMoneyBoxDto {
        checkCurrency(moneyBoxDto)
        val moneyBox = mapper.dtoToDo(moneyBoxDto)
        val companyBranch = companyBranchRepository.findById(moneyBox!!.companyBranch!!.id!!).orElseThrow { RecordNotFoundException() }
        // need to set subject of bank account before save
        val company = CompanyDto()
        company.id = companyBranch.company!!.id
        moneyBoxDto.bankAccount!!.subject = company
        moneyBoxDto.bankAccount!!.name = moneyBoxDto.name
        val bankAccountDto = bankAccountService.create(moneyBoxDto.bankAccount!!)
        moneyBox.order = orderableEntityService.newItemOrder(moneyBoxRepository)
        moneyBox.bankAccount = bankAccountRepository.getById(bankAccountDto.id!!)
        moneyBox.amount = BigDecimal.ZERO
        moneyBox.active = true
        val persistedMoneyBox = bankMoneyBoxRepository.saveAndFlush(moneyBox)

        // update account because initial amount could be set
        updateAccount(persistedMoneyBox.id!!)

        return mapper.doToDto(persistedMoneyBox)!!
    }

    override fun update(moneyBoxDto: BankMoneyBoxDto): BankMoneyBoxDto {
        checkCurrency(moneyBoxDto)
        val originalMoneyBox = bankMoneyBoxRepository.findById(moneyBoxDto.id!!).orElseThrow { RecordNotFoundException() }
        val newMoneyBox = mapper.dtoToDo(moneyBoxDto)
        bankAccountService.update(moneyBoxDto.bankAccount!!)
        originalMoneyBox.companyBranch = newMoneyBox!!.companyBranch
        originalMoneyBox.name = newMoneyBox.name
        originalMoneyBox.currency = newMoneyBox.currency
        originalMoneyBox.bankAccount = newMoneyBox.bankAccount
        originalMoneyBox.initialAmount = newMoneyBox.initialAmount
        originalMoneyBox.initialDate = newMoneyBox.initialDate
        originalMoneyBox.active = newMoneyBox.active
        val persistedMoneyBox = moneyBoxRepository.save(originalMoneyBox)

        // update account because initial amount could have changed
        updateAccount(moneyBoxDto.id!!)
        return mapper.doToDto(persistedMoneyBox)!!
    }

    override fun updateAccount(moneyBoxId: Long) {
        val balance = bankMoneyBoxRepository.computeBalance(moneyBoxId)
        val moneyBox = bankMoneyBoxRepository.getById(moneyBoxId)
        moneyBox.amount = balance.totalAmount.add(moneyBox.initialAmount)
    }

    // ensure same currency on money box and related bank account
    private fun checkCurrency(moneyBoxDto: BankMoneyBoxDto) {
        if (moneyBoxDto.currency != moneyBoxDto.bankAccount!!.currency) {
            throw ServiceRuntimeException("bankMoneyBox.currency.different")
        }
    }
}