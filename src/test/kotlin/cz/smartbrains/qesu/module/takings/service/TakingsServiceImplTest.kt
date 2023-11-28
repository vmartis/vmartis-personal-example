package cz.smartbrains.qesu.module.takings.service

import cz.smartbrains.qesu.module.cashbox.entity.CashBox
import cz.smartbrains.qesu.module.common.exception.ServiceRuntimeException
import cz.smartbrains.qesu.module.common.service.Messages
import cz.smartbrains.qesu.module.company.entity.CompanyBranch
import cz.smartbrains.qesu.module.moneyBox.dto.CashMoneyBoxDto
import cz.smartbrains.qesu.module.moneyBox.dto.CashMoneyMovementDto
import cz.smartbrains.qesu.module.moneyBox.entity.CashMoneyBox
import cz.smartbrains.qesu.module.moneyBox.repository.CashMoneyBoxRepository
import cz.smartbrains.qesu.module.moneyBox.service.CashMoneyMovementService
import cz.smartbrains.qesu.module.moneyBox.type.AccountingType
import cz.smartbrains.qesu.module.moneyBox.type.MoneyMovementType
import cz.smartbrains.qesu.module.takings.dto.TakingsDto
import cz.smartbrains.qesu.module.takings.dto.TakingsTransferDto
import cz.smartbrains.qesu.module.takings.entity.Takings
import cz.smartbrains.qesu.module.takings.mapper.TakingsMapper
import cz.smartbrains.qesu.module.takings.repository.TakingsRepository
import cz.smartbrains.qesu.module.user.repository.UserRepository
import cz.smartbrains.qesu.security.AlfaUserDetails
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.capture
import org.mockito.kotlin.eq
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@ExtendWith(MockitoExtension::class)
class TakingsServiceImplTest {
    @Mock
    private lateinit var repository: TakingsRepository

    @Mock
    private lateinit var cashMoneyBoxRepository: CashMoneyBoxRepository

    @Mock
    private lateinit var mapper: TakingsMapper

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var cashMoneyMovementService: CashMoneyMovementService

    @Mock
    private lateinit var messages: Messages

    private val alfaUserDetails: AlfaUserDetails = AlfaUserDetails(10, "deer", "", true, emptyList())

    @Captor
    private lateinit var movementCaptor: ArgumentCaptor<CashMoneyMovementDto>

    private var service: TakingsService? = null

    @BeforeEach
    fun setUp() {
        service = TakingsServiceImpl(repository, cashMoneyBoxRepository, userRepository, mapper, cashMoneyMovementService, messages)
    }

    @Test
    fun update_transferredTaking_exIsThrown() {
        val dto = TakingsDto()
        dto.id = 1L
        val originEntity = Takings()
        originEntity.transferred = true
        Mockito.`when`(repository.findById(dto.id!!)).thenReturn(Optional.of(originEntity))
        Assertions.assertThatThrownBy {
            service!!.update(dto, alfaUserDetails)
        }.isInstanceOf(ServiceRuntimeException::class.java)
    }

    @Test
    fun update_invalidMinTotalCash_exIsThrown() {
        val dto = TakingsDto()
        dto.id = 1L
        dto.amountCash = BigDecimal.valueOf(200)
        dto.totalCashAmount = BigDecimal.valueOf(199)
        Assertions.assertThatThrownBy {
            service!!.update(dto, alfaUserDetails)
        }.isInstanceOf(ServiceRuntimeException::class.java)
    }

    @Test
    fun update_invalidMinTotalCheque_exIsThrown() {
        val dto = TakingsDto()
        dto.id = 1L
        dto.amountCheque = BigDecimal.valueOf(200)
        dto.totalChequeAmount = BigDecimal.valueOf(199)
        Assertions.assertThatThrownBy {
            service!!.update(dto, alfaUserDetails)
        }.isInstanceOf(ServiceRuntimeException::class.java)
    }

    @Test
    fun create_invalidMinTotalCash_exIsThrown() {
        val dto = TakingsDto()
        dto.id = 1L
        dto.amountCash = BigDecimal.valueOf(200)
        dto.totalCashAmount = BigDecimal.valueOf(199)
        Assertions.assertThatThrownBy {
            service!!.create(dto, alfaUserDetails)
        }.isInstanceOf(ServiceRuntimeException::class.java)
    }

    @Test
    fun create_invalidMinTotalCheque_exIsThrown() {
        val dto = TakingsDto()
        dto.id = 1L
        dto.amountCheque = BigDecimal.valueOf(200)
        dto.totalChequeAmount = BigDecimal.valueOf(199)
        Assertions.assertThatThrownBy {
            service!!.create(dto, alfaUserDetails)
        }.isInstanceOf(ServiceRuntimeException::class.java)
    }

    @Test
    fun transfer_transferredTakings_exIsThrown() {
        val transferDto = TakingsTransferDto()
        val moneyBox = CashMoneyBoxDto()
        moneyBox.id = 1L
        val takings = TakingsDto()
        takings.id = 2L
        transferDto.moneyBox = moneyBox
        transferDto.takings = takings
        val takingsEntity = Mockito.mock(Takings::class.java)
        Mockito.`when`(takingsEntity.transferred).thenReturn(true)
        val moneyBoxEntity = Mockito.mock(CashMoneyBox::class.java)
        Mockito.`when`(cashMoneyBoxRepository.findById(moneyBox.id!!)).thenReturn(Optional.of(moneyBoxEntity))
        Mockito.`when`(repository.findById(takings.id!!)).thenReturn(Optional.of(takingsEntity))
        Assertions.assertThatThrownBy {
            service!!.transfer(transferDto, alfaUserDetails)
        }.isInstanceOf(ServiceRuntimeException::class.java)
    }

    @Test
    fun transfer_invalidCurrency_exIsThrown() {
        val transferDto = TakingsTransferDto()
        val moneyBox = CashMoneyBoxDto()
        moneyBox.id = 1L
        val takings = TakingsDto()
        takings.id = 2L
        transferDto.moneyBox = moneyBox
        transferDto.takings = takings
        val takingsEntity = Mockito.mock(Takings::class.java)
        Mockito.`when`(takingsEntity.currency).thenReturn("CZK")
        Mockito.`when`(takingsEntity.transferred).thenReturn(false)
        val moneyBoxEntity = Mockito.mock(CashMoneyBox::class.java)
        Mockito.`when`(moneyBoxEntity.currency).thenReturn("EUR")
        Mockito.`when`(cashMoneyBoxRepository.findById(moneyBox.id!!)).thenReturn(Optional.of(moneyBoxEntity))
        Mockito.`when`(repository.findById(takings.id!!)).thenReturn(Optional.of(takingsEntity))
        Assertions.assertThatThrownBy {
           service!!.transfer(transferDto, alfaUserDetails)
        }.isInstanceOf(ServiceRuntimeException::class.java)
    }

    @Test
    fun transfer_fiscalMovementIsCreated() {
        val transferDto = TakingsTransferDto()
        val moneyBox = CashMoneyBoxDto()
        moneyBox.id = 1L
        val takings = TakingsDto()
        takings.id = 2L
        transferDto.moneyBox = moneyBox
        transferDto.takings = takings
        val takingsEntity = Mockito.mock(Takings::class.java)
        Mockito.`when`(takingsEntity.currency).thenReturn("CZK")
        Mockito.`when`(takingsEntity.transferred).thenReturn(false)
        Mockito.`when`(takingsEntity.date).thenReturn(LocalDate.now())
        Mockito.`when`(takingsEntity.totalFiscal).thenReturn(BigDecimal.valueOf(500L))
        Mockito.`when`(takingsEntity.amountCash).thenReturn(BigDecimal.valueOf(200L))
        Mockito.`when`(takingsEntity.amountCheque).thenReturn(BigDecimal.valueOf(300L))
        Mockito.`when`(takingsEntity.totalNonFiscal).thenReturn(BigDecimal.ZERO)
        val cashBox = Mockito.mock(CashBox::class.java)
        val companyBranch = Mockito.mock(CompanyBranch::class.java)
        Mockito.`when`(cashBox.name).thenReturn("Kasa 1")
        Mockito.`when`(cashBox.companyBranch).thenReturn(companyBranch)
        Mockito.`when`(companyBranch.name).thenReturn("Hlavna 4")
        Mockito.`when`(takingsEntity.cashBox).thenReturn(cashBox)
        val moneyBoxEntity = Mockito.mock(CashMoneyBox::class.java)
        Mockito.`when`(moneyBoxEntity.currency).thenReturn("CZK")
        Mockito.`when`(cashMoneyBoxRepository.findById(moneyBox.id!!)).thenReturn(Optional.of(moneyBoxEntity))
        Mockito.`when`(repository.findById(takings.id!!)).thenReturn(Optional.of(takingsEntity))
        Mockito.`when`(messages.getMessage("takings.transfer.note", "Hlavna 4", "Kasa 1")).thenReturn("Trzba - Hlavna 4 - Kasa1")
        service!!.transfer(transferDto, alfaUserDetails)
        Mockito.verify(cashMoneyMovementService)!!.create(capture(movementCaptor), eq(alfaUserDetails))
        Assertions.assertThat(movementCaptor.allValues).hasSize(1)
        Mockito.verify(takingsEntity).transferred = true
        val movement = movementCaptor.value
        Assertions.assertThat(movement.date!!.toLocalDate()).isEqualTo(takingsEntity.date)
        Assertions.assertThat(movement.taxableDate).isEqualTo(takingsEntity.date)
        Assertions.assertThat(movement.amountCash).isEqualTo(takingsEntity.amountCash)
        Assertions.assertThat(movement.amountCheque).isEqualTo(takingsEntity.amountCheque)
        Assertions.assertThat(movement.totalVat).isEqualTo(takingsEntity.totalVatAmount)
        Assertions.assertThat(movement.accountingType).isEqualTo(AccountingType.FISCAL)
        Assertions.assertThat(movement.type).isEqualTo(MoneyMovementType.INCOME)
        Assertions.assertThat(movement.note).isEqualTo("Trzba - Hlavna 4 - Kasa1")
        Assertions.assertThat(movement.moneyBox).isEqualTo(moneyBox)
    }

    @Test
    fun transfer_nonFiscalMovementIsCreated() {
        val transferDto = TakingsTransferDto()
        val moneyBox = CashMoneyBoxDto()
        moneyBox.id = 1L
        val takings = TakingsDto()
        takings.id = 2L
        transferDto.moneyBox = moneyBox
        transferDto.takings = takings
        val takingsEntity = Mockito.mock(Takings::class.java)
        Mockito.`when`(takingsEntity.currency).thenReturn("CZK")
        Mockito.`when`(takingsEntity.transferred).thenReturn(false)
        Mockito.`when`(takingsEntity.date).thenReturn(LocalDate.now())
        Mockito.`when`(takingsEntity.totalFiscal).thenReturn(BigDecimal.ZERO)
        Mockito.`when`(takingsEntity.totalNonFiscal).thenReturn(BigDecimal.valueOf(500L))
        Mockito.`when`(takingsEntity.cashNonFiscal).thenReturn(BigDecimal.valueOf(300L))
        Mockito.`when`(takingsEntity.chequeNonFiscal).thenReturn(BigDecimal.valueOf(200))
        val cashBox = Mockito.mock(CashBox::class.java)
        val companyBranch = Mockito.mock(CompanyBranch::class.java)
        Mockito.`when`(cashBox.name).thenReturn("Kasa 1")
        Mockito.`when`(cashBox.companyBranch).thenReturn(companyBranch)
        Mockito.`when`(companyBranch.name).thenReturn("Hlavna 4")
        Mockito.`when`(takingsEntity.cashBox).thenReturn(cashBox)
        val moneyBoxEntity = Mockito.mock(CashMoneyBox::class.java)
        Mockito.`when`(moneyBoxEntity.currency).thenReturn("CZK")
        Mockito.`when`(cashMoneyBoxRepository.findById(moneyBox.id!!)).thenReturn(Optional.of(moneyBoxEntity))
        Mockito.`when`(repository.findById(takings.id!!)).thenReturn(Optional.of(takingsEntity))
        Mockito.`when`(messages.getMessage("takings.transfer.note", "Hlavna 4", "Kasa 1")).thenReturn("Trzba - Hlavna 4 - Kasa1")
        service!!.transfer(transferDto, alfaUserDetails)
        Mockito.verify(cashMoneyMovementService)!!.create(capture(movementCaptor), eq(alfaUserDetails))
        Assertions.assertThat(movementCaptor.allValues).hasSize(1)
        Mockito.verify(takingsEntity).transferred = true
        val movement = movementCaptor.value
        Assertions.assertThat(movement.date!!.toLocalDate()).isEqualTo(takingsEntity.date)
        Assertions.assertThat(movement.taxableDate).isNull()
        Assertions.assertThat(movement.amountCash).isEqualTo(takingsEntity.cashNonFiscal)
        Assertions.assertThat(movement.amountCheque).isEqualTo(takingsEntity.chequeNonFiscal)
        Assertions.assertThat(movement.totalVat).isNull()
        Assertions.assertThat(movement.accountingType).isEqualTo(AccountingType.NON_FISCAL)
        Assertions.assertThat(movement.type).isEqualTo(MoneyMovementType.INCOME)
        Assertions.assertThat(movement.note).isEqualTo("Trzba - Hlavna 4 - Kasa1")
        Assertions.assertThat(movement.moneyBox).isEqualTo(moneyBox)
    }

    @Test
    fun transfer_bothMovementsAreCreated() {
        val transferDto = TakingsTransferDto()
        val moneyBox = CashMoneyBoxDto()
        moneyBox.id = 1L
        val takings = TakingsDto()
        takings.id = 2L
        transferDto.moneyBox = moneyBox
        transferDto.takings = takings
        val takingsEntity = Mockito.mock(Takings::class.java)
        Mockito.`when`(takingsEntity.currency).thenReturn("CZK")
        Mockito.`when`(takingsEntity.transferred).thenReturn(false)
        Mockito.`when`(takingsEntity.date).thenReturn(LocalDate.now())
        Mockito.`when`(takingsEntity.totalFiscal).thenReturn(BigDecimal.valueOf(500L))
        Mockito.`when`(takingsEntity.amountCash).thenReturn(BigDecimal.valueOf(200L))
        Mockito.`when`(takingsEntity.amountCheque).thenReturn(BigDecimal.valueOf(300L))
        Mockito.`when`(takingsEntity.totalNonFiscal).thenReturn(BigDecimal.valueOf(500L))
        Mockito.`when`(takingsEntity.cashNonFiscal).thenReturn(BigDecimal.valueOf(300L))
        Mockito.`when`(takingsEntity.chequeNonFiscal).thenReturn(BigDecimal.valueOf(200))
        val cashBox = Mockito.mock(CashBox::class.java)
        val companyBranch = Mockito.mock(CompanyBranch::class.java)
        Mockito.`when`(cashBox.name).thenReturn("Kasa 1")
        Mockito.`when`(cashBox.companyBranch).thenReturn(companyBranch)
        Mockito.`when`(companyBranch.name).thenReturn("Hlavna 4")
        Mockito.`when`(takingsEntity.cashBox).thenReturn(cashBox)
        val moneyBoxEntity = Mockito.mock(CashMoneyBox::class.java)
        Mockito.`when`(moneyBoxEntity.currency).thenReturn("CZK")
        Mockito.`when`(cashMoneyBoxRepository.findById(moneyBox.id!!)).thenReturn(Optional.of(moneyBoxEntity))
        Mockito.`when`(repository.findById(takings.id!!)).thenReturn(Optional.of(takingsEntity))
        Mockito.`when`(messages.getMessage("takings.transfer.note", "Hlavna 4", "Kasa 1")).thenReturn("Trzba - Hlavna 4 - Kasa1")
        service!!.transfer(transferDto, alfaUserDetails)
        Mockito.verify(cashMoneyMovementService, Mockito.times(2))!!.create(capture(movementCaptor), eq(alfaUserDetails))
        Assertions.assertThat(movementCaptor.allValues).hasSize(2)
        Mockito.verify(takingsEntity).transferred = true
        val fiscalMovement = movementCaptor.allValues[0]
        Assertions.assertThat(fiscalMovement.date!!.toLocalDate()).isEqualTo(takingsEntity.date)
        Assertions.assertThat(fiscalMovement.taxableDate).isEqualTo(takingsEntity.date)
        Assertions.assertThat(fiscalMovement.amountCash).isEqualTo(takingsEntity.amountCash)
        Assertions.assertThat(fiscalMovement.amountCheque).isEqualTo(takingsEntity.amountCheque)
        Assertions.assertThat(fiscalMovement.totalVat).isEqualTo(takingsEntity.totalVatAmount)
        Assertions.assertThat(fiscalMovement.accountingType).isEqualTo(AccountingType.FISCAL)
        Assertions.assertThat(fiscalMovement.type).isEqualTo(MoneyMovementType.INCOME)
        Assertions.assertThat(fiscalMovement.note).isEqualTo("Trzba - Hlavna 4 - Kasa1")
        Assertions.assertThat(fiscalMovement.moneyBox).isEqualTo(moneyBox)
        val nonFiscalMovement = movementCaptor.allValues[1]
        Assertions.assertThat(nonFiscalMovement.date!!.toLocalDate()).isEqualTo(takingsEntity.date)
        Assertions.assertThat(nonFiscalMovement.taxableDate).isNull()
        Assertions.assertThat(nonFiscalMovement.amountCash).isEqualTo(takingsEntity.cashNonFiscal)
        Assertions.assertThat(nonFiscalMovement.amountCheque).isEqualTo(takingsEntity.chequeNonFiscal)
        Assertions.assertThat(nonFiscalMovement.totalVat).isNull()
        Assertions.assertThat(nonFiscalMovement.accountingType).isEqualTo(AccountingType.NON_FISCAL)
        Assertions.assertThat(nonFiscalMovement.type).isEqualTo(MoneyMovementType.INCOME)
        Assertions.assertThat(nonFiscalMovement.note).isEqualTo("Trzba - Hlavna 4 - Kasa1")
        Assertions.assertThat(nonFiscalMovement.moneyBox).isEqualTo(moneyBox)
    }
}