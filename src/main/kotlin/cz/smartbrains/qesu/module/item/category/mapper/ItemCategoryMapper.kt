package cz.smartbrains.qesu.module.item.category.mapper

import cz.smartbrains.qesu.module.common.dto.ColorableCodeListDto
import cz.smartbrains.qesu.module.common.mapper.CodeListMapper
import cz.smartbrains.qesu.module.item.category.entity.ItemCategory
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface ItemCategoryMapper : CodeListMapper<ItemCategory, ColorableCodeListDto>