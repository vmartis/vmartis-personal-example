package cz.smartbrains.qesu.module.stock.movement.dto

import com.fasterxml.jackson.annotation.JsonProperty
import cz.smartbrains.qesu.module.common.dto.AbstractDto
import cz.smartbrains.qesu.module.item.dto.ItemDto
import lombok.EqualsAndHashCode
import lombok.ToString
import java.math.BigDecimal
import java.util.*
import javax.validation.constraints.Digits
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

@ToString(callSuper = true)
class StockMovementItemDto : AbstractDto {
    constructor() : super()

    constructor(item: @NotNull ItemDto?, amount: @NotNull @Digits(fraction = 3, integer = 7) @Min(value = 0.toLong()) BigDecimal?, price: @Digits(fraction = 2, integer = 8) @Min(value = 0.toLong()) @NotNull BigDecimal?, totalPrice: BigDecimal?, itemBatches: MutableList<String>) : super() {
        this.item = item
        this.amount = amount
        this.price = price
        this.totalPrice = totalPrice
        this.itemBatches = itemBatches
    }


    var item: @NotNull ItemDto? = null
    var amount: @NotNull @Digits(integer = 7, fraction = 3) @Min(0) BigDecimal? = null
    var price: @Digits(integer = 8, fraction = 2) @Min(0) @NotNull BigDecimal? = null

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var totalPrice: BigDecimal? = null
    var itemBatches: MutableList<String> = ArrayList()

    override fun toString(): String {
        return "StockMovementItemDto(item=$item, amount=$amount, price=$price, totalPrice=$totalPrice, itemBatches=$itemBatches)"
    }
}