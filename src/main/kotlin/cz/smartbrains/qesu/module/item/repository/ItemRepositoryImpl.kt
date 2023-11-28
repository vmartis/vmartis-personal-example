package cz.smartbrains.qesu.module.item.repository

import cz.smartbrains.qesu.module.item.category.entity.ItemCategory_
import cz.smartbrains.qesu.module.item.dto.ItemFilter
import cz.smartbrains.qesu.module.item.entity.Item
import cz.smartbrains.qesu.module.item.entity.Item_
import cz.smartbrains.qesu.module.item.type.ItemDeterminationType
import org.springframework.stereotype.Repository
import java.util.*
import java.util.stream.Collectors
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate

@Repository
class ItemRepositoryImpl : ItemRepositoryCustom {
    @PersistenceContext
    private val entityManager: EntityManager? = null
    override fun findByFilter(filter: ItemFilter): List<Item> {
        val cb = entityManager!!.criteriaBuilder
        val q = cb.createQuery(Item::class.java)
        val item = q.from(Item::class.java)
        item.fetch<Any, Any>(Item_.UNIT, JoinType.LEFT)
        item.fetch<Any, Any>(Item_.CATEGORY, JoinType.LEFT)
        val allPredicates: MutableList<Predicate> = ArrayList()

        if (filter.type != null) {
            allPredicates.add(cb.equal(item.get<Any>(Item_.TYPE), cb.literal(filter.type)))
        }
        if (filter.origin != null) {
            allPredicates.add(cb.equal(item.get<Any>(Item_.ORIGIN), cb.literal(filter.origin)))
        }
        if (filter.determination != null) {
            if (filter.determination == ItemDeterminationType.SALE) {
                allPredicates.add(cb.equal(item.get<Any>(Item_.FOR_PURCHASE), cb.literal(true)))
            } else if (filter.determination == ItemDeterminationType.STOCK) {
                allPredicates.add(cb.equal(item.get<Any>(Item_.FOR_STOCK), cb.literal(true)))
            }
        }
        if (filter.batchEvidence != null) {
            allPredicates.add(cb.equal(item.get<Any>(Item_.BATCH_EVIDENCE), cb.literal(filter.batchEvidence)))
        }
        if (filter.activeOnly) {
            allPredicates.add(cb.equal(item.get<Any>(Item_.ACTIVE), cb.literal(true)))
        }
        if (filter.categories.isNotEmpty()) {
            allPredicates.add(cb.or(*filter.categories.stream()
                    .map { categoryId: Long -> cb.equal(item.get<Any>(Item_.CATEGORY).get<Any>(ItemCategory_.ID), cb.literal(categoryId)) }
                    .collect(Collectors.toList())
                    .toTypedArray()))
        }
        q.orderBy(cb.asc(item.get<Any>(Item_.NAME)))
        if (allPredicates.isNotEmpty()) {
            q.where(*allPredicates.toTypedArray())
        }
        return entityManager.createQuery(q).resultList
    }
}