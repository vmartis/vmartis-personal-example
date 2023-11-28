package cz.smartbrains.qesu.module.moneyBox.mapper

import cz.smartbrains.qesu.module.common.mapper.CodeListMapper
import cz.smartbrains.qesu.module.moneyBox.dto.MoneyMovementCategoryDto
import cz.smartbrains.qesu.module.moneyBox.entity.MoneyMovementCategory
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface MoneyMovementCategoryMapper : CodeListMapper<MoneyMovementCategory, MoneyMovementCategoryDto>