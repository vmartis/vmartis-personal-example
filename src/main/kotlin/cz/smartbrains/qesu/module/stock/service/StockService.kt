package cz.smartbrains.qesu.module.stock.service

import cz.smartbrains.qesu.module.common.service.OrdeableEntityService
import cz.smartbrains.qesu.module.stock.dto.StockDto
import org.springframework.security.access.prepost.PreAuthorize

interface StockService : OrdeableEntityService<StockDto> {
    @PreAuthorize("hasAnyAuthority('SETTING_STOCK', 'STOCK')")
    fun findAll(): List<StockDto>

    @PreAuthorize("hasAuthority('SETTING_STOCK')")
    fun create(stockDto: StockDto): StockDto

    @PreAuthorize("hasAnyAuthority('SETTING_STOCK', 'SETTING_REGULAR_SALE')")
    fun update(stockDto: StockDto): StockDto

    @PreAuthorize("hasAuthority('SETTING_STOCK')")
    override fun updatePosition(id: Long, position: Int): StockDto
}