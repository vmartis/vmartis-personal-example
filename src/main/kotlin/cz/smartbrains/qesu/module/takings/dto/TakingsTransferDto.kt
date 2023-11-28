package cz.smartbrains.qesu.module.takings.dto

import cz.smartbrains.qesu.module.moneyBox.dto.CashMoneyBoxDto
import cz.smartbrains.qesu.module.moneyBox.dto.MoneyMovementCategoryDto
import lombok.ToString
import javax.validation.constraints.NotNull

@ToString
class TakingsTransferDto {
    var takings: @NotNull TakingsDto? = null
    var moneyBox: @NotNull CashMoneyBoxDto? = null
    var category: MoneyMovementCategoryDto? = null
}