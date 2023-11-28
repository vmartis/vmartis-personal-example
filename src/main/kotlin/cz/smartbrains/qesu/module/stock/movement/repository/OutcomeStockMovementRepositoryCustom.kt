package cz.smartbrains.qesu.module.stock.movement.repository

import cz.smartbrains.qesu.module.stock.movement.dto.OutcomeStockMovementFilter
import cz.smartbrains.qesu.module.stock.movement.entity.OutcomeStockMovement

interface OutcomeStockMovementRepositoryCustom {
    fun findByFilter(filter: OutcomeStockMovementFilter): List<OutcomeStockMovement>
}