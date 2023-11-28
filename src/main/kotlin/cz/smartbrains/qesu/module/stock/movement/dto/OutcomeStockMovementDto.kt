package cz.smartbrains.qesu.module.stock.movement.dto

import com.fasterxml.jackson.annotation.JsonProperty
import cz.smartbrains.qesu.module.stock.movement.type.OutcomeStockMovementType
import lombok.EqualsAndHashCode
import lombok.ToString
import javax.validation.constraints.NotNull

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, exclude = ["incomeMovement"])
class OutcomeStockMovementDto : StockMovementDto() {
    var type: @NotNull OutcomeStockMovementType? = null

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var incomeMovement: IncomeStockMovementDto? = null
}