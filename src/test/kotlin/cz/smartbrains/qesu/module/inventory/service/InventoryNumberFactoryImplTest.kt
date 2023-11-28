package cz.smartbrains.qesu.module.inventory.service

import org.mockito.kotlin.any
import cz.smartbrains.qesu.module.common.exception.ServiceRuntimeException
import cz.smartbrains.qesu.module.inventory.repository.InventoryRepository
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
class InventoryNumberFactoryImplTest {
    @Mock
    private val repository: InventoryRepository? = null
    private var numberFactory: InventoryNumberFactory? = null

    @BeforeEach
    fun setUp() {
        numberFactory = InventoryNumberFactoryImpl(repository!!)
    }

    @Test
    fun nextNumber_notFirstRecord_validNumberGenerated() {
        Mockito.`when`(repository!!.findLastByNumber(ArgumentMatchers.eq(STOCK_ID), any(), any()))
            .thenReturn(Optional.of(BigInteger.valueOf(20000012)))
        Assertions.assertThat(numberFactory!!.nextNumber(STOCK_ID, 2020)).isEqualTo(BigInteger.valueOf(20000013))
    }

    @Test
    fun nextNumber_firstRecord_validNumberGenerated() {
        Mockito.`when`(repository!!.findLastByNumber(ArgumentMatchers.eq(STOCK_ID), any(), any()))
            .thenReturn(Optional.empty())
        Assertions.assertThat(numberFactory!!.nextNumber(STOCK_ID, 2020)).isEqualTo(BigInteger.valueOf(20000001))
    }

    @Test
    fun nextNumber_maxValueExceed_exIsThrown() {
        Mockito.`when`(repository!!.findLastByNumber(ArgumentMatchers.eq(STOCK_ID), any(), any()))
            .thenReturn(
                Optional.of(
                    BigInteger.valueOf(20000000 + InventoryNumberFactoryImpl.MAX_INDEX.toLong()).add(BigInteger.ONE)
                )
            )
        Assertions.assertThatThrownBy {
            numberFactory!!.nextNumber(STOCK_ID, 2020)
        }.isInstanceOf(ServiceRuntimeException::class.java)
    }

    companion object {
        const val STOCK_ID = 1L
    }
}