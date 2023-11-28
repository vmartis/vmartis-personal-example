package cz.smartbrains.qesu.module.moneyBox.mapper

import cz.smartbrains.qesu.module.invoice.mapper.InvoiceMapper
import cz.smartbrains.qesu.module.moneyBox.dto.CashMoneyMovementDto
import cz.smartbrains.qesu.module.moneyBox.dto.MoneyMovementDto
import cz.smartbrains.qesu.module.moneyBox.entity.CashMoneyMovement
import cz.smartbrains.qesu.module.moneyBox.entity.MoneyMovement
import cz.smartbrains.qesu.module.moneyBox.type.MoneyMovementType
import cz.smartbrains.qesu.module.subject.mapper.SubjectBaseMapper
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal

@ExtendWith(MockitoExtension::class)
class CashMoneyMovementMapperImplTest {
    @Mock
    private val moneyBoxMapper: MoneyBoxMapper? = null

    @Mock
    private val invoiceMapper: InvoiceMapper? = null

    @Mock
    private val subjectBaseMapper: SubjectBaseMapper? = null

    @Mock
    private val moneyMovementCategoryMapper: MoneyMovementCategoryMapper? = null

    @InjectMocks
    private val mapper: CashMoneyMovementMapper = CashMoneyMovementMapperImpl()
    @Test
    fun dtoToDo_outcomeType_amountsConvertedToNegative() {
        val moneyMovement = CashMoneyMovementDto()
        moneyMovement.amountCash = BigDecimal.valueOf(12000)
        moneyMovement.amountCheque = BigDecimal.valueOf(100)
        moneyMovement.totalVat = BigDecimal.valueOf(2100)
        moneyMovement.totalWithoutVat = BigDecimal.valueOf(10000)
        moneyMovement.type = MoneyMovementType.OUTCOME
        val entity = mapper.dtoToDo(moneyMovement)
        Assertions.assertThat(entity!!.amountCash).isEqualTo(BigDecimal.valueOf(-12000))
        Assertions.assertThat(entity.amountCheque).isEqualTo(BigDecimal.valueOf(-100))
        Assertions.assertThat(entity.totalAmount).isEqualTo(BigDecimal.valueOf(-12100))
        Assertions.assertThat(entity.totalVat).isEqualTo(BigDecimal.valueOf(-2100))
        Assertions.assertThat(entity.totalWithoutVat).isEqualTo(BigDecimal.valueOf(-10000))
    }

    @Test
    fun doToDto_nullVatAmount_convertedAsNull() {
        val moneyMovement = CashMoneyMovement()
        moneyMovement.amountCash = BigDecimal.valueOf(12100)
        moneyMovement.totalWithoutVat = BigDecimal.valueOf(10000)
        val dto: MoneyMovementDto? = mapper.doToDto(moneyMovement)
        Assertions.assertThat(dto!!.totalVat).isNull()
    }

    @Test
    fun dtoToDo_outcomeType_convertedAmountAsNegative() {
        val moneyMovementDto = CashMoneyMovementDto()
        moneyMovementDto.type = MoneyMovementType.OUTCOME
        moneyMovementDto.amountCash = BigDecimal.valueOf(10000)
        moneyMovementDto.amountCheque = BigDecimal.valueOf(2100)
        moneyMovementDto.totalVat = BigDecimal.valueOf(2100)
        moneyMovementDto.totalWithoutVat = BigDecimal.valueOf(999999999)
        val entity = mapper.dtoToDo(moneyMovementDto)
        Assertions.assertThat(entity!!.totalAmount).isEqualTo(BigDecimal.valueOf(-12100))
        Assertions.assertThat(entity.amountCash).isEqualTo(BigDecimal.valueOf(-10000))
        Assertions.assertThat(entity.amountCheque).isEqualTo(BigDecimal.valueOf(-2100))
        Assertions.assertThat(entity.totalVat).isEqualTo(BigDecimal.valueOf(-2100))
        Assertions.assertThat(entity.totalWithoutVat).isEqualTo(BigDecimal.valueOf(-10000))
    }

    @Test
    fun dtoToDo_nullVatAmount_convertedAsNull() {
        val moneyMovementDto = CashMoneyMovementDto()
        moneyMovementDto.type = MoneyMovementType.OUTCOME
        moneyMovementDto.amountCash = BigDecimal.valueOf(12100)
        moneyMovementDto.totalWithoutVat = BigDecimal.valueOf(10000)
        val entity: MoneyMovement? = mapper.dtoToDo(moneyMovementDto)
        Assertions.assertThat(entity!!.totalVat).isNull()
    }

    @Test
    fun dtoToDo_cashAndCheque_sumIsOk() {
        val moneyMovement = CashMoneyMovementDto()
        moneyMovement.amountCash = BigDecimal.valueOf(1000)
        moneyMovement.amountCheque = BigDecimal.valueOf(1000)
        moneyMovement.type = MoneyMovementType.OUTCOME
        val entity: MoneyMovement? = mapper.dtoToDo(moneyMovement)
        Assertions.assertThat(entity!!.totalAmount).isEqualTo(BigDecimal.valueOf(-2000))
        Assertions.assertThat(entity.totalWithoutVat).isEqualTo(BigDecimal.valueOf(-2000))
    }

    @Test
    fun dtoToDo_onlyCheque_sumIsOk() {
        val moneyMovement = CashMoneyMovementDto()
        moneyMovement.amountCheque = BigDecimal.valueOf(1000)
        moneyMovement.type = MoneyMovementType.OUTCOME
        val entity: MoneyMovement? = mapper.dtoToDo(moneyMovement)
        Assertions.assertThat(entity!!.totalAmount).isEqualTo(BigDecimal.valueOf(-1000))
        Assertions.assertThat(entity.totalWithoutVat).isEqualTo(BigDecimal.valueOf(-1000))
    }

    @Test
    fun dtoToDo_nullCashAndChequeAmount_convertedAsZero() {
        val dto = CashMoneyMovementDto()
        val entity = mapper.dtoToDo(dto)
        Assertions.assertThat(entity!!.amountCash).isEqualTo(BigDecimal.ZERO)
        Assertions.assertThat(entity.amountCheque).isEqualTo(BigDecimal.ZERO)
    }
}