package cz.smartbrains.qesu.module.item.mapper

import cz.smartbrains.qesu.module.item.category.mapper.ItemCategoryMapper
import cz.smartbrains.qesu.module.item.dto.ItemDto
import cz.smartbrains.qesu.module.item.entity.Item
import cz.smartbrains.qesu.module.unit.mapper.UnitMapper
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(componentModel = "spring", uses = [UnitMapper::class, ItemCategoryMapper::class])
interface ItemMapper {
    fun doToDto(item: Item?): ItemDto?

    @Mappings(Mapping(target = "created", ignore = true), Mapping(target = "updated", ignore = true))
    fun dtoToDo(item: ItemDto?): Item?
}