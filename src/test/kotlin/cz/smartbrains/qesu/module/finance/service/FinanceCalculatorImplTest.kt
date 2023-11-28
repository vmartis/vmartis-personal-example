package cz.smartbrains.qesu.module.finance.service

import cz.smartbrains.qesu.module.configuration.type.VatCalculationType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class FinanceCalculatorImplTest {
    val financeCalculator: FinanceCalculator = FinanceCalculatorImpl()

    @Test
    fun calculateVat_sumType_validResult() {

        val value = financeCalculator.calculateVat(
            VatCalculationType.SUM,
            BigDecimal.valueOf(1.12),
            BigDecimal.valueOf(16),
            BigDecimal.valueOf(20)
        )
        assertEquals(BigDecimal.valueOf(3.58), value)
    }

    @Test
    fun calculateTotalWithoutVat_sumType_validResult() {

        val value = financeCalculator.calculateTotalWithoutVat(
            VatCalculationType.SUM,
            BigDecimal.valueOf(1.12),
            BigDecimal.valueOf(16),
            BigDecimal.valueOf(20)
        )
        assertEquals(BigDecimal.valueOf(17.92), value)
    }

    @Test
    fun calculateTotalWithoutVat_itemType_validResult() {

        val value = financeCalculator.calculateTotalWithoutVat(
            VatCalculationType.ITEM,
            BigDecimal.valueOf(1.12),
            BigDecimal.valueOf(16),
            BigDecimal.valueOf(20)
        )
        assertEquals(BigDecimal.valueOf(17.87), value)
    }

    @Test
    fun calculateTotalWithVat_sumType_validResult() {

        val value = financeCalculator.calculateTotalWithVat(
            VatCalculationType.SUM,
            BigDecimal.valueOf(1.12),
            BigDecimal.valueOf(16),
            BigDecimal.valueOf(20)
        )
        assertEquals(BigDecimal.valueOf(21.50).setScale(2), value)
    }

    @Test
    fun calculateTotalWithVat_itemType_validResult() {

        val value = financeCalculator.calculateTotalWithVat(
            VatCalculationType.ITEM,
            BigDecimal.valueOf(1.12),
            BigDecimal.valueOf(16),
            BigDecimal.valueOf(20)
        )
        assertEquals(BigDecimal.valueOf(21.44), value)
    }
}