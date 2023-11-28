package cz.smartbrains.qesu.module.order.repository

import cz.smartbrains.qesu.module.item.entity.Item_
import cz.smartbrains.qesu.module.order.entity.Order_
import cz.smartbrains.qesu.module.region.entity.Region_
import cz.smartbrains.qesu.module.common.entity.Address_
import cz.smartbrains.qesu.module.company.entity.CompanyBranch_
import cz.smartbrains.qesu.module.company.entity.Company_
import cz.smartbrains.qesu.module.order.dto.OrderFilter
import cz.smartbrains.qesu.module.order.entity.Order
import org.springframework.stereotype.Repository
import java.lang.Boolean
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate

@Repository
class OrderRepositoryImpl : OrderRepositoryCustom {
    @PersistenceContext
    private val entityManager: EntityManager? = null
    override fun findByFilter(filter: OrderFilter): List<Order> {
        val cb = entityManager!!.criteriaBuilder
        val q = cb.createQuery(Order::class.java)
        val orderRoot = q.from(Order::class.java)
        orderRoot.fetch<Any, Any>(Order_.ITEMS, JoinType.LEFT)
        orderRoot.fetch<Any, Any>(Order_.SUBSCRIBER, JoinType.LEFT)
        orderRoot.fetch<Any, Any>(Order_.SUBSCRIBER_BRANCH, JoinType.LEFT)
        q.distinct(true)
        val allPredicates: MutableList<Predicate> = ArrayList()
        if (filter.supplierBranchId != null) {
            allPredicates.add(cb.equal(orderRoot.get<Any>(Order_.SUPPLIER_BRANCH).get<Any>(Item_.ID), cb.literal(filter.supplierBranchId)))
        }
        if (filter.subscriberId != null) {
            allPredicates.add(cb.equal(orderRoot.get<Any>(Order_.SUBSCRIBER).get<Any>(Company_.ID), cb.literal(filter.subscriberId)))
        }
        if (filter.regionId != null) {
            // subscriberBranch is not null and subscriberBranch.address.region.id == region.id
            val subscriberBranchRegion = cb.and(
                    orderRoot.get<Any>(Order_.SUBSCRIBER_BRANCH).isNotNull,
                    cb.equal(orderRoot.get<Any>(Order_.SUBSCRIBER_BRANCH).get<Any>(CompanyBranch_.ADDRESS).get<Any>(Address_.REGION).get<Any>(Region_.ID), cb.literal(filter.regionId))
            )
            // subscriber.address.region.id == region.id
            val companyRegion = cb.equal(orderRoot.get<Any>(Order_.SUBSCRIBER).get<Any>(Company_.ADDRESS).get<Any>(Address_.REGION).get<Any>(Region_.ID), cb.literal(filter.regionId))
            allPredicates.add(cb.or(subscriberBranchRegion, companyRegion))
        }
        if (filter.date != null) {
            allPredicates.add(cb.equal(orderRoot.get<Any>(Order_.DATE), cb.literal(filter.date)))
        }
        if (filter.dateFrom != null) {
            allPredicates.add(cb.greaterThanOrEqualTo(orderRoot.get(Order_.DATE), cb.literal(filter.dateFrom)))
        }
        if (filter.dateTo != null) {
            allPredicates.add(cb.lessThanOrEqualTo(orderRoot.get(Order_.DATE), cb.literal(filter.dateTo)))
        }
        if (filter.currency != null) {
            allPredicates.add(cb.equal(orderRoot.get<Any>(Order_.CURRENCY), cb.literal(filter.currency)))
        }
        if (filter.state != null) {
            allPredicates.add(cb.equal(orderRoot.get<Any>(Order_.STATE), cb.literal(filter.state)))
        }
        if (filter.noStatement === Boolean.TRUE) {
            allPredicates.add(cb.isNull(orderRoot.get<Any>(Order_.STATEMENT)))
        }
        if (allPredicates.isNotEmpty()) {
            q.where(*allPredicates.toTypedArray())
        }
        q.orderBy(cb.desc(orderRoot.get<Any>(Order_.DATE)), cb.asc(orderRoot.get<Any>(Order_.SUBSCRIBER).get<Any>(Company_.NAME)))
        return entityManager.createQuery(q).resultList
    }
}