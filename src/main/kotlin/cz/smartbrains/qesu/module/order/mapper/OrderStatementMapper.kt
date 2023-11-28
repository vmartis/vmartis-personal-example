package cz.smartbrains.qesu.module.order.mapper

import cz.smartbrains.qesu.module.invoice.mapper.InvoiceMapper
import cz.smartbrains.qesu.module.common.mapper.AuditableMapper
import cz.smartbrains.qesu.module.order.dto.OrderStatementDto
import cz.smartbrains.qesu.module.order.entity.OrderStatement
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(uses = [OrderMapper::class, InvoiceMapper::class, AuditableMapper::class], componentModel = "spring")
abstract class OrderStatementMapper {
    @Mappings(Mapping(target = "created", ignore = true),
            Mapping(target = "updated", ignore = true),
            Mapping(target = "createdBy", ignore = true),
            Mapping(target = "updatedBy", ignore = true))
    abstract fun dtoToDo(dto: OrderStatementDto?): OrderStatement?
    abstract fun doToDto(entity: OrderStatement?): OrderStatementDto?
}