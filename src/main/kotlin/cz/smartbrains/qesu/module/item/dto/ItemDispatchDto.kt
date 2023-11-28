package cz.smartbrains.qesu.module.item.dto

import cz.smartbrains.qesu.module.common.dto.AbstractDto
import lombok.ToString
import java.math.BigDecimal
import javax.validation.constraints.Digits
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

@ToString(callSuper = true)
class ItemDispatchDto : AbstractDto() {
    var item: @NotNull ItemDto? = null
    var subItem: @NotNull ItemDto? = null
    var amount: @Digits(integer = 10, fraction = 3) @NotNull @Positive BigDecimal? = null
}