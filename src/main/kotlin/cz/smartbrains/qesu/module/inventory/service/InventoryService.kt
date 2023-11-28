package cz.smartbrains.qesu.module.inventory.service

import cz.smartbrains.qesu.module.inventory.dto.InventoryDto
import cz.smartbrains.qesu.module.inventory.dto.InventoryFilter
import cz.smartbrains.qesu.security.AlfaUserDetails
import org.springframework.security.access.prepost.PreAuthorize

interface InventoryService {
    @PreAuthorize("hasAuthority('STOCK')")
    fun findByFilter(filter: InventoryFilter): List<InventoryDto>

    @PreAuthorize("hasAuthority('STOCK')")
    fun find(id: Long): InventoryDto

    @PreAuthorize("hasAuthority('STOCK')")
    fun create(inventoryDto: InventoryDto, user: AlfaUserDetails): InventoryDto

    @PreAuthorize("hasAuthority('STOCK')")
    fun update(inventoryDto: InventoryDto, user: AlfaUserDetails): InventoryDto

    @PreAuthorize("hasAuthority('STOCK')")
    fun delete(id: Long)
}