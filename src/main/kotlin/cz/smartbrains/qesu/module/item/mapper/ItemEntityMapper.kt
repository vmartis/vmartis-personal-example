package cz.smartbrains.qesu.module.item.mapper

import cz.smartbrains.qesu.module.common.mapper.EntityMapper
import cz.smartbrains.qesu.module.item.dto.ItemDto
import cz.smartbrains.qesu.module.item.entity.Item
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
abstract class ItemEntityMapper() : EntityMapper<Item, ItemDto>(Item::class.java, ItemDto::class.java) {
}