package cz.smartbrains.qesu.module.order.repository

import cz.smartbrains.qesu.module.order.entity.OrderStatement_
import cz.smartbrains.qesu.module.order.entity.Order_
import cz.smartbrains.qesu.module.paymenttype.entity.PaymentType_
import cz.smartbrains.qesu.module.company.entity.Company_
import cz.smartbrains.qesu.module.company.entity.SaleSettings_
import cz.smartbrains.qesu.module.order.dto.OrderStatementFilter
import cz.smartbrains.qesu.module.order.entity.Order
import cz.smartbrains.qesu.module.order.entity.OrderStatement
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate

@Repository
class OrderStatementRepositoryImpl : OrderStatementRepositoryCustom {
    @PersistenceContext
    private val entityManager: EntityManager? = null
    override fun findByFilter(filter: OrderStatementFilter): List<OrderStatement> {
        val cb = entityManager!!.criteriaBuilder
        val q = cb.createQuery(OrderStatement::class.java)
        val orderStatementRoot = q.from(OrderStatement::class.java)
        val orders = orderStatementRoot.fetch<Any, Any>(OrderStatement_.ORDERS, JoinType.LEFT)
        orderStatementRoot.fetch<Any, Any>(OrderStatement_.INVOICES, JoinType.LEFT)
        q.distinct(true)
        val allPredicates: MutableList<Predicate> = ArrayList()
        if (filter.dateFrom != null) {
            allPredicates.add(cb.greaterThanOrEqualTo(orderStatementRoot.get(OrderStatement_.DATE), cb.literal(filter.dateFrom)))
        }
        if (filter.dateTo != null) {
            allPredicates.add(cb.lessThanOrEqualTo(orderStatementRoot.get(OrderStatement_.DATE), cb.literal(filter.dateTo)))
        }
        if (filter.paymentTypeId != null) {
            val orderSubquery = q.subquery(Order::class.java)
            val orderRoot = orderSubquery.from(Order::class.java)
            orderSubquery.select(orderRoot)
                    .where(
                            cb.equal(orderRoot.get<Any>(Order_.STATEMENT), orderStatementRoot.get<Any>(OrderStatement_.ID)),
                            cb.equal(orderRoot.get<Any>(Order_.SUBSCRIBER)
                                    .get<Any>(Company_.SALE_SETTINGS)
                                    .get<Any>(SaleSettings_.PAYMENT_TYPE)
                                    .get<Any>(PaymentType_.ID), filter.paymentTypeId))
            allPredicates.add(cb.exists(orderSubquery))
        }
        if (filter.subscriberId != null) {
            val orderSubquery = q.subquery(Order::class.java)
            val orderRoot = orderSubquery.from(Order::class.java)
            orderSubquery.select(orderRoot)
                    .where(
                            cb.equal(orderRoot.get<Any>(Order_.STATEMENT), orderStatementRoot.get<Any>(OrderStatement_.ID)),
                            cb.equal(orderRoot.get<Any>(Order_.SUBSCRIBER)
                                    .get<Any>(Company_.ID), filter.subscriberId))
            allPredicates.add(cb.exists(orderSubquery))
        }
        if (allPredicates.isNotEmpty()) {
            q.where(*allPredicates.toTypedArray())
        }
        q.orderBy(cb.desc(orderStatementRoot.get<Any>(OrderStatement_.NUMBER)))
        return entityManager.createQuery(q).resultList
    }
}