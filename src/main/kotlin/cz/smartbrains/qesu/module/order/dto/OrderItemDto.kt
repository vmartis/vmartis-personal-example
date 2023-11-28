package cz.smartbrains.qesu.module.order.dto

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import cz.smartbrains.qesu.module.common.dto.AbstractDto
import cz.smartbrains.qesu.module.common.json.EntitySerializer
import cz.smartbrains.qesu.module.item.dto.ItemDto
import lombok.ToString
import java.math.BigDecimal
import javax.validation.constraints.Digits
import javax.validation.constraints.Max
import javax.validation.constraints.NotNull

@ToString(callSuper = true, exclude = ["item"])
class OrderItemDto : AbstractDto() {
    @JsonSerialize(using = EntitySerializer::class)
    var item: @NotNull ItemDto? = null
    var ordered: @NotNull BigDecimal? = null
    var delivered = BigDecimal.ZERO
    var returned = BigDecimal.ZERO
    var itemPrice: @NotNull @Digits(integer = 8, fraction = 2) BigDecimal? = null
    var vatRate: @NotNull @Digits(integer = 3, fraction = 2) @Max(100) BigDecimal? = null
}