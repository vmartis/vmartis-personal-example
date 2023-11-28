package cz.smartbrains.qesu.module.item.service

import cz.smartbrains.qesu.module.stock.movement.dto.StockMovementDto
import cz.smartbrains.qesu.module.inventory.dto.InventoryDto
import cz.smartbrains.qesu.module.item.dto.ItemBatchDto
import cz.smartbrains.qesu.module.item.dto.ItemBatchFilter
import org.springframework.security.access.prepost.PreAuthorize

interface ItemBatchService {
    @PreAuthorize("hasAnyAuthority('SETTING_ITEM', 'STOCK')")
    fun findByFilter(filter: ItemBatchFilter): List<ItemBatchDto>

    @PreAuthorize("hasAuthority('STOCK')")
    fun createFromStockMovement(stockMovement: StockMovementDto)

    @PreAuthorize("hasAuthority('STOCK')")
    fun updateForInventory(inventory: InventoryDto)
}