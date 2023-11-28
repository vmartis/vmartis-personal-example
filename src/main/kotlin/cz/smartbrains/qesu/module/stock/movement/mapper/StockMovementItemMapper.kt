package cz.smartbrains.qesu.module.stock.movement.mapper

import cz.smartbrains.qesu.module.item.mapper.ItemMapper
import cz.smartbrains.qesu.module.stock.movement.dto.StockMovementItemDto
import cz.smartbrains.qesu.module.stock.movement.entity.StockMovementItem
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(componentModel = "spring", uses = [ItemMapper::class])
interface StockMovementItemMapper {
    @Mappings(Mapping(target = "amount", expression = "java(stockMovementItem.getAmount().abs())"))
    fun doToDto(stockMovementItem: StockMovementItem?): StockMovementItemDto?
}