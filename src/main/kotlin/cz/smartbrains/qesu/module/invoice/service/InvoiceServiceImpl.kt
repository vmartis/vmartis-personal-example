package cz.smartbrains.qesu.module.invoice.service

import com.google.common.base.Preconditions
import cz.smartbrains.qesu.module.common.exception.RecordNotFoundException
import cz.smartbrains.qesu.module.common.exception.ServiceRuntimeException
import cz.smartbrains.qesu.module.invoice.dto.InvoiceDto
import cz.smartbrains.qesu.module.invoice.dto.InvoiceFilter
import cz.smartbrains.qesu.module.invoice.entity.Invoice
import cz.smartbrains.qesu.module.invoice.entity.InvoiceItem
import cz.smartbrains.qesu.module.invoice.mapper.InvoiceMapper
import cz.smartbrains.qesu.module.invoice.repository.InvoiceRepository
import cz.smartbrains.qesu.module.invoice.type.InvoiceType
import cz.smartbrains.qesu.module.order.repository.OrderStatementRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime
import java.util.function.Consumer
import java.util.stream.Collectors

@Service
class InvoiceServiceImpl(private val invoiceRepository: InvoiceRepository,
                         private val orderStatementRepository: OrderStatementRepository,
                         private val invoiceSeriesService: InvoiceSeriesService,
                         private val invoiceMapper: InvoiceMapper) : InvoiceService {
    @Transactional(readOnly = true)
    override fun find(id: Long): InvoiceDto {
        return invoiceMapper.doToDto(invoiceRepository.findById(id).orElseThrow { RecordNotFoundException() })!!
    }

    @Transactional(readOnly = true)
    override fun findAll(filter: InvoiceFilter): List<InvoiceDto> {
        return invoiceRepository.findByFilter(filter)
                .stream()
                .map { invoice: Invoice -> invoiceMapper.doToDto(invoice)!! }
                .collect(Collectors.toList())
    }

    @Transactional
    override fun create(invoiceDto: InvoiceDto): InvoiceDto {
        checkCreateInvoice(invoiceDto)
        val invoice = invoiceMapper.dtoToDo(invoiceDto)
        val now = LocalDateTime.now()
        if (invoice!!.type == InvoiceType.OUTCOME) {
            invoice.items.forEach(Consumer { invoiceItem: InvoiceItem ->
                invoiceItem.created = now
                invoiceItem.invoice = invoice
            })
            computeAmounts(invoice)
            val invoiceSeriesNumber = invoiceSeriesService.nextNumber(invoiceDto.invoiceSeries!!.id!!)
            invoice.number = invoiceSeriesNumber.invoiceNumber
            invoice.variableSymbol = invoiceSeriesNumber.variableSymbol
        } else if (invoice.type == InvoiceType.INCOME) {
            correctIncomeAmounts(invoice)
        }
        invoice.created = now
        val persistedInvoice = invoiceRepository.save(invoice)
        if (invoiceDto.orderStatement != null && invoiceDto.orderStatement!!.id != null) {
            val orderStatement = orderStatementRepository.getById(invoiceDto.orderStatement!!.id!!)
            orderStatement.invoices.add(persistedInvoice)
        }
        return invoiceMapper.doToDto(persistedInvoice)!!
    }

    @Transactional
    override fun update(invoiceDto: InvoiceDto): InvoiceDto {
        val originalInvoice = invoiceRepository.findById(invoiceDto.id!!).orElseThrow { RecordNotFoundException() }
        checkUpdateInvoice(invoiceDto, originalInvoice)
        val newInvoice = invoiceMapper.dtoToDo(invoiceDto)
        val now = LocalDateTime.now()
        if (originalInvoice.type == InvoiceType.OUTCOME) {
            newInvoice!!.items.forEach(Consumer { invoiceItem: InvoiceItem ->
                if (invoiceItem.id == null) {
                    invoiceItem.created = now
                }
                invoiceItem.invoice = originalInvoice
            })
            originalInvoice.items.clear()
            originalInvoice.items.addAll(newInvoice.items)
            computeAmounts(originalInvoice)
        } else if (originalInvoice.type == InvoiceType.INCOME) {
            originalInvoice.totalAmount = newInvoice!!.totalAmount
            originalInvoice.totalVat = newInvoice.totalVat
            correctIncomeAmounts(originalInvoice)
        }
        originalInvoice.variableSymbol = newInvoice!!.variableSymbol
        originalInvoice.specificSymbol = newInvoice.specificSymbol
        originalInvoice.constantSymbol = newInvoice.constantSymbol
        originalInvoice.supplier = newInvoice.supplier
        originalInvoice.subscriber = newInvoice.subscriber
        originalInvoice.currency = newInvoice.currency
        originalInvoice.paymentMethod = newInvoice.paymentMethod
        originalInvoice.dateOfIssue = newInvoice.dateOfIssue
        originalInvoice.dueDate = newInvoice.dueDate
        originalInvoice.taxableDate = newInvoice.taxableDate
        originalInvoice.reference = newInvoice.reference
        originalInvoice.bankAccount = newInvoice.bankAccount
        originalInvoice.transferredVat = newInvoice.transferredVat
        val persistedInvoice = invoiceRepository.save(originalInvoice)
        return invoiceMapper.doToDto(persistedInvoice)!!
    }

    @Transactional
    override fun delete(id: Long) {
        val invoice = invoiceRepository.findById(id).orElseThrow { RecordNotFoundException() }
        checkPaidAmount(invoice)
        invoiceRepository.deleteById(id)
    }

    @Transactional
    override fun pay(invoiceId: Long, amount: BigDecimal) {
        Preconditions.checkArgument(amount.compareTo(BigDecimal.ZERO) > 0, "invoice.pay.amount.negative")
        val invoice = invoiceRepository.findById(invoiceId).orElseThrow { RecordNotFoundException() }
        val remainingUnpaidAmount = invoice.unpaidAmount!!.subtract(amount)
        if (remainingUnpaidAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw ServiceRuntimeException("invoice.pay.amount.low")
        }
        invoice.unpaidAmount = remainingUnpaidAmount
    }

    @Transactional
    override fun cancelPayment(invoiceId: Long, amount: BigDecimal) {
        Preconditions.checkArgument(amount.compareTo(BigDecimal.ZERO) > 0, "invoice.pay.cancel.amount.negative")
        val invoice = invoiceRepository.findById(invoiceId).orElseThrow { RecordNotFoundException() }
        val remainingUnpaidAmount = invoice.unpaidAmount!!.add(amount)
        if (remainingUnpaidAmount.compareTo(invoice.totalAmount) > 0) {
            throw ServiceRuntimeException("invoice.pay.cancel.amount.cancelPayment.overflow")
        }
        invoice.unpaidAmount = remainingUnpaidAmount
    }

    fun checkCreateInvoice(invoiceDto: InvoiceDto) {
        if (invoiceDto.type == InvoiceType.OUTCOME) {
            checkOutcomeInvoice(invoiceDto)
            checkInvoiceSeries(invoiceDto)
        } else {
            checkIncomeInvoice(invoiceDto)
        }
        checkDueDate(invoiceDto)
    }

    fun checkUpdateInvoice(newInvoice: InvoiceDto, originalInvoice: Invoice) {
        checkPaidAmount(originalInvoice)
        checkDueDate(newInvoice)
        if (newInvoice.type == InvoiceType.OUTCOME) {
            checkOutcomeInvoice(newInvoice)
        } else {
            checkIncomeInvoice(newInvoice)
        }
    }

    //do not allow change of paid invoice
    private fun checkPaidAmount(originalInvoice: Invoice) {
        if (originalInvoice.unpaidAmount!!.compareTo(originalInvoice.totalAmount) != 0) {
            throw ServiceRuntimeException("invoice.update.invalid.unpaidAmount")
        }
    }

    fun checkOutcomeInvoice(invoiceDto: InvoiceDto) {
        if (invoiceDto.items!!.isEmpty()) {
            throw ServiceRuntimeException("invoice.create.outcome.items.empty")
        }
    }

    fun checkIncomeInvoice(invoiceDto: InvoiceDto) {
        if (!StringUtils.hasText(invoiceDto.number)) {
            throw ServiceRuntimeException("invoice.create.income.number.required")
        } else if (!invoiceDto.items!!.isEmpty()) {
            throw ServiceRuntimeException("invoice.create.income.items.notEmpty")
        } else if (invoiceDto.totalAmount == null) {
            throw ServiceRuntimeException("invoice.create.income.totalAmount.required")
        } else if (invoiceDto.totalVat == null) {
            throw ServiceRuntimeException("invoice.create.income.totalVat.required")
        }
    }

    private fun checkInvoiceSeries(invoiceDto: InvoiceDto) {
        if (invoiceDto.invoiceSeries == null || invoiceDto.invoiceSeries!!.id == null) {
            throw ServiceRuntimeException("invoice.create.outcome.series.required")
        }
    }

    private fun checkDueDate(invoiceDto: InvoiceDto) {
        if (invoiceDto.dueDate!!.isBefore(invoiceDto.dateOfIssue)) {
            throw ServiceRuntimeException("invoice.dueDate.before.dateOfIssue")
        }
    }

    private fun correctIncomeAmounts(invoice: Invoice?) {
        invoice!!.totalAmount = invoice.totalAmount!!.setScale(AMOUNT_SCALE, RoundingMode.HALF_UP)
        invoice.totalVat = invoice.totalVat!!.setScale(AMOUNT_SCALE, RoundingMode.HALF_UP)
        invoice.unpaidAmount = invoice.totalAmount
    }

    private fun computeAmounts(invoice: Invoice?) {
        var totalAmount = BigDecimal.ZERO
        var totalVat = BigDecimal.ZERO
        for (invoiceItem in invoice!!.items) {
            totalVat = totalVat.add(invoiceItem.vat)
            totalAmount = totalAmount.add(invoiceItem.totalWithVat)
        }
        invoice.totalAmount = totalAmount.setScale(AMOUNT_SCALE, RoundingMode.HALF_UP)
        invoice.unpaidAmount = totalAmount.setScale(AMOUNT_SCALE, RoundingMode.HALF_UP)
        invoice.totalVat = totalVat.setScale(AMOUNT_SCALE, RoundingMode.HALF_UP)
    }

    companion object {
        private const val AMOUNT_SCALE = 2
    }
}