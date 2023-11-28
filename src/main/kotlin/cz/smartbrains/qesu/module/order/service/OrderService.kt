package cz.smartbrains.qesu.module.order.service

import cz.smartbrains.qesu.module.order.dto.OrderDto
import cz.smartbrains.qesu.module.order.dto.OrderFilter
import cz.smartbrains.qesu.security.AlfaUserDetails
import org.springframework.security.access.prepost.PreAuthorize

interface OrderService {
    @PreAuthorize("hasAuthority('REGULAR_SALE')")
    fun find(id: Long): OrderDto

    @PreAuthorize("hasAuthority('REGULAR_SALE')")
    fun findAll(filter: OrderFilter): List<OrderDto>

    @PreAuthorize("hasAuthority('REGULAR_SALE')")
    fun create(order: OrderDto, user: AlfaUserDetails): OrderDto

    @PreAuthorize("hasAuthority('REGULAR_SALE')")
    fun update(order: OrderDto, user: AlfaUserDetails): OrderDto

    @PreAuthorize("hasAuthority('REGULAR_SALE')")
    fun delete(id: Long)

    @PreAuthorize("hasAuthority('REGULAR_SALE')")
    fun updateBulk(orders: Set<OrderDto>, user: AlfaUserDetails)
}