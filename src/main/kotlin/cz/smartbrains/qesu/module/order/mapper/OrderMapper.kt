package cz.smartbrains.qesu.module.order.mapper

import cz.smartbrains.qesu.module.common.mapper.AddressMapper
import cz.smartbrains.qesu.module.common.mapper.AuditableMapper
import cz.smartbrains.qesu.module.company.mapper.CompanyBranchEntityMapper
import cz.smartbrains.qesu.module.company.mapper.CompanyEntityMapper
import cz.smartbrains.qesu.module.configuration.service.ConfigurationService
import cz.smartbrains.qesu.module.finance.service.FinanceCalculator
import cz.smartbrains.qesu.module.item.mapper.ItemEntityMapper
import cz.smartbrains.qesu.module.order.dto.OrderDto
import cz.smartbrains.qesu.module.order.dto.OrderItemDto
import cz.smartbrains.qesu.module.order.dto.OrderStatementDto
import cz.smartbrains.qesu.module.order.entity.Order
import cz.smartbrains.qesu.module.order.entity.OrderItem
import cz.smartbrains.qesu.module.order.entity.OrderStatement
import cz.smartbrains.qesu.module.paymenttype.mapper.PaymentTypeMapper
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal

@Mapper(uses = [ItemEntityMapper::class, PaymentTypeMapper::class, AuditableMapper::class, CompanyEntityMapper::class, CompanyBranchEntityMapper::class],
    componentModel = "spring")
abstract class OrderMapper {
    @Autowired
    protected var addressMapper: AddressMapper? = null
    @Autowired
    protected var paymentTypeMapper: PaymentTypeMapper? = null
    @Autowired
    protected var configurationService: ConfigurationService? = null
    @Autowired
    protected var financeCalculator: FinanceCalculator? = null

    @Mappings(Mapping(target = "totalPrice", expression = "java(calcTotalPrice(order))"),
        Mapping(target = "totalVat", expression = "java(calcTotalVat(order))"),
        Mapping(target = "totalDeliveredPrice", expression = "java(calcTotalDeliveredPrice(order))"),
        Mapping(target = "totalDeliveredVat", expression = "java(calcTotalDeliveredVat(order))"),
        Mapping(target = "created", ignore = true),
        Mapping(target = "createdBy", ignore = true),
        Mapping(target = "updated", ignore = true),
        Mapping(target = "updatedBy", ignore = true))
    abstract fun dtoToDo(order: OrderDto?): Order?
    abstract fun doToDto(entity: Order?): OrderDto?
    protected abstract fun itemDoToDto(orderItem: OrderItem?): OrderItemDto?

    @Mappings(Mapping(target = "created", ignore = true),
        Mapping(target = "updated", ignore = true),
        Mapping(target = "order", ignore = true))
    protected abstract fun itemDtoToDo(orderItem: OrderItemDto?): OrderItem?
    protected fun calcTotalVat(order: OrderDto?): BigDecimal? {
        if (order == null) {
            return null
        }
        var totalVat = BigDecimal.ZERO
        val vatCalculationType = configurationService!!.find().invoice!!.vatCalculationType!!
        for (item in order.items!!) {
            totalVat = totalVat.add(financeCalculator!!.calculateVat(vatCalculationType, item.itemPrice!!, item.ordered, item.vatRate))
        }
        return totalVat
    }

    protected fun calcTotalPrice(order: OrderDto?): BigDecimal? {
        if (order == null) {
            return null
        }
        var totalPrice = BigDecimal.ZERO
        val vatCalculationType = configurationService!!.find().invoice!!.vatCalculationType!!
        for (item in order.items!!) {
            totalPrice = totalPrice.add(financeCalculator!!.calculateTotalWithVat(vatCalculationType, item.itemPrice!!, item.ordered, item.vatRate))
        }
        return totalPrice
    }

    protected fun calcTotalDeliveredVat(order: OrderDto?): BigDecimal? {
        if (order == null) {
            return null
        }
        var totalVat = BigDecimal.ZERO
        val vatCalculationType = configurationService!!.find().invoice!!.vatCalculationType!!
        for (item in order.items!!) {
            totalVat = totalVat.add(financeCalculator!!.calculateVat(vatCalculationType, item.itemPrice!!, item.delivered, item.vatRate))
        }
        return totalVat
    }

    protected fun calcTotalDeliveredPrice(order: OrderDto?): BigDecimal? {
        if (order == null) {
            return null
        }
        var totalDeliveredPrice = BigDecimal.ZERO
        val vatCalculationType = configurationService!!.find().invoice!!.vatCalculationType!!
        for (item in order.items!!) {
            totalDeliveredPrice = totalDeliveredPrice.add(financeCalculator!!.calculateTotalWithVat(vatCalculationType, item.itemPrice!!, item.delivered, item.vatRate))
        }
        return totalDeliveredPrice
    }

    protected fun orderStatementDtoToDo(orderStatementDto: OrderStatementDto?): OrderStatement? {
        if (orderStatementDto == null) {
            return null
        }
        val orderStatement = OrderStatement()
        orderStatement.id = orderStatementDto.id
        return orderStatement
    }

    protected fun orderStatementDoToDto(orderStatementEntity: OrderStatement?): OrderStatementDto? {
        if (orderStatementEntity == null) {
            return null
        }
        val orderStatementDto = OrderStatementDto()
        orderStatementDto.id = orderStatementEntity.id
        return orderStatementDto
    }

    companion object {
        val HUNDRED = BigDecimal.valueOf(100)
    }
}