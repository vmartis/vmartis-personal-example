package cz.smartbrains.qesu.module.stock.service

import cz.smartbrains.qesu.module.stock.dto.StockItemDto
import cz.smartbrains.qesu.module.stock.dto.StockItemFilter
import org.springframework.security.access.prepost.PreAuthorize

interface StockItemService {
    @PreAuthorize("hasAuthority('STOCK')")
    fun find(id: Long): StockItemDto

    @PreAuthorize("hasAnyAuthority('STOCK')")
    fun findAll(filter: StockItemFilter): List<StockItemDto>

    @PreAuthorize("hasAuthority('SETTING_STOCK')")
    fun create(stockItem: StockItemDto): StockItemDto

    @PreAuthorize("hasAuthority('SETTING_STOCK')")
    fun update(stockItem: StockItemDto): StockItemDto
}