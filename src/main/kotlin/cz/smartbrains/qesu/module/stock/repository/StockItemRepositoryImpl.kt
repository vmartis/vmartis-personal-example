package cz.smartbrains.qesu.module.stock.repository

import cz.smartbrains.qesu.module.item.entity.Item_
import cz.smartbrains.qesu.module.stock.dto.StockItemFilter
import cz.smartbrains.qesu.module.stock.entity.StockItem
import cz.smartbrains.qesu.module.stock.entity.StockItem_
import cz.smartbrains.qesu.module.stock.entity.Stock_
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Order
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

@Repository
class StockItemRepositoryImpl : StockItemCustomRepository {
    @PersistenceContext
    private val entityManager: EntityManager? = null
    override fun findByFilter(filter: StockItemFilter): List<StockItem> {
        val cb = entityManager!!.criteriaBuilder
        val q = cb.createQuery(StockItem::class.java)
        val stockItemRoot: Root<StockItem> = q.from(StockItem::class.java)!!
        stockItemRoot.fetch<Any, Any>(StockItem_.STOCK, JoinType.LEFT)
        stockItemRoot.fetch<Any, Any>(StockItem_.ITEM, JoinType.LEFT)
        val allPredicates: MutableList<Predicate> = ArrayList()
        var order: Order? = null
        if (filter.stockId != null) {
            allPredicates.add(cb.equal(stockItemRoot.get<Any>(StockItem_.STOCK).get<Any>(Stock_.ID), cb.literal(filter.stockId)))
        }
        if (filter.stockIds != null && !filter.stockIds!!.isEmpty()) {
            allPredicates.add(cb.`in`<Any>(stockItemRoot.get<Any>(StockItem_.STOCK).get<Any>(Stock_.ID)).value(filter.stockIds))
        }
        if (filter.itemId != null) {
            allPredicates.add(cb.equal(stockItemRoot.get<Any>(StockItem_.ITEM).get<Any>(Item_.ID), cb.literal(filter.itemId)))
            order = cb.asc(stockItemRoot.get<Any>(StockItem_.STOCK).get<Any>(Stock_.ORDER))
        }
        if (filter.activeOnly) {
            allPredicates.add(cb.equal(stockItemRoot.get<Any>(StockItem_.ACTIVE), cb.literal(true)))
        }
        if (allPredicates.isNotEmpty()) {
            q.where(*allPredicates.toTypedArray())
        }
        if (order == null) {
            order = cb.asc(stockItemRoot.get<Any>(StockItem_.ITEM).get<Any>(Item_.NAME)) // default order is by item.name
        }
        q.orderBy(order)
        return entityManager.createQuery(q).resultList
    }
}