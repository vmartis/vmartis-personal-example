package cz.smartbrains.qesu.module.stock.repository

import cz.smartbrains.qesu.module.stock.entity.StockItem
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository

interface StockItemRepository : JpaRepository<StockItem, Long>, StockItemCustomRepository {
    fun findAllByStockId(stockId: Long, sort: Sort): List<StockItem>
}