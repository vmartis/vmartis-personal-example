package cz.smartbrains.qesu.module.stock.movement.repository

import cz.smartbrains.qesu.module.item.entity.Item_
import cz.smartbrains.qesu.module.stock.entity.Stock_
import cz.smartbrains.qesu.module.stock.movement.dto.StockMovementFilter
import cz.smartbrains.qesu.module.stock.movement.entity.StockMovement
import cz.smartbrains.qesu.module.stock.movement.entity.StockMovementItem_
import cz.smartbrains.qesu.module.stock.movement.entity.StockMovement_
import org.springframework.stereotype.Repository
import java.time.temporal.ChronoUnit
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate

@Repository
class StockMovementRepositoryImpl : StockMovementRepositoryCustom {
    @PersistenceContext
    private val entityManager: EntityManager? = null
    override fun findByFilter(filter: StockMovementFilter): List<StockMovement> {
        val cb = entityManager!!.criteriaBuilder
        val q = cb.createQuery(StockMovement::class.java)
        val stockMovement = q.from(StockMovement::class.java)
        stockMovement.fetch<Any, Any>(StockMovement_.STOCK, JoinType.LEFT)
        stockMovement.fetch<Any, Any>(StockMovement_.INVENTORY, JoinType.LEFT)
        stockMovement.fetch<Any, Any>(StockMovement_.UPDATED_BY, JoinType.LEFT)
        stockMovement.fetch<Any, Any>(StockMovement_.CREATED_BY, JoinType.LEFT)
        val allPredicates: MutableList<Predicate> = ArrayList()
        if (filter.stockId != null) {
            allPredicates.add(cb.equal(stockMovement.get<Any>(StockMovement_.STOCK).get<Any>(Stock_.ID), cb.literal(filter.stockId)))
        }
        if (filter.stockId != null) {
            allPredicates.add(cb.equal(stockMovement.get<Any>(StockMovement_.STOCK).get<Any>(Stock_.ID), cb.literal(filter.stockId)))
        }
        if (filter.dateFrom != null) {
            allPredicates.add(cb.greaterThanOrEqualTo(stockMovement.get(StockMovement_.DATE), cb.literal(filter.dateFrom!!.atStartOfDay())))
        }
        if (filter.dateTo != null) {
            allPredicates.add(cb.lessThan(stockMovement.get(StockMovement_.DATE), cb.literal(filter.dateTo!!.plus(1, ChronoUnit.DAYS).atStartOfDay())))
        }
        if (filter.itemId != null) {
            allPredicates.add(cb.equal(stockMovement.join<Any, Any>(StockMovement_.ITEMS).get<Any>(StockMovementItem_.ITEM).get<Any>(Item_.ID), cb.literal(filter.itemId)))
        }
        if (filter.itemBatchNumber != null) {
            allPredicates.add(cb.isMember(filter.itemBatchNumber, stockMovement.join<Any, Any>(StockMovement_.ITEMS).get<Collection<String>>(StockMovementItem_.ITEM_BATCHES)))
        }
        q.orderBy(cb.desc(stockMovement.get<Any>(StockMovement_.DATE)))
        if (allPredicates.isNotEmpty()) {
            q.where(*allPredicates.toTypedArray())
        }
        return entityManager.createQuery(q).resultList
    }
}