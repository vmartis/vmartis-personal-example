package cz.smartbrains.qesu.module.stock.service

import org.springframework.security.access.prepost.PreAuthorize

interface StockBalanceService {
    @PreAuthorize("hasAuthority('STOCK')")
    fun updateBalance(stockId: Long, recalculatePrice: Boolean = false)
}