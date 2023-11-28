package cz.smartbrains.qesu.module.moneyBox.dto

import lombok.EqualsAndHashCode
import org.hibernate.validator.constraints.Length
import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@EqualsAndHashCode
class BankMoneyMovementSplitDto {
    constructor(originalMovement: @NotNull BankMoneyMovementDto?, newMovements: @NotEmpty @Valid MutableList<BankMoneyMovementDto>?, note: @Length(max = 300) String?) {
        this.originalMovement = originalMovement
        this.newMovements = newMovements
        this.note = note
    }

    var originalMovement: @NotNull BankMoneyMovementDto? = null
    var newMovements: @NotEmpty @Valid MutableList<BankMoneyMovementDto>? = null
    var note: @Length(max = 300) String? = null
}