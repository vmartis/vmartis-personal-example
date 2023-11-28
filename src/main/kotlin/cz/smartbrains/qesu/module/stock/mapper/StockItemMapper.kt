package cz.smartbrains.qesu.module.stock.mapper

import cz.smartbrains.qesu.module.item.mapper.ItemMapper
import cz.smartbrains.qesu.module.stock.dto.StockItemDto
import cz.smartbrains.qesu.module.stock.entity.StockItem
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(uses = [StockMapper::class, ItemMapper::class], componentModel = "spring")
interface StockItemMapper {
    @Mappings(Mapping(target = "created", ignore = true), Mapping(target = "updated", ignore = true))
    fun dtoToDo(stockItem: StockItemDto?): StockItem?
    fun doToDto(stockItem: StockItem?): StockItemDto?
}