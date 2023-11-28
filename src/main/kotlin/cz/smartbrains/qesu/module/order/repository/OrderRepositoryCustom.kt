package cz.smartbrains.qesu.module.order.repository

import cz.smartbrains.qesu.module.order.dto.OrderFilter
import cz.smartbrains.qesu.module.order.entity.Order

interface OrderRepositoryCustom {
    fun findByFilter(filter: OrderFilter): List<Order>
}