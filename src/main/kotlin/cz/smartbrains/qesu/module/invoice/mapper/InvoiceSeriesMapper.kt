package cz.smartbrains.qesu.module.invoice.mapper

import cz.smartbrains.qesu.module.invoice.dto.InvoiceSeriesDto
import cz.smartbrains.qesu.module.invoice.entity.InvoiceSeries
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(componentModel = "spring")
interface InvoiceSeriesMapper {
    @Mappings(Mapping(target = "created", ignore = true), Mapping(target = "updated", ignore = true))
    fun dtoToDo(locationDto: InvoiceSeriesDto?): InvoiceSeries?
    fun doToDto(location: InvoiceSeries?): InvoiceSeriesDto?
}