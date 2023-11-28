package cz.smartbrains.qesu.module.moneyBox.service

import cz.smartbrains.qesu.module.invoice.repository.InvoiceRepository
import cz.smartbrains.qesu.module.invoice.service.InvoiceService
import cz.smartbrains.qesu.module.invoice.type.InvoiceType
import cz.smartbrains.qesu.module.bank.entity.BankAccount
import cz.smartbrains.qesu.module.common.exception.RecordNotFoundException
import cz.smartbrains.qesu.module.common.exception.ServiceRuntimeException
import cz.smartbrains.qesu.module.moneyBox.dto.BankMoneyMovementDto
import cz.smartbrains.qesu.module.moneyBox.dto.CashMoneyMovementDto
import cz.smartbrains.qesu.module.moneyBox.entity.BankMoneyMovement
import cz.smartbrains.qesu.module.moneyBox.entity.CashMoneyMovement
import cz.smartbrains.qesu.module.moneyBox.repository.MoneyBoxRepository
import cz.smartbrains.qesu.module.moneyBox.type.MoneyMovementType
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class MoneyMovementInvoicePairServiceImpl(private val invoiceService: InvoiceService,
                                          private val invoiceRepository: InvoiceRepository,
                                          private val moneyBoxRepository: MoneyBoxRepository) : MoneyMovementInvoicePairService {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun payInvoice(newMoneyMovement: CashMoneyMovementDto) {
        if (newMoneyMovement.invoice == null) {
            log.debug("Nothing to pay, invoice was not selected.")
            return
        }
        val moneyBox = moneyBoxRepository.findById(newMoneyMovement.moneyBox!!.id!!).orElseThrow { RecordNotFoundException() }
        checkMovementType(newMoneyMovement.invoice!!.id, newMoneyMovement.type)
        checkCurrency(newMoneyMovement.invoice!!.id, moneyBox.currency)
        invoiceService.pay(newMoneyMovement.invoice!!.id!!, newMoneyMovement.totalAmount!!.abs())
    }

    override fun payInvoice(newMoneyMovement: CashMoneyMovementDto?, originalMovement: CashMoneyMovement) {
        if ((newMoneyMovement == null || newMoneyMovement.invoice == null)
                && originalMovement.invoice != null) {
            invoiceService.cancelPayment(originalMovement.invoice!!.id!!, originalMovement.totalAmount!!.abs())
        } else if (newMoneyMovement != null && newMoneyMovement.invoice != null && originalMovement.invoice == null) {
            checkMovementType(newMoneyMovement.invoice!!.id, newMoneyMovement.type)
            checkCurrency(newMoneyMovement.invoice!!.id, originalMovement.moneyBox!!.currency)
            invoiceService.pay(newMoneyMovement.invoice!!.id!!, newMoneyMovement.totalAmount!!.abs())
        } else if (newMoneyMovement != null && newMoneyMovement.invoice != null
                && originalMovement.invoice != null && originalMovement.invoice!!.id != newMoneyMovement.invoice!!.id) {
            checkMovementType(newMoneyMovement.invoice!!.id, newMoneyMovement.type)
            checkCurrency(newMoneyMovement.invoice!!.id, originalMovement.moneyBox!!.currency)
            invoiceService.cancelPayment(originalMovement.invoice!!.id!!, originalMovement.totalAmount!!.abs())
            invoiceService.pay(newMoneyMovement.invoice!!.id!!, newMoneyMovement.totalAmount!!.abs())
        }
    }

    override fun payInvoice(newMoneyMovement: BankMoneyMovementDto, originalMovement: BankMoneyMovement) {
        if (newMoneyMovement.invoice != null && originalMovement.invoice == null) {
            checkMovementType(newMoneyMovement.invoice!!.id, newMoneyMovement.type)
            checkCurrency(newMoneyMovement.invoice!!.id, originalMovement.moneyBox!!.currency)
            checkSubject(newMoneyMovement.invoice!!.id, originalMovement.correspondingBankAccount)
            invoiceService.pay(newMoneyMovement.invoice!!.id!!, newMoneyMovement.totalAmount!!.abs())
        } else if (newMoneyMovement.invoice == null
                && originalMovement.invoice != null) {
            invoiceService.cancelPayment(originalMovement.invoice!!.id!!, originalMovement.totalAmount!!.abs())
        } else if (newMoneyMovement.invoice != null && originalMovement.invoice != null && originalMovement.invoice!!.id != newMoneyMovement.invoice!!.id) {
            checkMovementType(newMoneyMovement.invoice!!.id, newMoneyMovement.type)
            checkSubject(newMoneyMovement.invoice!!.id, originalMovement.correspondingBankAccount)
            checkCurrency(newMoneyMovement.invoice!!.id, originalMovement.moneyBox!!.currency)
            invoiceService.cancelPayment(originalMovement.invoice!!.id!!, originalMovement.totalAmount!!.abs())
            invoiceService.pay(newMoneyMovement.invoice!!.id!!, newMoneyMovement.totalAmount!!.abs())
        }
    }

    fun checkCurrency(invoiceId: Long?, currency: String?) {
        val invoice = invoiceRepository.findById(invoiceId!!).orElseThrow { RecordNotFoundException() }
        if (invoice.currency != currency) {
            throw ServiceRuntimeException("moneyMovement.invoice.currency.not.match")
        }
    }

    fun checkMovementType(invoiceId: Long?, movementType: MoneyMovementType?) {
        val invoice = invoiceRepository.findById(invoiceId!!).orElseThrow { RecordNotFoundException() }
        if (movementType === MoneyMovementType.INCOME && invoice.type != InvoiceType.OUTCOME) {
            throw ServiceRuntimeException("money-movement.type.not.allowed")
        } else if (movementType === MoneyMovementType.OUTCOME && invoice.type != InvoiceType.INCOME) {
            throw ServiceRuntimeException("money-movement.type.not.allowed")
        }
    }

    fun checkSubject(invoiceId: Long?, correspondingBankAccount: BankAccount?) {
        if (correspondingBankAccount != null) {
            val invoice = invoiceRepository.findById(invoiceId!!).orElseThrow { RecordNotFoundException() }
            if (invoice.type == InvoiceType.OUTCOME && invoice.subscriber!!.id != correspondingBankAccount.subject!!.id) {
                throw ServiceRuntimeException("moneyMovement.invoice.subscriber.not.match")
            } else if (invoice.type == InvoiceType.INCOME && invoice.supplier!!.id != correspondingBankAccount.subject!!.id) {
                throw ServiceRuntimeException("moneyMovement.invoice.supplier.not.match")
            }
        }
    }
}