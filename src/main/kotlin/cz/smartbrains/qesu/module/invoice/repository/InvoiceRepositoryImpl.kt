package cz.smartbrains.qesu.module.invoice.repository

import cz.smartbrains.qesu.module.invoice.entity.Invoice_
import cz.smartbrains.qesu.module.bank.entity.BankAccount
import cz.smartbrains.qesu.module.company.entity.Company_
import cz.smartbrains.qesu.module.invoice.dto.InvoiceFilter
import cz.smartbrains.qesu.module.invoice.entity.Invoice
import cz.smartbrains.qesu.module.invoice.type.InvoiceStatus
import cz.smartbrains.qesu.module.subject.entity.Subject
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.Join
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate

@Repository
class InvoiceRepositoryImpl : InvoiceRepositoryCustom {
    @PersistenceContext
    private val entityManager: EntityManager? = null
    override fun findByFilter(filter: InvoiceFilter): List<Invoice> {
        val cb = entityManager!!.criteriaBuilder
        val q = cb.createQuery(Invoice::class.java)
        val invoice = q.from(Invoice::class.java)
        val subscriber = invoice.fetch<BankAccount?, Subject?>(Invoice_.SUBSCRIBER, JoinType.LEFT) as Join<BankAccount?, Subject?> //need to type to Join because of getting attribute later
        invoice.fetch<BankAccount?, Subject?>(Invoice_.SUPPLIER, JoinType.LEFT) as Join<BankAccount?, Subject?> //need to type to Join because of getting attribute later
        q.distinct(true)
        val allPredicates: MutableList<Predicate> = ArrayList()
        if (filter.subscriberId != null) {
            allPredicates.add(cb.equal(subscriber.get<Any>(Company_.ID), cb.literal(filter.subscriberId)))
        }
        if (filter.type != null) {
            allPredicates.add(cb.equal(invoice.get<Any>(Invoice_.TYPE), cb.literal(filter.type)))
        }
        if (filter.currency != null) {
            allPredicates.add(cb.equal(invoice.get<Any>(Invoice_.CURRENCY), cb.literal(filter.currency)))
        }
        if (filter.dateOfIssueFrom != null) {
            allPredicates.add(cb.greaterThanOrEqualTo(invoice.get(Invoice_.DATE_OF_ISSUE), cb.literal(filter.dateOfIssueFrom)))
        }
        if (filter.dateOfIssueTo != null) {
            allPredicates.add(cb.lessThanOrEqualTo(invoice.get(Invoice_.DATE_OF_ISSUE), cb.literal(filter.dateOfIssueTo)))
        }
        if (filter.status != null) {
            val zero = cb.literal(BigDecimal.ZERO)
            when (filter.status) {
                InvoiceStatus.PAID -> allPredicates.add(cb.equal(invoice.get<Any>(Invoice_.UNPAID_AMOUNT), zero))
                InvoiceStatus.OUTSTANDING -> allPredicates.add(cb.greaterThan(invoice.get(Invoice_.UNPAID_AMOUNT), zero))
                InvoiceStatus.OVERDUE -> {
                    allPredicates.add(cb.greaterThan(invoice.get(Invoice_.UNPAID_AMOUNT), zero))
                    allPredicates.add(cb.lessThan(invoice.get(Invoice_.DUE_DATE), LocalDate.now()))
                }
            }
        }
        if (filter.excludeId != null) {
            allPredicates.add(cb.notEqual(invoice.get<Any>(Invoice_.ID), cb.literal(filter.excludeId)))
        }
        if (allPredicates.isNotEmpty()) {
            q.where(*allPredicates.toTypedArray())
        }
        q.orderBy(cb.desc(invoice.get<Any>(Invoice_.NUMBER)))
        val query = entityManager.createQuery(q)
        if (filter.limit != null) {
            query.maxResults = filter.limit!!
        }
        return query.resultList
    }
}