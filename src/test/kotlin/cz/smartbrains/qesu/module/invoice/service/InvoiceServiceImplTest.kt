package cz.smartbrains.qesu.module.invoice.service

import org.mockito.kotlin.any
import cz.smartbrains.qesu.module.common.exception.ServiceRuntimeException
import cz.smartbrains.qesu.module.invoice.dto.InvoiceDto
import cz.smartbrains.qesu.module.invoice.dto.InvoiceItemDto
import cz.smartbrains.qesu.module.invoice.entity.Invoice
import cz.smartbrains.qesu.module.invoice.entity.InvoiceItem
import cz.smartbrains.qesu.module.invoice.entity.InvoiceSeries
import cz.smartbrains.qesu.module.invoice.entity.InvoiceSeriesNumber
import cz.smartbrains.qesu.module.invoice.mapper.InvoiceMapper
import cz.smartbrains.qesu.module.invoice.repository.InvoiceRepository
import cz.smartbrains.qesu.module.invoice.type.InvoiceType
import cz.smartbrains.qesu.module.order.repository.OrderStatementRepository
import org.assertj.core.api.Assertions
import org.assertj.core.util.Lists
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.util.*

@ExtendWith(MockitoExtension::class)
class InvoiceServiceImplTest {
    @Mock
    private lateinit var invoiceRepository: InvoiceRepository

    @Mock
    private lateinit var invoiceSeriesService: InvoiceSeriesService
    @Mock
    private lateinit var invoiceMapper: InvoiceMapper
    @Mock
    private lateinit var orderStatementRepository: OrderStatementRepository

    @Captor
    private lateinit var invoiceCaptor: ArgumentCaptor<Invoice>

    private lateinit var service: InvoiceServiceImpl

    @BeforeEach
    fun setUp() {
        service = InvoiceServiceImpl(invoiceRepository, orderStatementRepository, invoiceSeriesService, invoiceMapper)
    }

    @Test
    fun checkCreate_invoiceOutcome_seriesShouldBeChecked() {
        val invoiceDto = InvoiceDto()
        invoiceDto.type = InvoiceType.OUTCOME
        invoiceDto.items!!.add(InvoiceItemDto())

        Assertions.assertThatExceptionOfType(ServiceRuntimeException::class.java)
                .isThrownBy { service.checkCreateInvoice(invoiceDto) }
                .withMessage("invoice.create.outcome.series.required")
    }

    @Test
    fun checkCreate_invoiceOutcome_itemsShouldBeChecked() {
        val invoiceDto = InvoiceDto()
        val invoiceSeries = InvoiceSeries()
        invoiceSeries.id = 2L
        invoiceDto.type = InvoiceType.OUTCOME
        invoiceDto.invoiceSeries = invoiceSeries
        Assertions.assertThatExceptionOfType(ServiceRuntimeException::class.java)
                .isThrownBy { service.checkCreateInvoice(invoiceDto) }
                .withMessage("invoice.create.outcome.items.empty")
    }

    @Test
    fun checkCreate_invoiceIncome_numberIsNotNullCheck() {
        val invoiceDto = InvoiceDto()
        invoiceDto.type = InvoiceType.INCOME
        Assertions.assertThatExceptionOfType(ServiceRuntimeException::class.java)
                .isThrownBy { service.checkCreateInvoice(invoiceDto) }
                .withMessage("invoice.create.income.number.required")
    }

    @Test
    fun checkCreate_invoiceIncome_itemsShouldBeChecked() {
        val invoiceDto = InvoiceDto()
        invoiceDto.type = InvoiceType.INCOME
        invoiceDto.number = INVOICE_NUMBER
        invoiceDto.items = Lists.newArrayList(InvoiceItemDto())
        Assertions.assertThatExceptionOfType(ServiceRuntimeException::class.java)
                .isThrownBy { service.checkCreateInvoice(invoiceDto) }
                .withMessage("invoice.create.income.items.notEmpty")
    }

    @Test
    fun checkCreate_invoiceIncome_totalAmountShouldBeChecked() {
        val invoiceDto = InvoiceDto()
        invoiceDto.type = InvoiceType.INCOME
        invoiceDto.number = INVOICE_NUMBER
        Assertions.assertThatExceptionOfType(ServiceRuntimeException::class.java)
                .isThrownBy { service.checkCreateInvoice(invoiceDto) }
                .withMessage("invoice.create.income.totalAmount.required")
    }

    @Test
    fun checkCreate_invoiceIncome_totalVatShouldBeChecked() {
        val invoiceDto = InvoiceDto()
        invoiceDto.type = InvoiceType.INCOME
        invoiceDto.number = INVOICE_NUMBER
        invoiceDto.totalAmount = BigDecimal.TEN
        Assertions.assertThatExceptionOfType(ServiceRuntimeException::class.java)
                .isThrownBy { service.checkCreateInvoice(invoiceDto) }
                .withMessage("invoice.create.income.totalVat.required")
    }

    @Test
    fun checkCreate_dueDateShouldBeChecked() {
        val invoiceDto = InvoiceDto()
        invoiceDto.type = InvoiceType.INCOME
        invoiceDto.number = INVOICE_NUMBER
        invoiceDto.totalAmount = BigDecimal.TEN
        invoiceDto.totalVat = BigDecimal.ZERO
        invoiceDto.dateOfIssue = LocalDate.now()
        invoiceDto.dueDate = invoiceDto.dateOfIssue!!.minusDays(1)
        Assertions.assertThatExceptionOfType(ServiceRuntimeException::class.java)
                .isThrownBy { service.checkCreateInvoice(invoiceDto) }
                .withMessage("invoice.dueDate.before.dateOfIssue")
    }

    @Test
    fun checkUpdate_invoiceOutcome_unpaidAmountShouldBeChecked() {
        val invoiceDto = InvoiceDto()
        val invoiceSeries = InvoiceSeries()
        invoiceSeries.id = 2L
        invoiceDto.type = InvoiceType.OUTCOME
        invoiceDto.invoiceSeries = invoiceSeries
        val originalInvoice = Invoice()
        originalInvoice.totalAmount = BigDecimal.TEN
        originalInvoice.unpaidAmount = BigDecimal.ZERO
        Assertions.assertThatExceptionOfType(ServiceRuntimeException::class.java)
                .isThrownBy { service.checkUpdateInvoice(invoiceDto, originalInvoice) }
                .withMessage("invoice.update.invalid.unpaidAmount")
    }

    @Test
    fun checkUpdate_invoiceOutcome_itemsShouldBeChecked() {
        val invoiceDto = InvoiceDto()
        val invoiceSeries = InvoiceSeries()
        invoiceSeries.id = 2L
        invoiceDto.type = InvoiceType.OUTCOME
        invoiceDto.invoiceSeries = invoiceSeries
        invoiceDto.dateOfIssue = LocalDate.now()
        invoiceDto.dueDate = LocalDate.now()
        val originalInvoice = Invoice()
        originalInvoice.totalAmount = BigDecimal.TEN
        originalInvoice.unpaidAmount = BigDecimal.TEN
        Assertions.assertThatExceptionOfType(ServiceRuntimeException::class.java)
                .isThrownBy { service.checkUpdateInvoice(invoiceDto, originalInvoice) }
                .withMessage("invoice.create.outcome.items.empty")
    }

    @Test
    fun checkUpdate_invoiceIncome_numberIsNotNullCheck() {
        val invoiceDto = InvoiceDto()
        invoiceDto.type = InvoiceType.INCOME
        val originalInvoice = Invoice()
        originalInvoice.totalAmount = BigDecimal.TEN
        originalInvoice.unpaidAmount = BigDecimal.TEN
        invoiceDto.dateOfIssue = LocalDate.now()
        invoiceDto.dueDate = LocalDate.now()
        Assertions.assertThatExceptionOfType(ServiceRuntimeException::class.java)
                .isThrownBy { service.checkUpdateInvoice(invoiceDto, originalInvoice) }
                .withMessage("invoice.create.income.number.required")
    }

    @Test
    fun checkUpdate_invoiceIncome_itemsShouldBeChecked() {
        val invoiceDto = InvoiceDto()
        invoiceDto.type = InvoiceType.INCOME
        invoiceDto.number = INVOICE_NUMBER
        invoiceDto.dateOfIssue = LocalDate.now()
        invoiceDto.dueDate = LocalDate.now()
        invoiceDto.items = Lists.newArrayList(InvoiceItemDto())
        val originalInvoice = Invoice()
        originalInvoice.totalAmount = BigDecimal.TEN
        originalInvoice.unpaidAmount = BigDecimal.TEN
        Assertions.assertThatExceptionOfType(ServiceRuntimeException::class.java)
                .isThrownBy { service.checkUpdateInvoice(invoiceDto, originalInvoice) }
                .withMessage("invoice.create.income.items.notEmpty")
    }

    @Test
    fun checkUpdate_invoiceIncome_totalAmountShouldBeChecked() {
        val invoiceDto = InvoiceDto()
        invoiceDto.type = InvoiceType.INCOME
        invoiceDto.number = INVOICE_NUMBER
        invoiceDto.dateOfIssue = LocalDate.now()
        invoiceDto.dueDate = LocalDate.now()
        val originalInvoice = Invoice()
        originalInvoice.totalAmount = BigDecimal.TEN
        originalInvoice.unpaidAmount = BigDecimal.TEN
        Assertions.assertThatExceptionOfType(ServiceRuntimeException::class.java)
                .isThrownBy { service.checkUpdateInvoice(invoiceDto, originalInvoice) }
                .withMessage("invoice.create.income.totalAmount.required")
    }

    @Test
    fun checkUpdate_invoiceIncome_totalVatShouldBeChecked() {
        val invoiceDto = InvoiceDto()
        invoiceDto.type = InvoiceType.INCOME
        invoiceDto.number = INVOICE_NUMBER
        invoiceDto.totalAmount = BigDecimal.TEN
        invoiceDto.dateOfIssue = LocalDate.now()
        invoiceDto.dueDate = LocalDate.now()
        val originalInvoice = Invoice()
        originalInvoice.totalAmount = BigDecimal.TEN
        originalInvoice.unpaidAmount = BigDecimal.TEN
        Assertions.assertThatExceptionOfType(ServiceRuntimeException::class.java)
                .isThrownBy { service.checkUpdateInvoice(invoiceDto, originalInvoice) }
                .withMessage("invoice.create.income.totalVat.required")
    }

    @Test
    fun checkUpdate_dueDateShouldBeChecked() {
        val invoiceDto = InvoiceDto()
        invoiceDto.type = InvoiceType.INCOME
        invoiceDto.number = INVOICE_NUMBER
        invoiceDto.totalAmount = BigDecimal.TEN
        invoiceDto.dateOfIssue = LocalDate.now()
        invoiceDto.dueDate = invoiceDto.dateOfIssue!!.minusDays(1)
        val originalInvoice = Invoice()
        originalInvoice.totalAmount = BigDecimal.TEN
        originalInvoice.unpaidAmount = BigDecimal.TEN
        Assertions.assertThatExceptionOfType(ServiceRuntimeException::class.java)
                .isThrownBy { service.checkUpdateInvoice(invoiceDto, originalInvoice) }
                .withMessage("invoice.dueDate.before.dateOfIssue")
    }

    @Test
    fun checkCreate_invoiceOutcome_numberAndVSAreGenerated() {
        val invoiceDto = InvoiceDto()
        val invoiceSeries = InvoiceSeries()
        invoiceSeries.id = 2L
        invoiceDto.dateOfIssue = LocalDate.now()
        invoiceDto.dueDate = LocalDate.now()
        invoiceDto.type = InvoiceType.OUTCOME
        invoiceDto.type = InvoiceType.OUTCOME
        invoiceDto.invoiceSeries = invoiceSeries
        invoiceDto.items = Lists.newArrayList(InvoiceItemDto())
        val transformedInvoice = Invoice()
        transformedInvoice.type = InvoiceType.OUTCOME
        transformedInvoice.number = "NUMBER-to-be-overwritten"
        transformedInvoice.variableSymbol = "VS-to-be-overwritten"
        Mockito.`when`(invoiceMapper.dtoToDo(invoiceDto)).thenReturn(transformedInvoice)
        Mockito.`when`(invoiceRepository.save(any())).thenReturn(transformedInvoice)
        Mockito.`when`(invoiceMapper.doToDto(transformedInvoice)).thenReturn(Mockito.mock(InvoiceDto::class.java))
        mockInvoiceSeriesService()
        service.create(invoiceDto)
        Mockito.verify(invoiceRepository)!!.save(invoiceCaptor.capture())
        val invoiceToBePersisted = invoiceCaptor.value
        Assertions.assertThat(invoiceToBePersisted.number).isEqualTo(INVOICE_NUMBER)
        Assertions.assertThat(invoiceToBePersisted.variableSymbol).isEqualTo(VARIABLE_SYMBOL)
        Assertions.assertThat(invoiceToBePersisted.created).isNotNull
    }

    @Test
    fun create_invoiceIncome_numberAndVSAreNotGenerated() {
        val number = "NUMBER-to-be-overwritten"
        val variableSymbol = "VS-to-be-overwritten"
        val invoiceDto = InvoiceDto()
        invoiceDto.type = InvoiceType.INCOME
        invoiceDto.number = number
        invoiceDto.totalAmount = BigDecimal.TEN
        invoiceDto.totalVat = BigDecimal.ZERO
        invoiceDto.dateOfIssue = LocalDate.now()
        invoiceDto.dueDate = LocalDate.now()
        val transformedInvoice = Invoice()
        transformedInvoice.type = InvoiceType.INCOME
        transformedInvoice.number = number
        transformedInvoice.variableSymbol = variableSymbol
        transformedInvoice.totalAmount = BigDecimal.TEN
        transformedInvoice.totalVat = BigDecimal.ZERO
        Mockito.`when`(invoiceMapper.dtoToDo(invoiceDto)).thenReturn(transformedInvoice)
        Mockito.`when`(invoiceRepository.save(any())).thenReturn(transformedInvoice)
        Mockito.`when`(invoiceMapper.doToDto(transformedInvoice)).thenReturn(Mockito.mock(InvoiceDto::class.java))
        service.create(invoiceDto)
        Mockito.verify(invoiceRepository)!!.save(invoiceCaptor.capture())
        val invoiceToBePersisted = invoiceCaptor.value
        Assertions.assertThat(invoiceToBePersisted.number).isEqualTo(number)
        Assertions.assertThat(invoiceToBePersisted.variableSymbol).isEqualTo(variableSymbol)
        Assertions.assertThat(invoiceToBePersisted.created).isNotNull
    }

    @Test
    fun create_invoiceOutcome_amountsAreComputed() {
        val invoiceDto = InvoiceDto()
        invoiceDto.type = InvoiceType.OUTCOME
        val invoiceSeries = InvoiceSeries()
        invoiceSeries.id = 2L
        invoiceDto.invoiceSeries = invoiceSeries
        invoiceDto.items = Lists.newArrayList(InvoiceItemDto())
        val transformedInvoice = Invoice()
        transformedInvoice.type = InvoiceType.OUTCOME
        val item1 = InvoiceItem()
        val item2 = InvoiceItem()
        item1.vatRate = BigDecimal.valueOf(21)
        item1.price = BigDecimal.valueOf(100)
        item1.number = BigDecimal.valueOf(5)
        item2.vatRate = BigDecimal.valueOf(10)
        item2.price = BigDecimal.valueOf(50)
        item2.number = BigDecimal.valueOf(8)
        invoiceDto.dateOfIssue = LocalDate.now()
        invoiceDto.dueDate = LocalDate.now()
        transformedInvoice.items.add(item1)
        transformedInvoice.items.add(item2)
        Mockito.`when`(invoiceMapper.dtoToDo(invoiceDto)).thenReturn(transformedInvoice)
        Mockito.`when`(invoiceRepository.save(any())).thenReturn(transformedInvoice)
        Mockito.`when`(invoiceMapper.doToDto(transformedInvoice)).thenReturn(Mockito.mock(InvoiceDto::class.java))
        mockInvoiceSeriesService()
        service.create(invoiceDto)
        Mockito.verify(invoiceRepository)!!.save(invoiceCaptor.capture())
        val invoiceToBePersisted = invoiceCaptor.value
        Assertions.assertThat(invoiceToBePersisted.totalVat).isEqualTo(BigDecimal.valueOf(100 * 5 * 0.21 + 8 * 50 * 0.1).setScale(2, RoundingMode.HALF_UP))
        Assertions.assertThat(invoiceToBePersisted.totalAmount).isEqualTo(BigDecimal.valueOf(100 * 5 + 100 * 5 * 21 / 100 + 8 * 50 + (8 * 50 * 10 / 100).toLong()).setScale(2, RoundingMode.HALF_UP))
        Assertions.assertThat(invoiceToBePersisted.unpaidAmount).isEqualTo(invoiceToBePersisted.totalAmount)
    }

    @Test
    fun create_invoiceIncome_amountsArePersisted() {
        val totalAmount = BigDecimal.valueOf(12345)
        val totalVat = BigDecimal.valueOf(987)
        val invoiceDto = InvoiceDto()
        invoiceDto.type = InvoiceType.INCOME
        invoiceDto.number = INVOICE_NUMBER
        val invoiceSeries = InvoiceSeries()
        invoiceSeries.id = 2L
        invoiceDto.invoiceSeries = invoiceSeries
        invoiceDto.totalAmount = totalAmount
        invoiceDto.totalVat = totalVat
        invoiceDto.dateOfIssue = LocalDate.now()
        invoiceDto.dueDate = LocalDate.now()
        val transformedInvoice = Invoice()
        transformedInvoice.totalAmount = totalAmount
        transformedInvoice.totalVat = totalVat
        transformedInvoice.type = InvoiceType.INCOME
        Mockito.`when`(invoiceMapper.dtoToDo(invoiceDto)).thenReturn(transformedInvoice)
        Mockito.`when`(invoiceRepository.save(any())).thenReturn(transformedInvoice)
        Mockito.`when`(invoiceMapper.doToDto(transformedInvoice)).thenReturn(Mockito.mock(InvoiceDto::class.java))
        service.create(invoiceDto)
        Mockito.verify(invoiceRepository)!!.save(invoiceCaptor.capture())
        val invoiceToBePersisted = invoiceCaptor.value
        Assertions.assertThat(invoiceToBePersisted.totalVat).isEqualTo(totalVat.setScale(2, RoundingMode.HALF_UP))
        Assertions.assertThat(invoiceToBePersisted.totalAmount).isEqualTo(totalAmount.setScale(2, RoundingMode.HALF_UP))
        Assertions.assertThat(invoiceToBePersisted.unpaidAmount).isEqualTo(totalAmount.setScale(2, RoundingMode.HALF_UP))
    }

    @Test
    fun update_invoiceOutcome_amountsAreComputed() {
        val invoiceDto = InvoiceDto()
        val invoiceId = 1L
        invoiceDto.id = invoiceId
        invoiceDto.type = InvoiceType.OUTCOME
        invoiceDto.items = Lists.newArrayList(InvoiceItemDto())
        invoiceDto.dateOfIssue = LocalDate.now()
        invoiceDto.dueDate = LocalDate.now()
        val transformedInvoice = Invoice()
        transformedInvoice.type = InvoiceType.OUTCOME
        val item1 = InvoiceItem()
        val item2 = InvoiceItem()
        item1.vatRate = BigDecimal.valueOf(21)
        item1.price = BigDecimal.valueOf(100)
        item1.number = BigDecimal.valueOf(5)
        item2.vatRate = BigDecimal.valueOf(10)
        item2.price = BigDecimal.valueOf(50)
        item2.number = BigDecimal.valueOf(8)
        transformedInvoice.items.addAll(listOf(item1, item2))
        Mockito.`when`(invoiceMapper.dtoToDo(invoiceDto)).thenReturn(transformedInvoice)
        val originalInvoice = Invoice()
        originalInvoice.type = InvoiceType.OUTCOME
        originalInvoice.unpaidAmount = BigDecimal.TEN
        originalInvoice.totalAmount = BigDecimal.TEN
        Mockito.`when`(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(originalInvoice))
        Mockito.`when`(invoiceRepository.save(any())).thenReturn(transformedInvoice)
        Mockito.`when`(invoiceMapper.doToDto(transformedInvoice)).thenReturn(Mockito.mock(InvoiceDto::class.java))
        service.update(invoiceDto)
        Mockito.verify(invoiceRepository).save(invoiceCaptor.capture())
        val invoiceToBePersisted = invoiceCaptor.value
        Assertions.assertThat(invoiceToBePersisted).isSameAs(originalInvoice)
        Assertions.assertThat(invoiceToBePersisted.totalVat).isEqualTo(BigDecimal.valueOf(100 * 5 * 0.21 + 8 * 50 * 0.1).setScale(2, RoundingMode.HALF_UP))
        Assertions.assertThat(invoiceToBePersisted.totalAmount).isEqualTo(BigDecimal.valueOf(100 * 5 + 100 * 5 * 21 / 100 + 8 * 50 + (8 * 50 * 10 / 100).toLong()).setScale(2, RoundingMode.HALF_UP))
        Assertions.assertThat(invoiceToBePersisted.unpaidAmount).isEqualTo(invoiceToBePersisted.totalAmount)
    }

    @Test
    fun update_invoiceIncome_amountsArePersisted() {
        val invoiceId = 1L
        val totalAmount = BigDecimal.valueOf(12345)
        val totalVat = BigDecimal.valueOf(987)
        val invoiceDto = InvoiceDto()
        invoiceDto.id = invoiceId
        invoiceDto.type = InvoiceType.INCOME
        invoiceDto.number = INVOICE_NUMBER
        val invoiceSeries = InvoiceSeries()
        invoiceSeries.id = 2L
        invoiceDto.invoiceSeries = invoiceSeries
        invoiceDto.totalAmount = totalAmount
        invoiceDto.totalVat = totalVat
        invoiceDto.dateOfIssue = LocalDate.now()
        invoiceDto.dueDate = LocalDate.now()
        val transformedInvoice = Invoice()
        transformedInvoice.totalAmount = totalAmount
        transformedInvoice.totalVat = totalVat
        transformedInvoice.type = InvoiceType.INCOME
        val originalInvoice = Invoice()
        originalInvoice.type = InvoiceType.INCOME
        originalInvoice.unpaidAmount = BigDecimal.TEN
        originalInvoice.totalAmount = BigDecimal.TEN
        Mockito.`when`(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(originalInvoice))
        Mockito.`when`(invoiceMapper.dtoToDo(invoiceDto)).thenReturn(transformedInvoice)
        Mockito.`when`(invoiceRepository.save(any())).thenReturn(transformedInvoice)
        Mockito.`when`(invoiceMapper.doToDto(transformedInvoice)).thenReturn(Mockito.mock(InvoiceDto::class.java))
        service.update(invoiceDto)
        Mockito.verify(invoiceRepository).save(invoiceCaptor.capture())
        val invoiceToBePersisted = invoiceCaptor.value
        Assertions.assertThat(invoiceToBePersisted.totalVat).isEqualTo(totalVat.setScale(2, RoundingMode.HALF_UP))
        Assertions.assertThat(invoiceToBePersisted.totalAmount).isEqualTo(totalAmount.setScale(2, RoundingMode.HALF_UP))
        Assertions.assertThat(invoiceToBePersisted.unpaidAmount).isEqualTo(totalAmount.setScale(2, RoundingMode.HALF_UP))
    }

    @Test
    fun pay_invalidNegativeAmount_exIsThrown() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java)
                .isThrownBy { service.pay(1L, BigDecimal.valueOf(-1)) }
                .withMessage("invoice.pay.amount.negative")
    }

    @Test
    fun pay_amountBiggerThenUnpaid_exIsThrown() {
        val invoiceId = 1L
        val unpaidAmount = BigDecimal.valueOf(100)
        val invoice = Invoice()
        invoice.unpaidAmount = unpaidAmount
        Mockito.`when`(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice))
        Assertions.assertThatThrownBy {
            service.pay(invoiceId, unpaidAmount.add(BigDecimal.ONE))
        }.isInstanceOf(ServiceRuntimeException::class.java)
    }

    @Test
    fun pay_validAmountNotCoverAllUnpaidAmount_remainingAmountIsValid() {
        val invoiceId = 1L
        val unpaidAmount = BigDecimal.valueOf(100)
        val invoice = Invoice()
        invoice.unpaidAmount = unpaidAmount
        Mockito.`when`(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice))
        service.pay(invoiceId, BigDecimal.valueOf(40L))
        Assertions.assertThat(invoice.unpaidAmount).isEqualTo(BigDecimal.valueOf(60))
    }

    @Test
    fun pay_validAmountCoverAllUnpaidAmount_remainingAmountIsZero() {
        val invoiceId = 1L
        val unpaidAmount = BigDecimal.valueOf(100)
        val invoice = Invoice()
        invoice.unpaidAmount = unpaidAmount
        Mockito.`when`(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice))
        service.pay(invoiceId, unpaidAmount)
        Assertions.assertThat(invoice.unpaidAmount).isEqualTo(BigDecimal.ZERO)
    }

    @Test
    fun cancelPayment_invalidNegativeAmount_exIsThrown() {
        Assertions.assertThatThrownBy {
            service.cancelPayment(1L, BigDecimal.valueOf(-1))
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun cancelPayment_amountBiggerThenPaid_exIsThrown() {
        val invoiceId = 1L
        val totalAmount = BigDecimal.valueOf(100)
        val unpaidAmount = BigDecimal.valueOf(50)
        val invoice = Invoice()
        invoice.unpaidAmount = unpaidAmount
        invoice.totalAmount = totalAmount
        Mockito.`when`(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice))
        Assertions.assertThatThrownBy {
            service.cancelPayment(invoiceId, BigDecimal.valueOf(60))
        }.isInstanceOf(ServiceRuntimeException::class.java)
    }

    @Test
    fun cancelPayment_validAmountNotCoverAllUnpaidAmount_remainingAmountIsValid() {
        val invoiceId = 1L
        val totalAmount = BigDecimal.valueOf(100)
        val unpaidAmount = BigDecimal.valueOf(50)
        val invoice = Invoice()
        invoice.unpaidAmount = unpaidAmount
        invoice.totalAmount = totalAmount
        Mockito.`when`(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice))
        service.cancelPayment(invoiceId, BigDecimal.valueOf(40L))
        Assertions.assertThat(invoice.unpaidAmount).isEqualTo(BigDecimal.valueOf(90))
    }

    @Test
    fun cancelPayment_validAmountCoverAllUnpaidAmount_remainingAmountIsTotalAmount() {
        val invoiceId = 1L
        val totalAmount = BigDecimal.valueOf(100)
        val unpaidAmount = BigDecimal.valueOf(10)
        val invoice = Invoice()
        invoice.unpaidAmount = unpaidAmount
        invoice.totalAmount = totalAmount
        Mockito.`when`(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice))
        service.cancelPayment(invoiceId, BigDecimal.valueOf(90))
        Assertions.assertThat(invoice.unpaidAmount).isEqualTo(totalAmount)
    }

    private fun mockInvoiceSeriesService() {
        Mockito.`when`(invoiceSeriesService.nextNumber(Mockito.anyLong()))
                .thenReturn(InvoiceSeriesNumber(INVOICE_NUMBER, VARIABLE_SYMBOL))
    }

    companion object {
        private const val INVOICE_NUMBER = "VF111"
        private const val VARIABLE_SYMBOL = "111"
    }
}