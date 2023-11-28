package cz.smartbrains.qesu.module.inventory.mapper

import cz.smartbrains.qesu.module.inventory.dto.InventoryItemDto
import cz.smartbrains.qesu.module.inventory.entity.InventoryItem
import cz.smartbrains.qesu.module.item.mapper.ItemMapper
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(uses = [ItemMapper::class], componentModel = "spring")
interface InventoryItemMapper {
    @Mappings(Mapping(target = "created", ignore = true), Mapping(target = "updated", ignore = true), Mapping(target = "inventory", ignore = true))
    fun dtoToDo(InventoryItem: InventoryItemDto?): InventoryItem?
    fun doToDto(InventoryItem: InventoryItem?): InventoryItemDto?
}