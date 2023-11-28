package cz.smartbrains.qesu.module.inventory.service

import java.math.BigInteger

interface InventoryNumberFactory {
    fun nextNumber(stockId: Long, year: Int): BigInteger
}