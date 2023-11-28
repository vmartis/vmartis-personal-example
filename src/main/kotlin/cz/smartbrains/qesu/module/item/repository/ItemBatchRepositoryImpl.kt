package cz.smartbrains.qesu.module.item.repository

import cz.smartbrains.qesu.module.item.entity.ItemBatch_
import cz.smartbrains.qesu.module.item.entity.Item_
import cz.smartbrains.qesu.module.stock.entity.Stock_
import cz.smartbrains.qesu.module.item.dto.ItemBatchFilter
import cz.smartbrains.qesu.module.item.entity.ItemBatch
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate

@Repository
class ItemBatchRepositoryImpl : ItemBatchRepositoryCustom {
    @PersistenceContext
    private val entityManager: EntityManager? = null
    override fun findByFilter(filter: ItemBatchFilter): List<ItemBatch> {
        val cb = entityManager!!.criteriaBuilder
        val q = cb.createQuery(ItemBatch::class.java)
        val itemBatch = q.from(ItemBatch::class.java)
        itemBatch.fetch<Any, Any>(ItemBatch_.ITEM, JoinType.LEFT)
        itemBatch.fetch<Any, Any>(ItemBatch_.STOCK, JoinType.LEFT)
        val allPredicates: MutableList<Predicate> = ArrayList()
        if (filter.itemId != null) {
            allPredicates.add(cb.equal(itemBatch.get<Any>(ItemBatch_.ITEM).get<Any>(Item_.ID), cb.literal(filter.itemId)))
        }
        if (filter.stockId != null) {
            allPredicates.add(cb.equal(itemBatch.get<Any>(ItemBatch_.STOCK).get<Any>(Stock_.ID), cb.literal(filter.stockId)))
        }
        if (filter.activeOnly) {
            allPredicates.add(cb.equal(itemBatch.get<Any>(ItemBatch_.ACTIVE), cb.literal(true)))
        }
        q.orderBy(cb.asc(itemBatch.get<Any>(ItemBatch_.NAME)))
        if (allPredicates.isNotEmpty()) {
            q.where(*allPredicates.toTypedArray())
        }
        return entityManager.createQuery(q).resultList
    }
}