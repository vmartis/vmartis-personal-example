package cz.smartbrains.qesu.module.order.repository

import cz.smartbrains.qesu.module.order.dto.OrderStatementFilter
import cz.smartbrains.qesu.module.order.entity.OrderStatement

interface OrderStatementRepositoryCustom {
    fun findByFilter(filter: OrderStatementFilter): List<OrderStatement>
}