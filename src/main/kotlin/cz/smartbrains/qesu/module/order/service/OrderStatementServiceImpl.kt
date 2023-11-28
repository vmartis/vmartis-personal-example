package cz.smartbrains.qesu.module.order.service

import cz.smartbrains.qesu.module.user.repository.UserRepository
import cz.smartbrains.qesu.module.common.exception.RecordNotFoundException
import cz.smartbrains.qesu.module.common.exception.ServiceRuntimeException
import cz.smartbrains.qesu.module.order.dto.OrderStatementDto
import cz.smartbrains.qesu.module.order.dto.OrderStatementFilter
import cz.smartbrains.qesu.module.order.entity.Order
import cz.smartbrains.qesu.module.order.entity.OrderStatement
import cz.smartbrains.qesu.module.order.mapper.OrderStatementMapper
import cz.smartbrains.qesu.module.order.repository.OrderRepository
import cz.smartbrains.qesu.module.order.repository.OrderStatementRepository
import cz.smartbrains.qesu.module.order.type.OrderState
import cz.smartbrains.qesu.security.AlfaUserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.function.Consumer
import java.util.function.Predicate
import java.util.stream.Collectors

@Transactional
@Service
class OrderStatementServiceImpl(private val repository: OrderStatementRepository,
                                private val orderRepository: OrderRepository,
                                private val userRepository: UserRepository,
                                private val mapper: OrderStatementMapper,
                                private val numberFactory: OrderStatementNumberFactory) : OrderStatementService {
    @Transactional(readOnly = true)
    override fun find(id: Long): OrderStatementDto {
        return mapper.doToDto(repository.findById(id).orElseThrow { RecordNotFoundException() })!!
    }

    @Transactional(readOnly = true)
    override fun findAll(filter: OrderStatementFilter): List<OrderStatementDto> {
        return repository.findByFilter(filter)
                .stream()
                .map { entity: OrderStatement? -> mapper.doToDto(entity)!! }
                .collect(Collectors.toList())
    }

    override fun create(orderStatement: OrderStatementDto, user: AlfaUserDetails): OrderStatementDto {
        val orderStatementEntity = mapper.dtoToDo(orderStatement)
        checkStatementEnabled(orderStatementEntity)
        checkUniqueCurrency(orderStatementEntity)
        checkOrderState(orderStatementEntity)
        orderStatementEntity!!.number = numberFactory.nextNumber(orderStatementEntity.date!!.year)
        orderStatementEntity.createdBy = userRepository.getById(user.id)
        val orders = orderStatementEntity.orders.stream().map { order: Order -> orderRepository.getById(order.id!!) }.collect(Collectors.toList())
        orderStatementEntity.orders = orders
        val savedEntity = repository.save(orderStatementEntity)
        orders.forEach(Consumer { order: Order -> order.statement = savedEntity })
        return mapper.doToDto(savedEntity)!!
    }

    override fun update(orderStatement: OrderStatementDto, user: AlfaUserDetails): OrderStatementDto {
        val newOrderStatementEntity = mapper.dtoToDo(orderStatement)
        checkUniqueCurrency(newOrderStatementEntity)
        checkOrderState(newOrderStatementEntity)
        val origOrderStatement = repository.findById(newOrderStatementEntity!!.id!!).orElseThrow { RecordNotFoundException() }
        origOrderStatement.orders.forEach(Consumer { order: Order ->
            val orderE = orderRepository.getById(order.id!!)
            orderE.statement = null
        })
        origOrderStatement.orders.clear()
        newOrderStatementEntity.orders.forEach(Consumer { order: Order ->
            val orderE = orderRepository.getById(order.id!!)
            orderE.statement = origOrderStatement
            origOrderStatement.orders.add(orderE)
        })
        if (origOrderStatement.date!!.year != newOrderStatementEntity.date!!.year) {
            origOrderStatement.number = numberFactory.nextNumber(newOrderStatementEntity.date!!.year)
        }
        origOrderStatement.note = newOrderStatementEntity.note
        origOrderStatement.date = newOrderStatementEntity.date
        val persistedOrderStatement = repository.save(origOrderStatement)
        return mapper.doToDto(persistedOrderStatement)!!
    }

    override fun delete(id: Long) {
        val origOrderStatement = repository.findById(id).orElseThrow { RecordNotFoundException() }
        if (origOrderStatement.invoices.isNotEmpty()) {
            throw ServiceRuntimeException("order-statement.delete.invoice.exists")
        }
        origOrderStatement.orders.forEach(Consumer { order ->
            order.statement = null
            orderRepository.saveAndFlush(order)
        })
        repository.deleteById(id)
    }

    fun checkUniqueCurrency(statement: OrderStatement?) {
        val count = statement!!.orders.stream()
                .map { order: Order -> orderRepository.getById(order.id!!) }
                .map(Order::currency).distinct().count()
        if (count > 1) {
            throw ServiceRuntimeException("order-statement.currency.multiple")
        }
    }

    fun checkOrderState(statement: OrderStatement?) {
        val allDelivered = statement!!.orders.stream()
                .map { order: Order -> orderRepository.getById(order.id!!) }
                .map(Order::state)
                .distinct()
                .allMatch(Predicate { orderState: OrderState? -> orderState === OrderState.DELIVERED })
        if (!allDelivered) {
            throw ServiceRuntimeException("order-statement.not.delivered.order")
        }
    }

    private fun checkStatementEnabled(orderStatementEntity: OrderStatement?) {
        val subscriber = orderRepository.findById(orderStatementEntity!!.orders[0].id!!).orElseThrow { RecordNotFoundException() }.subscriber
        if (subscriber!!.saleSettings == null || subscriber.saleSettings!!.paymentType == null || !subscriber.saleSettings!!.paymentType!!.statementEnabled) {
            throw ServiceRuntimeException("order-statement.not.enabled.for.subscriber")
        }
    }
}