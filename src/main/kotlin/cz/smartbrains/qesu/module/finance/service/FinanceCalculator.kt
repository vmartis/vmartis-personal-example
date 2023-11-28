package cz.smartbrains.qesu.module.finance.service

import cz.smartbrains.qesu.module.configuration.type.VatCalculationType
import java.math.BigDecimal

interface FinanceCalculator {
    fun calculateVat(
        vatCalculationType: VatCalculationType,
        price: BigDecimal?,
        amount: BigDecimal?,
        vatRate: BigDecimal?
    ): BigDecimal?

    fun calculateTotalWithoutVat(
        vatCalculationType: VatCalculationType,
        price: BigDecimal?,
        amount: BigDecimal?,
        vatRate: BigDecimal?
    ): BigDecimal?

    fun calculateTotalWithVat(
        vatCalculationType: VatCalculationType,
        price: BigDecimal?,
        amount: BigDecimal?,
        vatRate: BigDecimal?
    ): BigDecimal?
}