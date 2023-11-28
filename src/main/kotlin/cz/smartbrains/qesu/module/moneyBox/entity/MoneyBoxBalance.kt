package cz.smartbrains.qesu.module.moneyBox.entity

import java.math.BigDecimal
import java.time.LocalDate

class MoneyBoxBalance {
    var amountCash: BigDecimal? = null
    var amountCheque: BigDecimal? = null
    var totalAmount: BigDecimal
    var date: LocalDate? = null
    var moneyBox: MoneyBox? = null



    constructor(totalAmount: BigDecimal, moneyBox: MoneyBox?) {
        this.totalAmount = totalAmount
        this.moneyBox = moneyBox
    }

    @JvmOverloads
    constructor(totalAmount: BigDecimal?, amountCash: BigDecimal? = null, amountCheque: BigDecimal? = null) {
        this.totalAmount = totalAmount ?: BigDecimal.ZERO
        this.amountCash = amountCash ?: BigDecimal.ZERO
        this.amountCheque = amountCheque ?: BigDecimal.ZERO
    }

    constructor(amountCash: BigDecimal?, amountCheque: BigDecimal?, totalAmount: BigDecimal, moneyBox: MoneyBox?) {
        this.amountCash = amountCash
        this.amountCheque = amountCheque
        this.totalAmount = totalAmount
        this.moneyBox = moneyBox
    }
}