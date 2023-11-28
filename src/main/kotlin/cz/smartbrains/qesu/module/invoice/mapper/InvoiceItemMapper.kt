package cz.smartbrains.qesu.module.invoice.mapper

import cz.smartbrains.qesu.module.invoice.dto.InvoiceItemDto
import cz.smartbrains.qesu.module.invoice.entity.InvoiceItem
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(componentModel = "spring")
abstract class InvoiceItemMapper {
    abstract fun doToDto(invoiceItem: InvoiceItem?): InvoiceItemDto?
    @Mappings(Mapping(target = "created", ignore = true), Mapping(target = "updated", ignore = true), Mapping(target = "invoice", ignore = true))
    abstract fun dtoToDo(invoiceItemDto: InvoiceItemDto?): InvoiceItem?
}