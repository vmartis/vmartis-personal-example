package cz.smartbrains.qesu.module.order.service

import cz.smartbrains.qesu.module.order.dto.OrderStatementDto
import cz.smartbrains.qesu.module.order.dto.OrderStatementFilter
import cz.smartbrains.qesu.security.AlfaUserDetails
import org.springframework.security.access.prepost.PreAuthorize

interface OrderStatementService {
    @PreAuthorize("hasAuthority('REGULAR_SALE')")
    fun find(id: Long): OrderStatementDto

    @PreAuthorize("hasAuthority('REGULAR_SALE')")
    fun findAll(filter: OrderStatementFilter): List<OrderStatementDto>

    @PreAuthorize("hasAnyAuthority('REGULAR_SALE')")
    fun create(orderStatement: OrderStatementDto, user: AlfaUserDetails): OrderStatementDto

    @PreAuthorize("hasAnyAuthority('REGULAR_SALE')")
    fun update(orderStatement: OrderStatementDto, user: AlfaUserDetails): OrderStatementDto

    @PreAuthorize("hasAuthority('REGULAR_SALE')")
    fun delete(id: Long)
}