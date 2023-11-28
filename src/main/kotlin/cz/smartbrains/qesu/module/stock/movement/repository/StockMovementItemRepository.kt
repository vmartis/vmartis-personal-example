package cz.smartbrains.qesu.module.stock.movement.repository

import cz.smartbrains.qesu.module.stock.entity.StockBalance
import cz.smartbrains.qesu.module.stock.movement.entity.StockMovementItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface StockMovementItemRepository : JpaRepository<StockMovementItem?, Long?> {
    @Query("SELECT new cz.smartbrains.qesu.module.stock.entity.StockBalance(item.id, SUM(mi.amount), SUM(mi.totalPrice)) " +
            "FROM StockMovementItem mi LEFT JOIN mi.movement m LEFT JOIN mi.item item LEFT JOIN m.stock s WHERE s.id=:stockId GROUP BY item")
    fun computeItemBalances(@Param("stockId") stockId: Long): List<StockBalance>
}