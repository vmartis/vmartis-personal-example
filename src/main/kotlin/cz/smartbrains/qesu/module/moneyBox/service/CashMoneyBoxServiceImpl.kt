package cz.smartbrains.qesu.module.moneyBox.service

import cz.smartbrains.qesu.module.common.exception.RecordNotFoundException
import cz.smartbrains.qesu.module.common.service.OrderableEntityService
import cz.smartbrains.qesu.module.moneyBox.dto.CashMoneyBoxDto
import cz.smartbrains.qesu.module.moneyBox.mapper.CashMoneyBoxMapper
import cz.smartbrains.qesu.module.moneyBox.repository.CashMoneyBoxRepository
import cz.smartbrains.qesu.module.moneyBox.repository.MoneyBoxRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Transactional
@Service
class CashMoneyBoxServiceImpl(private val mapper: CashMoneyBoxMapper,
                              private val cashMoneyBoxRepository: CashMoneyBoxRepository,
                              private val moneyBoxRepository: MoneyBoxRepository,
                              private val orderableEntityService: OrderableEntityService) : CashMoneyBoxService {
    override fun create(moneyBoxDto: CashMoneyBoxDto): CashMoneyBoxDto {
        val moneyBox = mapper.dtoToDo(moneyBoxDto)!!
        moneyBox.order = orderableEntityService.newItemOrder(moneyBoxRepository)
        moneyBox.amount = BigDecimal.ZERO
        moneyBox.amountCash = BigDecimal.ZERO
        moneyBox.amountCheque = BigDecimal.ZERO
        moneyBox.active = true
        val persistedMoneyBox = cashMoneyBoxRepository.save(moneyBox)
        return mapper.doToDto(persistedMoneyBox)!!
    }

    override fun update(moneyBoxDto: CashMoneyBoxDto): CashMoneyBoxDto {
        val originalMoneyBox = cashMoneyBoxRepository.findById(moneyBoxDto.id!!).orElseThrow { RecordNotFoundException() }
        val newMoneyBox = mapper.dtoToDo(moneyBoxDto)!!
        originalMoneyBox.companyBranch = newMoneyBox.companyBranch
        originalMoneyBox.name = newMoneyBox.name
        originalMoneyBox.currency = newMoneyBox.currency
        originalMoneyBox.active = newMoneyBox.active
        val persistedMoneyBox = cashMoneyBoxRepository.save(originalMoneyBox)
        return mapper.doToDto(persistedMoneyBox)!!
    }

    override fun updateAccount(moneyBoxId: Long) {
        val balance = cashMoneyBoxRepository.computeBalance(moneyBoxId)
        val moneyBox = cashMoneyBoxRepository.getById(moneyBoxId)
        moneyBox.amount = balance.totalAmount
        moneyBox.amountCash = balance.amountCash
        moneyBox.amountCheque = balance.amountCheque
    }
}