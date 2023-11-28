package cz.smartbrains.qesu.module.inventory.dto

import cz.smartbrains.qesu.module.common.dto.AbstractDto
import cz.smartbrains.qesu.module.item.dto.ItemDto
import lombok.EqualsAndHashCode
import lombok.ToString
import java.math.BigDecimal
import java.util.*
import javax.validation.constraints.Digits
import javax.validation.constraints.NotNull

@ToString(callSuper = true, exclude = ["item"])
@EqualsAndHashCode(callSuper = true)
class InventoryItemDto : AbstractDto() {
    var amount: @Digits(integer = 10, fraction = 3) @NotNull BigDecimal? = null
    var item: @NotNull ItemDto? = null
    var itemBatches: MutableList<String> = ArrayList()
}