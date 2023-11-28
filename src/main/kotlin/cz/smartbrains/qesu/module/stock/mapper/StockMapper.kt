package cz.smartbrains.qesu.module.stock.mapper

import cz.smartbrains.qesu.module.company.mapper.CompanyBranchMapper
import cz.smartbrains.qesu.module.stock.dto.StockDto
import cz.smartbrains.qesu.module.stock.entity.Stock
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(uses = [CompanyBranchMapper::class], componentModel = "spring")
interface StockMapper {
    @Mappings(Mapping(target = "created", ignore = true), Mapping(target = "updated", ignore = true))
    fun dtoToDo(stock: StockDto?): Stock?
    fun doToDto(stock: Stock?): StockDto?
}