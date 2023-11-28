package cz.smartbrains.qesu.module.stock.movement.repository

import cz.smartbrains.qesu.module.stock.movement.entity.IncomeStockMovement
import org.springframework.data.jpa.repository.JpaRepository

interface IncomeStockMovementRepository : JpaRepository<IncomeStockMovement, Long> {
    fun findTopByOrderByIdDesc(): IncomeStockMovement?
}