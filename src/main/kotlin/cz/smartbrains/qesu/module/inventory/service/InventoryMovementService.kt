package cz.smartbrains.qesu.module.inventory.service

import cz.smartbrains.qesu.security.AlfaUserDetails

interface InventoryMovementService {
    fun updateMovements(inventoryId: Long, alfaUserDetails: AlfaUserDetails)
}