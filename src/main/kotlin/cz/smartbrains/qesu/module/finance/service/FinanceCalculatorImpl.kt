package cz.smartbrains.qesu.module.finance.service

import cz.smartbrains.qesu.module.configuration.type.VatCalculationType
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class FinanceCalculatorImpl : FinanceCalculator {

    private fun calcVatRateNum (vatRate: BigDecimal): BigDecimal {
        return vatRate.divide(HUNDRED).plus(BigDecimal.ONE)
    }

    private fun calculateItemTotalWithVat (price: BigDecimal, vatRateNum: BigDecimal, amount: BigDecimal): BigDecimal {
        return price
            .multiply(vatRateNum)
            .setScale(SCALE, RoundingMode.HALF_UP)
            .multiply(amount)
            .setScale(SCALE, RoundingMode.HALF_UP)
    }

    override fun calculateVat (vatCalculationType: VatCalculationType, price: BigDecimal?, amount: BigDecimal?, vatRate: BigDecimal?): BigDecimal? {
        if (price == null || amount == null || vatRate == null) {
            return BigDecimal.ZERO
        }
        if (vatCalculationType === VatCalculationType.SUM) {
            return amount
                .multiply(price)
                .multiply(vatRate)
                .divide(HUNDRED, SCALE, RoundingMode.HALF_UP)
        } else {
            val vatRateNum = calcVatRateNum(vatRate)
            val totalWithVat = calculateItemTotalWithVat(price, vatRateNum, amount)
            val totalWithoutVat = totalWithVat.divide(vatRateNum, SCALE, RoundingMode.HALF_UP)
            return totalWithVat.minus(totalWithoutVat)
        }
    }

    override fun calculateTotalWithoutVat (vatCalculationType: VatCalculationType, price: BigDecimal?, amount: BigDecimal?, vatRate: BigDecimal?): BigDecimal? {
        if (price == null || amount == null || vatRate == null) {
            return BigDecimal.ZERO
        }
        return if (vatCalculationType === VatCalculationType.SUM) {
            amount
                .multiply(price)
                .setScale(SCALE, RoundingMode.HALF_UP)
        } else {
            val vatRateNum = calcVatRateNum(vatRate)
            val totalWithVat = calculateItemTotalWithVat(price, vatRateNum, amount)
            totalWithVat.divide(vatRateNum, SCALE, RoundingMode.HALF_UP)
        }
    }

    override fun calculateTotalWithVat (vatCalculationType: VatCalculationType, price: BigDecimal?, amount: BigDecimal?, vatRate: BigDecimal?): BigDecimal? {
        if (price == null || amount == null || vatRate == null) {
            return BigDecimal.ZERO
        }
        val vatRateNum = calcVatRateNum(vatRate)
        if (vatCalculationType === VatCalculationType.SUM) {
            return amount
                .multiply(price)
                .multiply(vatRateNum)
                .setScale(SCALE, RoundingMode.HALF_UP)
        } else {
            return calculateItemTotalWithVat(price, vatRateNum, amount)
        }
    }

    companion object {
        val SCALE = 2
        val HUNDRED = BigDecimal.valueOf(100)
    }
}