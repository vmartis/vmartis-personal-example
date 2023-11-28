package cz.smartbrains.qesu.module.stock.movement.repository

import cz.smartbrains.qesu.module.stock.movement.type.StockMovementType
import java.math.BigInteger

interface StockMovementNumberFactory {
    fun nextNumber(type: StockMovementType, stockId: Long, year: Int): BigInteger
}