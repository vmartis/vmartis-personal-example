package cz.smartbrains.qesu.module.order.service

import cz.smartbrains.qesu.module.common.exception.ServiceRuntimeException
import cz.smartbrains.qesu.module.order.entity.Order
import cz.smartbrains.qesu.module.order.entity.OrderStatement
import cz.smartbrains.qesu.module.order.mapper.OrderStatementMapper
import cz.smartbrains.qesu.module.order.repository.OrderRepository
import cz.smartbrains.qesu.module.order.repository.OrderStatementRepository
import cz.smartbrains.qesu.module.order.type.OrderState
import cz.smartbrains.qesu.module.user.repository.UserRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class OrderStatementServiceImplTest {
    @Mock
    private val repository: OrderStatementRepository? = null

    @Mock
    private val orderRepository: OrderRepository? = null

    @Mock
    private val userRepository: UserRepository? = null

    @Mock
    private val mapper: OrderStatementMapper? = null

    @Mock
    private val numberFactory: OrderStatementNumberFactory? = null
    private var service: OrderStatementServiceImpl? = null

    @BeforeEach
    fun setUp() {
        service = OrderStatementServiceImpl(repository!!, orderRepository!!, userRepository!!, mapper!!, numberFactory!!)
    }

    @Test
    fun checkUniqueCurrency_oneCurrency_passOk() {
        val statementEntity = OrderStatement()
        val order1Entity = Order()
        order1Entity.id = 1L
        order1Entity.currency = "CZK"
        val order2Entity = Order()
        order2Entity.id = 2L
        order2Entity.currency = "CZK"
        statementEntity.orders.add(order1Entity)
        statementEntity.orders.add(order2Entity)
        Mockito.`when`(orderRepository!!.getById(1L)).thenReturn(order1Entity)
        Mockito.`when`(orderRepository.getById(2L)).thenReturn(order2Entity)
        service!!.checkUniqueCurrency(statementEntity)
    }

    @Test
    fun checkUniqueCurrency_multipleCurrency_exIsThrown() {
        val statementEntity = OrderStatement()
        val order1Entity = Order()
        order1Entity.id = 1L
        order1Entity.currency = "CZK"
        val order2Entity = Order()
        order2Entity.id = 2L
        order2Entity.currency = "EUR"
        statementEntity.orders.add(order1Entity)
        statementEntity.orders.add(order2Entity)
        Mockito.`when`(orderRepository!!.getById(1L)).thenReturn(order1Entity)
        Mockito.`when`(orderRepository.getById(2L)).thenReturn(order2Entity)
        Assertions.assertThatThrownBy {
            service!!.checkUniqueCurrency(statementEntity)
        }.isInstanceOf(ServiceRuntimeException::class.java)
    }

    @Test
    fun checkOrderState_onlyDelivered_passOk() {
        val statementEntity = OrderStatement()
        val order1Entity = Order()
        order1Entity.id = 1L
        order1Entity.state = OrderState.DELIVERED
        val order2Entity = Order()
        order2Entity.id = 2L
        order2Entity.state = OrderState.DELIVERED
        statementEntity.orders.add(order1Entity)
        statementEntity.orders.add(order2Entity)
        Mockito.`when`(orderRepository!!.getById(1L)).thenReturn(order1Entity)
        Mockito.`when`(orderRepository.getById(2L)).thenReturn(order2Entity)
        service!!.checkOrderState(statementEntity)
    }

    @Test
    fun checkOrderState_multipleCurrency_exIsThrown() {
        val statementEntity = OrderStatement()
        val order1Entity = Order()
        order1Entity.id = 1L
        order1Entity.state = OrderState.DELIVERED
        val order2Entity = Order()
        order2Entity.id = 2L
        order2Entity.state = OrderState.NEW
        statementEntity.orders.add(order1Entity)
        statementEntity.orders.add(order2Entity)
        Mockito.`when`(orderRepository!!.getById(1L)).thenReturn(order1Entity)
        Mockito.`when`(orderRepository.getById(2L)).thenReturn(order2Entity)
        Assertions.assertThatThrownBy {
            service!!.checkOrderState(statementEntity)
        }.isInstanceOf(ServiceRuntimeException::class.java)
    }
}