package cz.smartbrains.qesu.module.stock.movement.repository

import org.mockito.kotlin.any
import cz.smartbrains.qesu.module.common.exception.ServiceRuntimeException
import cz.smartbrains.qesu.module.stock.movement.type.StockMovementType
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigInteger
import java.util.*

@ExtendWith(MockitoExtension::class)
class StockMovementNumberFactoryImplTest {
    @Mock
    private val repository: StockMovementRepository? = null
    private var numberFactory: StockMovementNumberFactory? = null

    @BeforeEach
    @Throws(Exception::class)
    fun setUp() {
        numberFactory = StockMovementNumberFactoryImpl(repository!!)
    }

    @Test
    fun nextNumber_notFirstRecord_validNumberGenerated() {
        Mockito.`when`(repository!!.findLastByNumber(ArgumentMatchers.eq(STOCK_ID), any(), any(), any()))
                .thenReturn(Optional.of(BigInteger.valueOf(20000012)))
        Assertions.assertThat(numberFactory!!.nextNumber(StockMovementType.INCOME, STOCK_ID, 2020)).isEqualTo(BigInteger.valueOf(20000013))
    }

    @Test
    fun nextNumber_firstRecord_validNumberGenerated() {
        Mockito.`when`(repository!!.findLastByNumber(ArgumentMatchers.eq(STOCK_ID), any(), any(), any()))
                .thenReturn(Optional.empty())
        Assertions.assertThat(numberFactory!!.nextNumber(StockMovementType.INCOME, STOCK_ID, 2020)).isEqualTo(BigInteger.valueOf(20000001))
    }

    @Test
    fun nextNumber_maxValueExceed_exIsThrown() {
        Mockito.`when`(repository!!.findLastByNumber(ArgumentMatchers.eq(STOCK_ID), any(), any(), any()))
                .thenReturn(Optional.of(BigInteger.valueOf(20000000 + StockMovementNumberFactoryImpl.MAX_INDEX.toLong()).add(BigInteger.ONE)))
        Assertions.assertThatThrownBy {
            numberFactory!!.nextNumber(StockMovementType.INCOME, STOCK_ID, 2020)
        }.isInstanceOf(ServiceRuntimeException::class.java)
    }

    companion object {
        const val STOCK_ID = 1L
    }
}