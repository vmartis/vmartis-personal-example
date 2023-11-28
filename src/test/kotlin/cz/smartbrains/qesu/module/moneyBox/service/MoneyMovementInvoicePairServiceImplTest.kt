package cz.smartbrains.qesu.module.moneyBox.service

import cz.smartbrains.qesu.module.bank.entity.BankAccount
import cz.smartbrains.qesu.module.common.exception.ServiceRuntimeException
import cz.smartbrains.qesu.module.company.entity.Company
import cz.smartbrains.qesu.module.invoice.dto.InvoiceDto
import cz.smartbrains.qesu.module.invoice.entity.Invoice
import cz.smartbrains.qesu.module.invoice.repository.InvoiceRepository
import cz.smartbrains.qesu.module.invoice.service.InvoiceService
import cz.smartbrains.qesu.module.invoice.type.InvoiceType
import cz.smartbrains.qesu.module.moneyBox.dto.BankMoneyMovementDto
import cz.smartbrains.qesu.module.moneyBox.dto.CashMoneyBoxDto
import cz.smartbrains.qesu.module.moneyBox.dto.CashMoneyMovementDto
import cz.smartbrains.qesu.module.moneyBox.entity.*
import cz.smartbrains.qesu.module.moneyBox.repository.MoneyBoxRepository
import cz.smartbrains.qesu.module.moneyBox.type.MoneyMovementType
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.util.*

@ExtendWith(MockitoExtension::class)
class MoneyMovementInvoicePairServiceImplTest {
    @Mock
    private val invoiceService: InvoiceService? = null

    @Mock
    private val invoiceRepository: InvoiceRepository? = null

    @Mock
    private val moneyBoxRepository: MoneyBoxRepository? = null
    private var service: MoneyMovementInvoicePairService? = null

    @BeforeEach
    fun setUp() {
        service = MoneyMovementInvoicePairServiceImpl(invoiceService!!, invoiceRepository!!, moneyBoxRepository!!)
    }

    @Test
    fun payInvoice_bankMovementWithoutInvoiceUpdatedWithInvoice_invoicePayIsCalled() {
        val totalAmount = BigDecimal.valueOf(200)
        val invoiceId = 1L
        val currency = "CZK"
        val originalMovement = BankMoneyMovement()
        val newMoneyMovement = BankMoneyMovementDto()
        val newInvoiceToBePaid = InvoiceDto()
        val newInvoiceToBePaidEntity = Invoice()
        val moneyBox: MoneyBox = BankMoneyBox()
        newMoneyMovement.totalAmount = totalAmount
        newInvoiceToBePaid.id = invoiceId
        newMoneyMovement.invoice = newInvoiceToBePaid
        originalMovement.moneyBox = moneyBox
        originalMovement.type = MoneyMovementType.INCOME
        moneyBox.currency = currency
        newInvoiceToBePaidEntity.currency = currency
        Mockito.`when`(invoiceRepository!!.findById(invoiceId)).thenReturn(Optional.of(newInvoiceToBePaidEntity))
        service!!.payInvoice(newMoneyMovement, originalMovement)
        Mockito.verify(invoiceService)!!.pay(invoiceId, totalAmount)
    }

    @Test
    fun payInvoice_bankMovementWithoutInvoiceUpdatedWithInvoiceOfInvalidCurrency_throwsEx() {
        val totalAmount = BigDecimal.valueOf(200)
        val invoiceId = 1L
        val currency = "CZK"
        val currency2 = "EUR"
        val originalMovement = BankMoneyMovement()
        val newMoneyMovement = BankMoneyMovementDto()
        val newInvoiceToBePaid = InvoiceDto()
        val newInvoiceToBePaidEntity = Invoice()
        val moneyBox: MoneyBox = BankMoneyBox()
        newMoneyMovement.totalAmount = totalAmount
        newInvoiceToBePaid.id = invoiceId
        newMoneyMovement.invoice = newInvoiceToBePaid
        originalMovement.moneyBox = moneyBox
        originalMovement.type = MoneyMovementType.INCOME
        moneyBox.currency = currency2
        newInvoiceToBePaidEntity.currency = currency
        Mockito.`when`(invoiceRepository!!.findById(invoiceId)).thenReturn(Optional.of(newInvoiceToBePaidEntity))
        Assertions.assertThatThrownBy {
            service!!.payInvoice(newMoneyMovement, originalMovement)
        }.isInstanceOf(ServiceRuntimeException::class.java)
            .hasMessage("moneyMovement.invoice.currency.not.match")
    }

    @Test
    fun payInvoice_bankMovementWithoutInvoiceUpdatedWithInvoiceOfInvalidSubscriber_throwsEx() {
        val totalAmount = BigDecimal.valueOf(200)
        val invoiceId = 1L
        val subscriberId = 2L
        val correspondingAccountOwnerId = 3L
        val currency = "CZK"
        val originalMovement = BankMoneyMovement()
        val newMoneyMovement = BankMoneyMovementDto()
        val newInvoiceToBePaid = InvoiceDto()
        val newInvoiceToBePaidEntity = Invoice()
        val moneyBox: MoneyBox = BankMoneyBox()
        val invoiceSubscriber = Company()
        val correspondingAccountOwner = Company()
        val correspondingBankAccount = BankAccount()
        newMoneyMovement.totalAmount = totalAmount
        newInvoiceToBePaid.id = invoiceId
        newMoneyMovement.invoice = newInvoiceToBePaid
        originalMovement.moneyBox = moneyBox
        originalMovement.type = MoneyMovementType.INCOME
        originalMovement.correspondingBankAccount = correspondingBankAccount
        correspondingBankAccount.subject = correspondingAccountOwner
        invoiceSubscriber.id = subscriberId
        correspondingAccountOwner.id = correspondingAccountOwnerId
        moneyBox.currency = currency
        newInvoiceToBePaidEntity.type = InvoiceType.OUTCOME
        newInvoiceToBePaidEntity.currency = currency
        newInvoiceToBePaidEntity.subscriber = invoiceSubscriber
        Mockito.`when`(invoiceRepository!!.findById(invoiceId)).thenReturn(Optional.of(newInvoiceToBePaidEntity))

        Assertions.assertThatThrownBy {
            service!!.payInvoice(newMoneyMovement, originalMovement)
        }.isInstanceOf(ServiceRuntimeException::class.java)
            .hasMessage("moneyMovement.invoice.subscriber.not.match")
    }

    @Test
    fun payInvoice_bankMmovementWithoutInvoiceUpdatedWithInvoiceOfInvalidSupplier_throwsEx() {
        val totalAmount = BigDecimal.valueOf(200)
        val invoiceId = 1L
        val supplierId = 2L
        val correspondingAccountOwnerId = 3L
        val currency = "CZK"
        val originalMovement = BankMoneyMovement()
        val newMoneyMovement = BankMoneyMovementDto()
        val newInvoiceToBePaid = InvoiceDto()
        val newInvoiceToBePaidEntity = Invoice()
        val moneyBox: MoneyBox = BankMoneyBox()
        val invoiceSupplier = Company()
        val correspondingAccountOwner = Company()
        val correspondingBankAccount = BankAccount()
        newMoneyMovement.totalAmount = totalAmount
        newInvoiceToBePaid.id = invoiceId
        newMoneyMovement.invoice = newInvoiceToBePaid
        originalMovement.moneyBox = moneyBox
        originalMovement.type = MoneyMovementType.OUTCOME
        originalMovement.correspondingBankAccount = correspondingBankAccount
        correspondingBankAccount.subject = correspondingAccountOwner
        invoiceSupplier.id = supplierId
        correspondingAccountOwner.id = correspondingAccountOwnerId
        moneyBox.currency = currency
        newInvoiceToBePaidEntity.type = InvoiceType.INCOME
        newInvoiceToBePaidEntity.currency = currency
        newInvoiceToBePaidEntity.supplier = invoiceSupplier
        Mockito.`when`(invoiceRepository!!.findById(invoiceId)).thenReturn(Optional.of(newInvoiceToBePaidEntity))
        Assertions.assertThatThrownBy {
            service!!.payInvoice(newMoneyMovement, originalMovement)
        }.isInstanceOf(ServiceRuntimeException::class.java)
            .hasMessage("moneyMovement.invoice.supplier.not.match")
    }

    @Test
    fun payInvoice_outcomeBankMovementWithOutcomeInvoice_throwsEx() {
        val invoiceId = 1L
        val newInvoiceToBePaid = InvoiceDto()
        newInvoiceToBePaid.id = invoiceId
        newInvoiceToBePaid.type = InvoiceType.OUTCOME
        val newInvoiceToBePaidEntity = Invoice()
        newInvoiceToBePaidEntity.id = invoiceId
        newInvoiceToBePaidEntity.type = InvoiceType.OUTCOME
        val newMoneyMovement = BankMoneyMovementDto()
        newMoneyMovement.type = MoneyMovementType.OUTCOME
        newMoneyMovement.invoice = newInvoiceToBePaid
        val originalMovement = BankMoneyMovement()
        originalMovement.type = MoneyMovementType.OUTCOME
        Mockito.`when`(invoiceRepository!!.findById(invoiceId)).thenReturn(Optional.of(newInvoiceToBePaidEntity))
        Assertions.assertThatThrownBy {
            service!!.payInvoice(newMoneyMovement, originalMovement)
        }.isInstanceOf(ServiceRuntimeException::class.java)
            .hasMessage("money-movement.type.not.allowed")

    }

    @Test
    fun payInvoice_incomeBankMovementWithIncomeInvoice_throwsEx() {
        val invoiceId = 1L
        val newInvoiceToBePaid = InvoiceDto()
        newInvoiceToBePaid.id = invoiceId
        newInvoiceToBePaid.type = InvoiceType.INCOME
        val newInvoiceToBePaidEntity = Invoice()
        newInvoiceToBePaidEntity.id = invoiceId
        newInvoiceToBePaidEntity.type = InvoiceType.INCOME
        val newMoneyMovement = BankMoneyMovementDto()
        newMoneyMovement.type = MoneyMovementType.INCOME
        newMoneyMovement.invoice = newInvoiceToBePaid
        val originalMovement = BankMoneyMovement()
        originalMovement.type = MoneyMovementType.INCOME
        Mockito.`when`(invoiceRepository!!.findById(invoiceId)).thenReturn(Optional.of(newInvoiceToBePaidEntity))
        Assertions.assertThatThrownBy {
            service!!.payInvoice(newMoneyMovement, originalMovement)
        }.isInstanceOf(ServiceRuntimeException::class.java)
            .hasMessage("money-movement.type.not.allowed")
    }

    @Test
    fun payInvoice_bankMovementWithInvoiceUpdatedWithoutInvoice_invoiceCancelPaymentCalled() {
        val totalAmount = BigDecimal.valueOf(200)
        val invoiceId = 1L
        val originalMovement = BankMoneyMovement()
        val newMoneyMovement = BankMoneyMovementDto()
        val pairedInvoice = Invoice()
        pairedInvoice.id = invoiceId
        originalMovement.totalAmount = totalAmount
        originalMovement.invoice = pairedInvoice
        service!!.payInvoice(newMoneyMovement, originalMovement)
        Mockito.verify(invoiceService)!!.cancelPayment(invoiceId, totalAmount)
    }

    @Test
    fun payInvoice_bankMovementWithInvoiceUpdatedWithOtherInvoice_invoicePayIsCalled() {
        val totalAmount = BigDecimal.valueOf(200)
        val originalTotalAmount = BigDecimal.valueOf(200)
        val paidInvoiceId = 1L
        val newInvoiceToBePaidId = 2L
        val currency = "CZK"
        val originalMovement = BankMoneyMovement()
        val newMoneyMovement = BankMoneyMovementDto()
        val newInvoiceToBePaid = InvoiceDto()
        val paidInvoice = Invoice()
        val newInvoiceToBePaidEntity = Invoice()
        val moneyBox: MoneyBox = BankMoneyBox()
        paidInvoice.id = paidInvoiceId
        newInvoiceToBePaid.id = newInvoiceToBePaidId
        newMoneyMovement.totalAmount = totalAmount
        newMoneyMovement.invoice = newInvoiceToBePaid
        originalMovement.moneyBox = moneyBox
        originalMovement.invoice = paidInvoice
        originalMovement.type = MoneyMovementType.INCOME
        originalMovement.totalAmount = originalTotalAmount
        moneyBox.currency = currency
        newInvoiceToBePaidEntity.currency = currency
        Mockito.`when`(invoiceRepository!!.findById(newInvoiceToBePaidId)).thenReturn(Optional.of(newInvoiceToBePaidEntity))
        service!!.payInvoice(newMoneyMovement, originalMovement)
        Mockito.verify(invoiceService)!!.cancelPayment(paidInvoiceId, originalTotalAmount)
        Mockito.verify(invoiceService)!!.pay(newInvoiceToBePaidId, totalAmount)
    }

    @Test
    fun payInvoice_createCashMovement_invoicePayIsCalled() {
        val totalAmount = BigDecimal.valueOf(200)
        val newInvoiceToBePaidId = 2L
        val moneyBoxId = 3L
        val currency = "CZK"
        val newMoneyMovement = CashMoneyMovementDto()
        val newInvoiceToBePaid = InvoiceDto()
        val newInvoiceToBePaidEntity = Invoice()
        val moneyBox: MoneyBox = CashMoneyBox()
        val moneyBoxDto = CashMoneyBoxDto()
        moneyBoxDto.id = moneyBoxId
        moneyBox.id = moneyBoxId
        moneyBox.currency = currency
        newInvoiceToBePaid.id = newInvoiceToBePaidId
        newMoneyMovement.totalAmount = totalAmount
        newMoneyMovement.invoice = newInvoiceToBePaid
        newMoneyMovement.moneyBox = moneyBoxDto
        newInvoiceToBePaidEntity.currency = currency
        Mockito.`when`(moneyBoxRepository!!.findById(moneyBoxId)).thenReturn(Optional.of(moneyBox))
        Mockito.`when`(invoiceRepository!!.findById(newInvoiceToBePaidId)).thenReturn(Optional.of(newInvoiceToBePaidEntity))
        service!!.payInvoice(newMoneyMovement)
        Mockito.verify(invoiceService)!!.pay(newInvoiceToBePaidId, totalAmount)
    }

    @Test
    fun payInvoice_cashMovementWithoutInvoiceUpdatedWithInvoice_invoicePayIsCalled() {
        val totalAmount = BigDecimal.valueOf(200)
        val invoiceId = 1L
        val currency = "CZK"
        val originalMovement = CashMoneyMovement()
        val newMoneyMovement = CashMoneyMovementDto()
        val newInvoiceToBePaid = InvoiceDto()
        val newInvoiceToBePaidEntity = Invoice()
        val moneyBox: MoneyBox = CashMoneyBox()
        newMoneyMovement.totalAmount = totalAmount
        newInvoiceToBePaid.id = invoiceId
        newMoneyMovement.invoice = newInvoiceToBePaid
        originalMovement.moneyBox = moneyBox
        originalMovement.type = MoneyMovementType.INCOME
        moneyBox.currency = currency
        newInvoiceToBePaidEntity.currency = currency
        Mockito.`when`(invoiceRepository!!.findById(invoiceId)).thenReturn(Optional.of(newInvoiceToBePaidEntity))
        service!!.payInvoice(newMoneyMovement, originalMovement)
        Mockito.verify(invoiceService)!!.pay(invoiceId, totalAmount)
    }

    @Test
    fun payInvoice_cashMovementWithoutInvoiceUpdatedWithInvoiceOfInvalidCurrency_throwsEx() {
        val totalAmount = BigDecimal.valueOf(200)
        val invoiceId = 1L
        val currency = "CZK"
        val currency2 = "EUR"
        val originalMovement = CashMoneyMovement()
        val newMoneyMovement = CashMoneyMovementDto()
        val newInvoiceToBePaid = InvoiceDto()
        val newInvoiceToBePaidEntity = Invoice()
        val moneyBox: MoneyBox = CashMoneyBox()
        newMoneyMovement.totalAmount = totalAmount
        newInvoiceToBePaid.id = invoiceId
        newMoneyMovement.invoice = newInvoiceToBePaid
        originalMovement.moneyBox = moneyBox
        originalMovement.type = MoneyMovementType.INCOME
        moneyBox.currency = currency2
        newInvoiceToBePaidEntity.currency = currency
        Mockito.`when`(invoiceRepository!!.findById(invoiceId)).thenReturn(Optional.of(newInvoiceToBePaidEntity))
        Assertions.assertThatThrownBy {
            service!!.payInvoice(newMoneyMovement, originalMovement)
        }.isInstanceOf(ServiceRuntimeException::class.java)
            .hasMessage("moneyMovement.invoice.currency.not.match")
    }

    @Test
    fun payInvoice_outcomeCashMovementWithOutcomeInvoice_throwsEx() {
        val invoiceId = 1L
        val newInvoiceToBePaid = InvoiceDto()
        newInvoiceToBePaid.id = invoiceId
        newInvoiceToBePaid.type = InvoiceType.OUTCOME
        val newInvoiceToBePaidEntity = Invoice()
        newInvoiceToBePaidEntity.id = invoiceId
        newInvoiceToBePaidEntity.type = InvoiceType.OUTCOME
        val newMoneyMovement = CashMoneyMovementDto()
        newMoneyMovement.type = MoneyMovementType.OUTCOME
        newMoneyMovement.invoice = newInvoiceToBePaid
        val originalMovement = CashMoneyMovement()
        originalMovement.type = MoneyMovementType.OUTCOME
        Mockito.`when`(invoiceRepository!!.findById(invoiceId)).thenReturn(Optional.of(newInvoiceToBePaidEntity))
        Assertions.assertThatThrownBy {
            service!!.payInvoice(newMoneyMovement, originalMovement)
        }.isInstanceOf(ServiceRuntimeException::class.java)
            .hasMessage("money-movement.type.not.allowed")
    }

    @Test
    fun payInvoice_incomeCashMovementWithIncomeInvoice_throwsEx() {
        val invoiceId = 1L
        val newInvoiceToBePaid = InvoiceDto()
        newInvoiceToBePaid.id = invoiceId
        newInvoiceToBePaid.type = InvoiceType.INCOME
        val newInvoiceToBePaidEntity = Invoice()
        newInvoiceToBePaidEntity.id = invoiceId
        newInvoiceToBePaidEntity.type = InvoiceType.INCOME
        val newMoneyMovement = CashMoneyMovementDto()
        newMoneyMovement.type = MoneyMovementType.INCOME
        newMoneyMovement.invoice = newInvoiceToBePaid
        val originalMovement = CashMoneyMovement()
        originalMovement.type = MoneyMovementType.INCOME
        Mockito.`when`(invoiceRepository!!.findById(invoiceId)).thenReturn(Optional.of(newInvoiceToBePaidEntity))
        Assertions.assertThatThrownBy {
            service!!.payInvoice(newMoneyMovement, originalMovement)
        }.isInstanceOf(ServiceRuntimeException::class.java)
            .hasMessage("money-movement.type.not.allowed")
    }

    @Test
    fun payInvoice_cashMovementWithInvoiceUpdatedWithoutInvoice_invoiceCancelPaymentCalled() {
        val totalAmount = BigDecimal.valueOf(200)
        val invoiceId = 1L
        val originalMovement = CashMoneyMovement()
        val newMoneyMovement = CashMoneyMovementDto()
        val pairedInvoice = Invoice()
        pairedInvoice.id = invoiceId
        originalMovement.totalAmount = totalAmount
        originalMovement.invoice = pairedInvoice
        service!!.payInvoice(newMoneyMovement, originalMovement)
        Mockito.verify(invoiceService)!!.cancelPayment(invoiceId, totalAmount)
    }

    @Test
    fun payInvoice_cashMovementWithInvoiceDeleted_invoiceCancelPaymentCalled() {
        val totalAmount = BigDecimal.valueOf(200)
        val invoiceId = 1L
        val originalMovement = CashMoneyMovement()
        val pairedInvoice = Invoice()
        pairedInvoice.id = invoiceId
        originalMovement.totalAmount = totalAmount
        originalMovement.invoice = pairedInvoice
        service!!.payInvoice(null, originalMovement)
        Mockito.verify(invoiceService)!!.cancelPayment(invoiceId, totalAmount)
    }

    @Test
    fun payInvoice_cashMovementWithInvoiceUpdatedWithOtherInvoice_invoicePayIsCalled() {
        val totalAmount = BigDecimal.valueOf(200)
        val originalTotalAmount = BigDecimal.valueOf(200)
        val paidInvoiceId = 1L
        val newInvoiceToBePaidId = 2L
        val currency = "CZK"
        val originalMovement = CashMoneyMovement()
        val newMoneyMovement = CashMoneyMovementDto()
        val newInvoiceToBePaid = InvoiceDto()
        val paidInvoice = Invoice()
        val newInvoiceToBePaidEntity = Invoice()
        val moneyBox: MoneyBox = CashMoneyBox()
        paidInvoice.id = paidInvoiceId
        newInvoiceToBePaid.id = newInvoiceToBePaidId
        newMoneyMovement.totalAmount = totalAmount
        newMoneyMovement.invoice = newInvoiceToBePaid
        originalMovement.moneyBox = moneyBox
        originalMovement.invoice = paidInvoice
        originalMovement.type = MoneyMovementType.INCOME
        originalMovement.totalAmount = originalTotalAmount
        moneyBox.currency = currency
        newInvoiceToBePaidEntity.currency = currency
        Mockito.`when`(invoiceRepository!!.findById(newInvoiceToBePaidId)).thenReturn(Optional.of(newInvoiceToBePaidEntity))
        service!!.payInvoice(newMoneyMovement, originalMovement)
        Mockito.verify(invoiceService)!!.cancelPayment(paidInvoiceId, originalTotalAmount)
        Mockito.verify(invoiceService)!!.pay(newInvoiceToBePaidId, totalAmount)
    }
}