package cz.smartbrains.qesu.module.order.service

import org.mockito.kotlin.any
import cz.smartbrains.qesu.module.common.exception.ServiceRuntimeException
import cz.smartbrains.qesu.module.order.repository.OrderRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigInteger
import java.util.*

@ExtendWith(MockitoExtension::class)
class OrderNumberFactoryImplTest {
    @Mock
    private val repository: OrderRepository? = null
    private var numberFactory: OrderNumberFactory? = null

    @BeforeEach
    fun setUp() {
        numberFactory = OrderNumberFactoryImpl(repository!!)
    }

    @Test
    fun nextNumber_notFirstRecord_validNumberGenerated() {
        Mockito.`when`(repository!!.findLastByNumber(any(), any()))
                .thenReturn(Optional.of(BigInteger.valueOf(20000012)))
        Assertions.assertThat(numberFactory!!.nextNumber(2020)).isEqualTo(BigInteger.valueOf(20000013))
    }

    @Test
    fun nextNumber_firstRecord_validNumberGenerated() {
        Mockito.`when`(repository!!.findLastByNumber(any(), any()))
                .thenReturn(Optional.empty())
        Assertions.assertThat(numberFactory!!.nextNumber(2020)).isEqualTo(BigInteger.valueOf(20000001))
    }

    @Test
    fun nextNumber_maxValueExceed_exIsThrown() {
        Mockito.`when`(repository!!.findLastByNumber(any(), any()))
                .thenReturn(Optional.of(BigInteger.valueOf(20000000 + OrderNumberFactoryImpl.MAX_INDEX.toLong()).add(BigInteger.ONE)))
        Assertions.assertThatThrownBy {
            numberFactory!!.nextNumber(2020)
        }.isInstanceOf(ServiceRuntimeException::class.java)
    }
}