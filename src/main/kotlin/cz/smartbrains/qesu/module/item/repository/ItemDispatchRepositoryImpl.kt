package cz.smartbrains.qesu.module.item.repository

import cz.smartbrains.qesu.module.item.entity.ItemDispatch_
import cz.smartbrains.qesu.module.item.entity.Item_
import cz.smartbrains.qesu.module.item.dto.ItemDispatchFilter
import cz.smartbrains.qesu.module.item.entity.ItemDispatch
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate

@Repository
class ItemDispatchRepositoryImpl : ItemDispatchRepositoryCustom {
    @PersistenceContext
    private val entityManager: EntityManager? = null
    override fun findByFilter(filter: ItemDispatchFilter): List<ItemDispatch> {
        val cb = entityManager!!.criteriaBuilder
        val q = cb.createQuery(ItemDispatch::class.java)
        val itemDispatch = q.from(ItemDispatch::class.java)
        itemDispatch.fetch<Any, Any>(ItemDispatch_.ITEM, JoinType.LEFT)
        itemDispatch.fetch<Any, Any>(ItemDispatch_.SUB_ITEM, JoinType.LEFT)
        val allPredicates: MutableList<Predicate> = ArrayList()
        if (filter.itemId != null) {
            allPredicates.add(cb.equal(itemDispatch.get<Any>(ItemDispatch_.ITEM).get<Any>(Item_.ID), cb.literal(filter.itemId)))
        }
        if (allPredicates.isNotEmpty()) {
            q.where(*allPredicates.toTypedArray())
        }
        return entityManager.createQuery(q).resultList
    }
}