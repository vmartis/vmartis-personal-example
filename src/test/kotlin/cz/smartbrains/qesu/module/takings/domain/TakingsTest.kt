package cz.smartbrains.qesu.module.takings.domain

import cz.smartbrains.qesu.module.takings.entity.Takings
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class TakingsTest {
    @Test
    fun computedProperty_allNotInitializedTakings_returnsZero() {
        val takings = Takings()
        Assertions.assertThat(takings.totalFiscal).isEqualTo(BigDecimal.ZERO)
        Assertions.assertThat(takings.totalNonFiscal).isEqualTo(BigDecimal.ZERO)
        Assertions.assertThat(takings.cashNonFiscal).isEqualTo(BigDecimal.ZERO)
        Assertions.assertThat(takings.chequeNonFiscal).isEqualTo(BigDecimal.ZERO)
    }

    @Test
    fun computedProperty_someNotInitializedTakings_returnsZero() {
        val takings = Takings()
        takings.amountCheque = BigDecimal.valueOf(100)
        takings.amountCard = BigDecimal.valueOf(200)
        takings.amountCash = BigDecimal.valueOf(300)
        Assertions.assertThat(takings.totalFiscal).isEqualTo(BigDecimal.ZERO)
        Assertions.assertThat(takings.totalNonFiscal).isEqualTo(BigDecimal.ZERO)
        Assertions.assertThat(takings.cashNonFiscal).isEqualTo(BigDecimal.ZERO)
        Assertions.assertThat(takings.chequeNonFiscal).isEqualTo(BigDecimal.ZERO)
    }

    @Test
    fun computedProperty_initializedTakings_sumsOk() {
        val takings = Takings()
        takings.amountCheque = BigDecimal.valueOf(100)
        takings.amountCard = BigDecimal.valueOf(200)
        takings.amountCash = BigDecimal.valueOf(300)
        takings.totalVatAmount = BigDecimal.valueOf(400)
        takings.totalCashAmount = BigDecimal.valueOf(2000)
        takings.totalChequeAmount = BigDecimal.valueOf(500)
        Assertions.assertThat(takings.totalFiscal).isEqualTo(BigDecimal.valueOf(100 + 300.toLong()))
        Assertions.assertThat(takings.totalNonFiscal).isEqualTo(BigDecimal.valueOf(2000 + 500 - 100 - 300.toLong()))
        Assertions.assertThat(takings.cashNonFiscal).isEqualTo(BigDecimal.valueOf(2000 - 300.toLong()))
        Assertions.assertThat(takings.chequeNonFiscal).isEqualTo(BigDecimal.valueOf(500 - 100.toLong()))
    }
}