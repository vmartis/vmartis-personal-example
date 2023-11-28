package cz.smartbrains.qesu.module.moneyBox.dto

import cz.smartbrains.qesu.module.common.entity.Orderable
import lombok.EqualsAndHashCode
import java.math.BigDecimal
import javax.validation.constraints.Digits

@EqualsAndHashCode(callSuper = false)
class CashMoneyBoxDto : MoneyBoxDto(), Orderable {
    var amountCheque: @Digits(integer = 10, fraction = 2) BigDecimal? = null
    var amountCash: @Digits(integer = 10, fraction = 2) BigDecimal? = null
}