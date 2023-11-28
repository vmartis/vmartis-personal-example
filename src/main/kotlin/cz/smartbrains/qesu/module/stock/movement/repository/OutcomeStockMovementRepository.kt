package cz.smartbrains.qesu.module.stock.movement.repository

import cz.smartbrains.qesu.module.stock.movement.entity.OutcomeStockMovement
import org.springframework.data.jpa.repository.JpaRepository

interface OutcomeStockMovementRepository : JpaRepository<OutcomeStockMovement, Long>, OutcomeStockMovementRepositoryCustom {
    fun findTopByOrderByIdDesc(): OutcomeStockMovement?
}