package cz.smartbrains.qesu.module.moneyBox.dto

import cz.smartbrains.qesu.module.common.dto.ColorableCodeListDto
import cz.smartbrains.qesu.module.moneyBox.type.MoneyMovementType
import lombok.ToString
import javax.validation.constraints.NotNull

@ToString(callSuper = true)
class MoneyMovementCategoryDto : ColorableCodeListDto() {
    var type: @NotNull MoneyMovementType? = null
}