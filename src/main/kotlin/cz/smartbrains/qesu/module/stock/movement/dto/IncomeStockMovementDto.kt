package cz.smartbrains.qesu.module.stock.movement.dto

import cz.smartbrains.qesu.module.company.dto.CompanyDto
import cz.smartbrains.qesu.module.stock.movement.type.IncomeStockMovementType
import lombok.EqualsAndHashCode
import lombok.NoArgsConstructor
import lombok.ToString
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
class IncomeStockMovementDto : StockMovementDto() {
    var type: @NotNull IncomeStockMovementType? = null
    var supplier: CompanyDto? = null
    var deliveryNote: @Size(max = 50) String? = null
    var outcomeMovement: OutcomeStockMovementDto? = null
    override fun toString(): String {
        return "IncomeStockMovementDto(type=$type, supplier=$supplier, deliveryNote=$deliveryNote, outcomeMovement=$outcomeMovement) ${super.toString()}"
    }


}