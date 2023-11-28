package cz.smartbrains.qesu.module.stock.entity

import java.math.BigDecimal

class StockBalance(var itemId: Long, sumAmount: BigDecimal?, sumPrice: BigDecimal?) {
    var sumAmount: BigDecimal
    var sumPrice: BigDecimal

    init {
        this.sumAmount = sumAmount ?: BigDecimal.ZERO
        this.sumPrice = sumPrice ?: BigDecimal.ZERO
    }
}