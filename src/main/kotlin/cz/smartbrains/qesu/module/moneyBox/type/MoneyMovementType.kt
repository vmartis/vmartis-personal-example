package cz.smartbrains.qesu.module.moneyBox.type

import java.math.BigDecimal

enum class MoneyMovementType {
    INCOME, OUTCOME;

    companion object {
        @JvmStatic
        fun of(amount: BigDecimal): MoneyMovementType {
            return if (amount > BigDecimal.ZERO) INCOME else OUTCOME
        }
    }
}