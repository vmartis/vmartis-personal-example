package cz.smartbrains.qesu.module.stock.dto

import com.fasterxml.jackson.annotation.JsonProperty
import cz.smartbrains.qesu.module.common.dto.AbstractDto
import cz.smartbrains.qesu.module.item.entity.Item
import lombok.ToString
import java.math.BigDecimal
import javax.validation.constraints.Digits
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

@ToString(callSuper = true, exclude = ["stock", "item"])
class StockItemDto : AbstractDto() {
    var stock: @NotNull StockDto? = null
    var item: @NotNull Item? = null

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var price: BigDecimal? = null

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var amount: BigDecimal? = null
    var minAmount: @NotNull @Digits(integer = 8, fraction = 3) @Min(0) BigDecimal? = null
    var active = false
}