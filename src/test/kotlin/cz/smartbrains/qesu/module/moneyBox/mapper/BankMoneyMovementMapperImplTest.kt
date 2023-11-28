package cz.smartbrains.qesu.module.moneyBox.mapper

import cz.smartbrains.qesu.module.invoice.mapper.InvoiceMapper
import cz.smartbrains.qesu.module.moneyBox.dto.BankMoneyMovementDto
import cz.smartbrains.qesu.module.moneyBox.dto.MoneyMovementDto
import cz.smartbrains.qesu.module.moneyBox.entity.BankMoneyMovement
import cz.smartbrains.qesu.module.moneyBox.entity.MoneyMovement
import cz.smartbrains.qesu.module.moneyBox.type.MoneyMovementType
import cz.smartbrains.qesu.module.subject.mapper.SubjectBaseMapper
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal

@ExtendWith(MockitoExtension::class)
class BankMoneyMovementMapperImplTest {
    @Mock
    private val moneyBoxMapper: MoneyBoxMapper? = null

    @Mock
    private val invoiceMapper: InvoiceMapper? = null

    @Mock
    private val subjectBaseMapper: SubjectBaseMapper? = null

    @Mock
    private val moneyMovementCategoryMapper: MoneyMovementCategoryMapper? = null

    private val mapper: BankMoneyMovementMapper = BankMoneyMovementMapperImpl()

    @BeforeEach
    fun setUp() {
        mapper.invoiceMapper = invoiceMapper
        mapper.moneyBoxMapper = moneyBoxMapper
        mapper.subjectBaseMapper = subjectBaseMapper
        mapper.moneyMovementCategoryMapper = moneyMovementCategoryMapper
    }

    @Test
    fun doToDto_negativeAmounts_converted() {
        val moneyMovement = BankMoneyMovement()
        moneyMovement.totalAmount = BigDecimal.valueOf(-12100)
        moneyMovement.totalVat = BigDecimal.valueOf(-2100)
        moneyMovement.totalWithoutVat = BigDecimal.valueOf(-10000)
        val dto: MoneyMovementDto? = mapper.doToDto(moneyMovement)
        Assertions.assertThat(dto!!.totalAmount).isEqualTo(BigDecimal.valueOf(-12100))
        Assertions.assertThat(dto.totalVat).isEqualTo(BigDecimal.valueOf(-2100))
        Assertions.assertThat(dto.totalWithoutVat).isEqualTo(BigDecimal.valueOf(-10000))
    }

    @Test
    fun doToDto_nullVatAmount_convertedAsNull() {
        val moneyMovement = BankMoneyMovement()
        moneyMovement.totalAmount = BigDecimal.valueOf(12100)
        moneyMovement.totalWithoutVat = BigDecimal.valueOf(10000)
        val dto: MoneyMovementDto? = mapper.doToDto(moneyMovement)
        Assertions.assertThat(dto!!.totalVat).isNull()
    }

    @Test
    fun dtoToDo_outcomeType_convertedAmountAsNegative() {
        val moneyMovementDto = BankMoneyMovementDto()
        moneyMovementDto.type = MoneyMovementType.OUTCOME
        moneyMovementDto.totalAmount = BigDecimal.valueOf(12100)
        moneyMovementDto.totalVat = BigDecimal.valueOf(2100)
        moneyMovementDto.totalWithoutVat = BigDecimal.valueOf(999999999)
        val entity: MoneyMovement? = mapper.dtoToDo(moneyMovementDto)
        Assertions.assertThat(entity!!.totalAmount).isEqualTo(BigDecimal.valueOf(-12100))
        Assertions.assertThat(entity.totalVat).isEqualTo(BigDecimal.valueOf(-2100))
        Assertions.assertThat(entity.totalWithoutVat).isEqualTo(BigDecimal.valueOf(-10000))
    }

    @Test
    fun dtoToDo_nullVatAmount_convertedAsNull() {
        val moneyMovementDto = BankMoneyMovementDto()
        moneyMovementDto.type = MoneyMovementType.OUTCOME
        moneyMovementDto.totalAmount = BigDecimal.valueOf(12100)
        moneyMovementDto.totalWithoutVat = BigDecimal.valueOf(10000)
        val entity: MoneyMovement? = mapper.dtoToDo(moneyMovementDto)
        Assertions.assertThat(entity!!.totalVat).isNull()
    }
}