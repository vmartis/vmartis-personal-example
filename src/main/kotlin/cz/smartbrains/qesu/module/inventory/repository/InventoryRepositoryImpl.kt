package cz.smartbrains.qesu.module.inventory.repository

import cz.smartbrains.qesu.module.inventory.dto.InventoryFilter
import cz.smartbrains.qesu.module.inventory.entity.Inventory
import cz.smartbrains.qesu.module.inventory.entity.InventoryItem_
import cz.smartbrains.qesu.module.inventory.entity.Inventory_
import cz.smartbrains.qesu.module.item.entity.Item_
import cz.smartbrains.qesu.module.stock.entity.Stock_
import org.springframework.stereotype.Repository
import java.time.temporal.ChronoUnit
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate

@Repository
class InventoryRepositoryImpl : InventoryRepositoryCustom {
    @PersistenceContext
    private val entityManager: EntityManager? = null
    override fun findByFilter(filter: InventoryFilter): List<Inventory> {
        val cb = entityManager!!.criteriaBuilder
        val q = cb.createQuery(Inventory::class.java)
        val inventory = q.from(Inventory::class.java)
        inventory.fetch<Any, Any>(Inventory_.STOCK, JoinType.LEFT)
        inventory.fetch<Any, Any>(Inventory_.UPDATED_BY, JoinType.LEFT)
        inventory.fetch<Any, Any>(Inventory_.CREATED_BY, JoinType.LEFT)
        val allPredicates: MutableList<Predicate> = ArrayList()
        if (filter.stockId != null) {
            allPredicates.add(cb.equal(inventory.get<Any>(Inventory_.STOCK).get<Any>(Stock_.ID), cb.literal(filter.stockId)))
        }
        if (filter.dateFrom != null) {
            allPredicates.add(cb.greaterThanOrEqualTo(inventory.get(Inventory_.DATE), cb.literal(filter.dateFrom.atStartOfDay())))
        }
        if (filter.dateTo != null) {
            allPredicates.add(cb.lessThan(inventory.get(Inventory_.DATE), cb.literal(filter.dateTo.plus(1, ChronoUnit.DAYS).atStartOfDay())))
        }
        if (filter.itemId != null) {
            allPredicates.add(cb.equal(inventory.join<Any, Any>(Inventory_.ITEMS).get<Any>(InventoryItem_.ITEM).get<Any>(Item_.ID), cb.literal(filter.itemId)))
        }
        if (filter.itemBatchNumber != null) {
            allPredicates.add(cb.isMember(filter.itemBatchNumber, inventory.join<Any, Any>(Inventory_.ITEMS).get(InventoryItem_.ITEM_BATCHES)))
        }
        q.orderBy(cb.desc(inventory.get<Any>(Inventory_.DATE)))
        if (allPredicates.isNotEmpty()) {
            q.where(*allPredicates.toTypedArray())
        }
        return entityManager.createQuery(q).resultList
    }
}