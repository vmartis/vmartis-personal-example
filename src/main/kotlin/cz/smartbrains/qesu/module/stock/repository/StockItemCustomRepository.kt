package cz.smartbrains.qesu.module.stock.repository

import cz.smartbrains.qesu.module.stock.dto.StockItemFilter
import cz.smartbrains.qesu.module.stock.entity.StockItem

interface StockItemCustomRepository {
    fun findByFilter(filter: StockItemFilter): List<StockItem>
}