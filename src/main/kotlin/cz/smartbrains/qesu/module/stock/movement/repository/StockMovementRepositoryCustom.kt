package cz.smartbrains.qesu.module.stock.movement.repository

import cz.smartbrains.qesu.module.stock.movement.dto.StockMovementFilter
import cz.smartbrains.qesu.module.stock.movement.entity.StockMovement

interface StockMovementRepositoryCustom {
    fun findByFilter(filter: StockMovementFilter): List<StockMovement>
}