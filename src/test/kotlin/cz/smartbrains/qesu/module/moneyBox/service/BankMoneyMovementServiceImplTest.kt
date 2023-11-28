package cz.smartbrains.qesu.module.moneyBox.service

import org.mockito.kotlin.any
import cz.smartbrains.qesu.module.common.exception.ServiceRuntimeException
import cz.smartbrains.qesu.module.invoice.dto.InvoiceDto
import cz.smartbrains.qesu.module.invoice.entity.Invoice
import cz.smartbrains.qesu.module.invoice.repository.InvoiceRepository
import cz.smartbrains.qesu.module.moneyBox.dto.BankMoneyMovementDto
import cz.smartbrains.qesu.module.moneyBox.dto.BankMoneyMovementSplitDto
import cz.smartbrains.qesu.module.moneyBox.entity.BankMoneyBox
import cz.smartbrains.qesu.module.moneyBox.entity.BankMoneyMovement
import cz.smartbrains.qesu.module.moneyBox.mapper.BankMoneyMovementMapper
import cz.smartbrains.qesu.module.moneyBox.repository.BankMoneyMovementRepository
import cz.smartbrains.qesu.module.moneyBox.type.MoneyMovementType
import cz.smartbrains.qesu.module.subject.dto.SubjectDto
import cz.smartbrains.qesu.module.user.entity.User
import cz.smartbrains.qesu.module.user.repository.UserRepository
import cz.smartbrains.qesu.security.AlfaUserDetails
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.lenient
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@ExtendWith(MockitoExtension::class)
class BankMoneyMovementServiceImplTest {
    @Mock
    private val bankMoneyMovementRepository: BankMoneyMovementRepository? = null

    @Mock
    private val bankMoneyMovementMapper: BankMoneyMovementMapper? = null

    @Mock
    private val invoiceRepository: InvoiceRepository? = null

    @Mock
    private val invoicePairService: MoneyMovementInvoicePairService? = null

    @Mock
    private val subjectPairService: MoneyMovementSubjectPairService? = null

    @Mock
    private val userRepository: UserRepository? = null
    private val userEntity = User()

    private val user: AlfaUserDetails = AlfaUserDetails(10, "jdeer", "", true, emptyList())

    private var service: BankMoneyMovementServiceImpl? = null

    @BeforeEach
    fun setUp() {
        service = BankMoneyMovementServiceImpl(bankMoneyMovementRepository!!, invoiceRepository!!,
                userRepository!!, bankMoneyMovementMapper!!, invoicePairService!!, subjectPairService!!)
        lenient().`when`(userRepository.getById(user.id)).thenReturn(userEntity)
        lenient().`when`(bankMoneyMovementMapper.doToDto(any())).thenReturn(BankMoneyMovementDto())
    }

    @Test
    fun update_bankMoneyMovementWthSubject_subjectPairServiceCalled() {
        val movementId = 1L
        val subjectId = 5L

        //mock persisted entity
        val movementEntity = Mockito.mock(BankMoneyMovement::class.java)
        Mockito.`when`(bankMoneyMovementRepository!!.findById(movementId)).thenReturn(Optional.of(movementEntity))

        //mock input DTO
        val movementDto = Mockito.mock(BankMoneyMovementDto::class.java)
        val subjectDto = Mockito.mock(SubjectDto::class.java)
        Mockito.`when`(movementDto.id).thenReturn(movementId)
        Mockito.`when`(movementDto.subject).thenReturn(subjectDto)
        Mockito.`when`(subjectDto.id).thenReturn(subjectId)
        val movementTransformedEntity = Mockito.mock(BankMoneyMovement::class.java)
        Mockito.`when`(bankMoneyMovementMapper!!.dtoToDo(movementDto)).thenReturn(movementTransformedEntity)
        service!!.update(movementDto, user)
        Mockito.verify(subjectPairService)!!.pairSubject(movementId, subjectId)
    }

    @Test
    fun split_oneNewMovement_throwsEx() {
        val originalMovementId = 1L
        val originalMovementDto = BankMoneyMovementDto()
        originalMovementDto.id = originalMovementId
        val origMovementEntity = BankMoneyMovement()
        origMovementEntity.id = originalMovementId
        origMovementEntity.totalAmount = BigDecimal.valueOf(1000)
        val note = "Note of new movements"
        val newMovement1Dto = BankMoneyMovementDto()
        newMovement1Dto.totalAmount = BigDecimal.valueOf(200)
        val split = BankMoneyMovementSplitDto(originalMovementDto, arrayListOf(newMovement1Dto), note)
        Assertions.assertThatThrownBy {
            service!!.split(split, user)
        }.isInstanceOf(ServiceRuntimeException::class.java)
    }

    @Test
    fun split_invalidNewMovementSum_throwsEx() {
        val originalMovementId = 1L
        val originalMovementDto = BankMoneyMovementDto()
        originalMovementDto.id = originalMovementId
        val origMovementEntity = BankMoneyMovement()
        origMovementEntity.id = originalMovementId
        origMovementEntity.totalAmount = BigDecimal.valueOf(1000)
        val note = "Note of new movements"
        val newMovement1Dto = BankMoneyMovementDto()
        newMovement1Dto.totalAmount = BigDecimal.valueOf(200)
        val newMovement2Dto = BankMoneyMovementDto()
        newMovement2Dto.totalAmount = BigDecimal.valueOf(200)
        val split = BankMoneyMovementSplitDto(originalMovementDto, arrayListOf(newMovement1Dto, newMovement2Dto), note)
        Assertions.assertThatThrownBy {
            service!!.split(split, user)
        }.isInstanceOf(ServiceRuntimeException::class.java)
    }

    @Test
    fun split_correctSplitData_movementIsSplit() {
        val originalMovementId = 1L
        val originalMovementDto = BankMoneyMovementDto()
        originalMovementDto.id = originalMovementId
        val origMovementEntity = BankMoneyMovement()
        origMovementEntity.id = originalMovementId
        origMovementEntity.totalAmount = BigDecimal.valueOf(1000)
        val note = "Note of new movements"
        val newMovement1Dto = BankMoneyMovementDto()
        newMovement1Dto.totalAmount = BigDecimal.valueOf(300)
        val newMovement2Dto = BankMoneyMovementDto()
        newMovement2Dto.totalAmount = BigDecimal.valueOf(700)
        val split = BankMoneyMovementSplitDto(originalMovementDto, Arrays.asList(newMovement1Dto, newMovement2Dto), note)
        val newMovement1Entity = BankMoneyMovement()
        newMovement1Entity.totalAmount = BigDecimal.valueOf(300)
        val newMovement2Entity = BankMoneyMovement()
        newMovement2Entity.totalAmount = BigDecimal.valueOf(700)
        Mockito.`when`(bankMoneyMovementRepository!!.findById(originalMovementId)).thenReturn(Optional.of(origMovementEntity))
        Mockito.`when`(bankMoneyMovementMapper!!.dtoToDo(newMovement1Dto)).thenReturn(newMovement1Entity)
        Mockito.`when`(bankMoneyMovementMapper.dtoToDo(newMovement2Dto)).thenReturn(newMovement2Entity)
        service!!.split(split, user)
        Assertions.assertThat(origMovementEntity.active).isFalse
        Mockito.verify(bankMoneyMovementRepository).saveAll(listOf(newMovement1Entity, newMovement2Entity))
    }

    @Test
    fun split_correctSplitWithInvoice_taxableDateAndTotalVatIsSet() {
        val originalMovementId = 1L
        val originalMovementDto = BankMoneyMovementDto()
        originalMovementDto.id = originalMovementId
        originalMovementDto.type = MoneyMovementType.INCOME
        val moneyBox = BankMoneyBox()
        moneyBox.currency = "CZK"
        val origMovementEntity = BankMoneyMovement()
        origMovementEntity.id = originalMovementId
        origMovementEntity.totalAmount = BigDecimal.valueOf(1000)
        origMovementEntity.type = MoneyMovementType.INCOME
        origMovementEntity.moneyBox = moneyBox
        val invoiceId = 2L
        val taxableDate = LocalDate.now()
        val invoiceDto = InvoiceDto()
        invoiceDto.id = invoiceId
        val invoiceEntity = Invoice()
        invoiceEntity.id = invoiceId
        invoiceEntity.totalAmount = BigDecimal.valueOf(700)
        invoiceEntity.totalVat = BigDecimal.valueOf(200)
        invoiceEntity.taxableDate = taxableDate
        invoiceEntity.currency = "CZK"
        val note = "Note of new movements"
        val newMovement1Dto = BankMoneyMovementDto()
        newMovement1Dto.totalAmount = BigDecimal.valueOf(300)
        val newMovement2Dto = BankMoneyMovementDto()
        newMovement2Dto.totalAmount = BigDecimal.valueOf(700)
        newMovement2Dto.invoice = invoiceDto
        val split = BankMoneyMovementSplitDto(originalMovementDto, Arrays.asList(newMovement1Dto, newMovement2Dto), note)
        val newMovement1Entity = BankMoneyMovement()
        newMovement1Entity.totalAmount = BigDecimal.valueOf(300)
        val newMovement2Entity = BankMoneyMovement()
        newMovement2Entity.totalAmount = BigDecimal.valueOf(700)
        newMovement2Entity.invoice = invoiceEntity
        Mockito.`when`(bankMoneyMovementRepository!!.findById(originalMovementId)).thenReturn(Optional.of(origMovementEntity))
        Mockito.`when`(bankMoneyMovementMapper!!.dtoToDo(newMovement1Dto)).thenReturn(newMovement1Entity)
        Mockito.`when`(bankMoneyMovementMapper.dtoToDo(newMovement2Dto)).thenReturn(newMovement2Entity)
        Mockito.`when`(invoiceRepository!!.findById(invoiceId)).thenReturn(Optional.of(invoiceEntity))
        service!!.split(split, user)
        Assertions.assertThat(newMovement1Entity.taxableDate).isNull()
        Assertions.assertThat(newMovement1Entity.totalVat).isNull()
        Assertions.assertThat(newMovement2Entity.taxableDate).isEqualTo(taxableDate)
        Assertions.assertThat(newMovement2Entity.totalVat).isEqualTo(invoiceEntity.totalVat)
    }
}