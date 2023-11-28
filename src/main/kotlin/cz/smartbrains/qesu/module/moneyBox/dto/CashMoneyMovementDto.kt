package cz.smartbrains.qesu.module.moneyBox.dto

import lombok.EqualsAndHashCode
import lombok.ToString
import java.math.BigDecimal
import javax.validation.constraints.Digits

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
class CashMoneyMovementDto : MoneyMovementDto() {
    var amountCash: @Digits(integer = 8, fraction = 2) BigDecimal? = null
    var amountCheque: @Digits(integer = 8, fraction = 2) BigDecimal? = null
}