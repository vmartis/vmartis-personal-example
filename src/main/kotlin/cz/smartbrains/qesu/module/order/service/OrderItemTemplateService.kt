package cz.smartbrains.qesu.module.order.service

import cz.smartbrains.qesu.module.common.service.OrdeableEntityService
import cz.smartbrains.qesu.module.order.dto.OrderItemTemplateDto
import org.springframework.security.access.prepost.PreAuthorize

interface OrderItemTemplateService : OrdeableEntityService<OrderItemTemplateDto> {
    @PreAuthorize("hasAnyAuthority('COMPANY_PARTNER', 'REGULAR_SALE')")
    fun findAllByCompany(companyId: Long): List<OrderItemTemplateDto>

    @PreAuthorize("hasAuthority('COMPANY_PARTNER')")
    fun create(orderItemTemplateDto: OrderItemTemplateDto): OrderItemTemplateDto

    @PreAuthorize("hasAuthority('COMPANY_PARTNER')")
    fun update(orderItemTemplateDto: OrderItemTemplateDto): OrderItemTemplateDto

    @PreAuthorize("hasAuthority('COMPANY_PARTNER')")
    fun delete(id: Long)
}