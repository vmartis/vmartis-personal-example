package cz.smartbrains.qesu.module.takings.repository

import cz.smartbrains.qesu.module.takings.entity.Takings_
import cz.smartbrains.qesu.module.cashbox.entity.CashBox_
import cz.smartbrains.qesu.module.company.entity.CompanyBranch_
import cz.smartbrains.qesu.module.takings.dto.TakingsFilter
import cz.smartbrains.qesu.module.takings.entity.Takings
import org.springframework.stereotype.Repository
import org.springframework.util.CollectionUtils
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate

@Repository
class TakingsRepositoryImpl : TakingsRepositoryCustom {
    @PersistenceContext
    private val entityManager: EntityManager? = null
    override fun findByFilter(filter: TakingsFilter): List<Takings> {
        val cb = entityManager!!.criteriaBuilder
        val q = cb.createQuery(Takings::class.java)
        val takingsRoot = q.from(Takings::class.java)
        val cashBox = takingsRoot.fetch<Any, Any>(Takings_.CASH_BOX, JoinType.LEFT)
        cashBox.fetch<Any, Any>(CashBox_.COMPANY_BRANCH, JoinType.LEFT)
        q.distinct(true)
        val allPredicates: MutableList<Predicate> = ArrayList()
        if (filter.companyBranchId != null) {
            allPredicates.add(cb.equal(takingsRoot.get<Any>(Takings_.CASH_BOX).get<Any>(CashBox_.COMPANY_BRANCH).get<Any>(CompanyBranch_.ID), cb.literal(filter.companyBranchId)))
        }
        if (!CollectionUtils.isEmpty(filter.companyBranchIds)) {
            allPredicates.add(cb.`in`<Any>(takingsRoot.get<Any>(Takings_.CASH_BOX).get<Any>(CashBox_.COMPANY_BRANCH).get<Any?>(CompanyBranch_.ID)).value(filter.companyBranchIds))
        }
        if (filter.currency != null) {
            allPredicates.add(cb.equal(takingsRoot.get<Any>(Takings_.CURRENCY), cb.literal(filter.currency)))
        }
        if (filter.date != null) {
            allPredicates.add(cb.equal(takingsRoot.get<Any>(Takings_.DATE), cb.literal(filter.date)))
        }
        if (filter.dateFrom != null) {
            allPredicates.add(cb.greaterThanOrEqualTo(takingsRoot.get(Takings_.DATE), cb.literal(filter.dateFrom)))
        }
        if (filter.dateTo != null) {
            allPredicates.add(cb.lessThanOrEqualTo(takingsRoot.get(Takings_.DATE), cb.literal(filter.dateTo)))
        }
        if (allPredicates.isNotEmpty()) {
            q.where(*allPredicates.toTypedArray())
        }
        q.orderBy(cb.desc(takingsRoot.get<Any>(Takings_.DATE)))
        return entityManager.createQuery(q).resultList
    }
}