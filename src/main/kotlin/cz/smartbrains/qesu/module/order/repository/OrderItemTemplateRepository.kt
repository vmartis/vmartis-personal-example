package cz.smartbrains.qesu.module.order.repository

import cz.smartbrains.qesu.module.common.repository.OrderableEntityRepository
import cz.smartbrains.qesu.module.order.entity.OrderItemTemplate
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface OrderItemTemplateRepository : OrderableEntityRepository<OrderItemTemplate> {
    @Query("select oit from OrderItemTemplate oit left join fetch oit.company c left join fetch oit.item where c.id=:companyId order by oit.order asc")
    fun findAllByCompany(@Param("companyId") companyId: Long): List<OrderItemTemplate>
}