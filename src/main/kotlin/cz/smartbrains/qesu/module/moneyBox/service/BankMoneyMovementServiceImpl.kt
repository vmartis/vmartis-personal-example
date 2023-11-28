package cz.smartbrains.qesu.module.moneyBox.service

import cz.smartbrains.qesu.module.common.exception.RecordNotFoundException
import cz.smartbrains.qesu.module.common.exception.ServiceRuntimeException
import cz.smartbrains.qesu.module.invoice.repository.InvoiceRepository
import cz.smartbrains.qesu.module.user.repository.UserRepository
import cz.smartbrains.qesu.module.moneyBox.dto.BankMoneyMovementDto
import cz.smartbrains.qesu.module.moneyBox.dto.BankMoneyMovementSplitDto
import cz.smartbrains.qesu.module.moneyBox.dto.MoneyMovementDto
import cz.smartbrains.qesu.module.moneyBox.entity.BankMoneyMovement
import cz.smartbrains.qesu.module.moneyBox.mapper.BankMoneyMovementMapper
import cz.smartbrains.qesu.module.moneyBox.repository.BankMoneyMovementRepository
import cz.smartbrains.qesu.module.moneyBox.type.AccountingType
import cz.smartbrains.qesu.security.AlfaUserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.function.Consumer
import java.util.stream.Collectors

@Service
class BankMoneyMovementServiceImpl(private val bankMoneyMovementRepository: BankMoneyMovementRepository,
                                   private val invoiceRepository: InvoiceRepository,
                                   private val userRepository: UserRepository,
                                   private val bankMoneyMovementMapper: BankMoneyMovementMapper,
                                   private val invoicePairService: MoneyMovementInvoicePairService,
                                   private val subjectPairService: MoneyMovementSubjectPairService) : BankMoneyMovementService {

    @Transactional
    override fun update(moneyMovement: BankMoneyMovementDto, user: AlfaUserDetails): BankMoneyMovementDto {
        val originalMovement = bankMoneyMovementRepository.findById(moneyMovement.id!!).orElseThrow { RecordNotFoundException() }
        subjectPairService.pairSubject(moneyMovement.id!!, if (moneyMovement.subject == null) null else moneyMovement.subject!!.id)
        invoicePairService.payInvoice(moneyMovement, originalMovement)
        val newMovement = bankMoneyMovementMapper.dtoToDo(moneyMovement)
        originalMovement.invoice = newMovement!!.invoice
        originalMovement.category = newMovement.category
        originalMovement.totalAmount = newMovement.totalAmount
        originalMovement.accountingType = newMovement.accountingType
        originalMovement.totalVat = newMovement.totalVat
        originalMovement.totalWithoutVat = newMovement.totalWithoutVat
        originalMovement.note = newMovement.note
        originalMovement.taxableDate = newMovement.taxableDate
        originalMovement.active = newMovement.active
        originalMovement.updatedBy = userRepository.getById(user.id)
        return bankMoneyMovementMapper.doToDto(originalMovement)!!
    }

    @Transactional
    override fun split(moneyMovementSplit: BankMoneyMovementSplitDto, user: AlfaUserDetails): BankMoneyMovementDto {
        val origMov = bankMoneyMovementRepository.findById(moneyMovementSplit.originalMovement!!.id!!).orElseThrow { RecordNotFoundException() }

        //check that orig amount is same as sum of all new movements
        if (moneyMovementSplit.newMovements!!.size < MIN_SPLIT_SIZE || moneyMovementSplit.newMovements!!.size > MAX_SPLIT_SIZE) {
            throw ServiceRuntimeException("bank-money-movement.split.newMovements.size.incorrect")
        }
        val userEntity = userRepository.getById(user.id)

        //check that orig amount is same as sum of all new movements
        if (origMov.totalAmount!!.abs().compareTo(
                        moneyMovementSplit.newMovements!!
                                .stream()
                                .map(MoneyMovementDto::totalAmount)
                                .reduce { obj: BigDecimal?, augend: BigDecimal? -> obj!!.add(augend) }
                                .orElseThrow { IllegalStateException() }) != 0) {
            throw ServiceRuntimeException("bank-money-movement.split.amount.incorrectSum")
        }
        moneyMovementSplit.newMovements!!.forEach(Consumer { newMovement: BankMoneyMovementDto -> invoicePairService.payInvoice(newMovement, origMov) })
        val newMovements: List<BankMoneyMovement> = moneyMovementSplit
                .newMovements!!
                .stream()
                .map { moneyMovementDto: BankMoneyMovementDto -> bankMoneyMovementMapper.dtoToDo(moneyMovementDto)!! }
                .collect(Collectors.toList())
        newMovements.forEach(Consumer { newMovement: BankMoneyMovement ->
            newMovement.date = origMov.date
            newMovement.taxableDate = origMov.taxableDate
            newMovement.moneyBox = origMov.moneyBox
            newMovement.accountingType = origMov.accountingType
            newMovement.subject = origMov.subject
            newMovement.category = origMov.category
            newMovement.note = moneyMovementSplit.note
            newMovement.bankTransaction = origMov.bankTransaction
            newMovement.correspondingBankAccount = origMov.correspondingBankAccount
            newMovement.subject = origMov.subject
            newMovement.active = true
            newMovement.createdBy = userEntity

            //if invoice was selected, set taxable date + vatAmount (if amount of movement is same as amount of invoice)
            if (newMovement.invoice != null) {
                val invoice = invoiceRepository.findById(newMovement.invoice!!.id!!).orElseThrow { RecordNotFoundException() }
                newMovement.taxableDate = invoice.taxableDate
                newMovement.accountingType = AccountingType.FISCAL
                if (newMovement.totalAmount!!.compareTo(invoice.totalAmount) == 0) {
                    newMovement.totalVat = invoice.totalVat
                    newMovement.totalWithoutVat = newMovement.totalAmount!!.subtract(newMovement.totalVat)
                }
            }
        })
        bankMoneyMovementRepository.saveAll(newMovements)
        origMov.active = false
        origMov.updatedBy = userEntity
        return bankMoneyMovementMapper.doToDto(origMov)!!
    }

    companion object {
        private const val MIN_SPLIT_SIZE = 2
        private const val MAX_SPLIT_SIZE = 50
    }
}