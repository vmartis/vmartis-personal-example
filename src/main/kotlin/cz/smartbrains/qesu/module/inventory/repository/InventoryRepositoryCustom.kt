package cz.smartbrains.qesu.module.inventory.repository

import cz.smartbrains.qesu.module.inventory.dto.InventoryFilter
import cz.smartbrains.qesu.module.inventory.entity.Inventory

interface InventoryRepositoryCustom {
    fun findByFilter(filter: InventoryFilter): List<Inventory>
}