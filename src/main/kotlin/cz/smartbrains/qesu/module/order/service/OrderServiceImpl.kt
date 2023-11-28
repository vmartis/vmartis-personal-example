package cz.smartbrains.qesu.module.order.service

import cz.smartbrains.qesu.module.user.repository.UserRepository
import cz.smartbrains.qesu.module.common.exception.RecordNotFoundException
import cz.smartbrains.qesu.module.common.exception.ServiceRuntimeException
import cz.smartbrains.qesu.module.order.dto.OrderDto
import cz.smartbrains.qesu.module.order.dto.OrderFilter
import cz.smartbrains.qesu.module.order.entity.Order
import cz.smartbrains.qesu.module.order.entity.OrderItem
import cz.smartbrains.qesu.module.order.mapper.OrderMapper
import cz.smartbrains.qesu.module.order.repository.OrderRepository
import cz.smartbrains.qesu.module.order.type.OrderState
import cz.smartbrains.qesu.security.AlfaUserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.function.Consumer
import java.util.stream.Collectors

@Transactional
@Service
class OrderServiceImpl(private val repository: OrderRepository,
                       private val userRepository: UserRepository,
                       private val mapper: OrderMapper,
                       private val orderNumberFactory: OrderNumberFactory) : OrderService {
    @Transactional(readOnly = true)
    override fun find(id: Long): OrderDto {
        return mapper.doToDto(repository.findById(id).orElseThrow { RecordNotFoundException() })!!
    }

    @Transactional(readOnly = true)
    override fun findAll(filter: OrderFilter): List<OrderDto> {
        return repository.findByFilter(filter)
                .stream()
                .map { entity: Order? -> mapper.doToDto(entity)!! }
                .collect(Collectors.toList())
    }

    override fun create(order: OrderDto, user: AlfaUserDetails): OrderDto {
        val orderEntity = mapper.dtoToDo(order)
        orderEntity!!.state = OrderState.NEW
        orderEntity.items.forEach(Consumer { orderItem: OrderItem ->
            orderItem.order = orderEntity
            orderItem.returned = BigDecimal.ZERO
        })
        orderEntity.createdBy = userRepository.getById(user.id)
        orderEntity.number = orderNumberFactory.nextNumber(orderEntity.date!!.year)
        val savedEntity = repository.save(orderEntity)
        return mapper.doToDto(savedEntity)!!
    }

    override fun update(order: OrderDto, user: AlfaUserDetails): OrderDto {
        val newOrderEntity = mapper.dtoToDo(order)
        val origOrder = repository.findById(order.id!!).orElseThrow { RecordNotFoundException() }
        origOrder.note = newOrderEntity!!.note
        origOrder.subscriberOrderNumber = newOrderEntity.subscriberOrderNumber
        origOrder.updatedBy = userRepository.getById(user.id)

        // update all properties only if order is not billed
        if (!origOrder.billed) {
            newOrderEntity.items.forEach(Consumer { orderItem: OrderItem ->
                orderItem.order = origOrder
                if (orderItem.id == null) {
                    orderItem.order = origOrder
                    orderItem.returned = BigDecimal.ZERO
                }
            })
            origOrder.items.clear()
            origOrder.items.addAll(newOrderEntity.items)
            origOrder.totalPrice = newOrderEntity.totalPrice
            origOrder.totalVat = newOrderEntity.totalVat
            origOrder.totalDeliveredPrice = newOrderEntity.totalDeliveredPrice
            origOrder.totalDeliveredVat = newOrderEntity.totalDeliveredVat
            origOrder.state = newOrderEntity.state
            origOrder.currency = newOrderEntity.currency
        }
        val persistedOrder = repository.save(origOrder)
        return mapper.doToDto(persistedOrder)!!
    }

    override fun delete(id: Long) {
        val order = repository.getById(id)
        if (order.state !== OrderState.NEW) {
            throw ServiceRuntimeException("order.delete.invalid.state")
        }
        repository.deleteById(id)
    }

    override fun updateBulk(orders: Set<OrderDto>, user: AlfaUserDetails) {
        orders.forEach(Consumer { orderDto: OrderDto -> update(orderDto, user) })
    }
}