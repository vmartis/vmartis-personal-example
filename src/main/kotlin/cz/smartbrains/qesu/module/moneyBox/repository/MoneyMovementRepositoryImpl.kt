package cz.smartbrains.qesu.module.moneyBox.repository

import cz.smartbrains.qesu.module.moneyBox.entity.MoneyBox_
import cz.smartbrains.qesu.module.moneyBox.entity.MoneyMovementCategory_
import cz.smartbrains.qesu.module.moneyBox.entity.MoneyMovement_
import cz.smartbrains.qesu.module.moneyBox.dto.MoneyMovementFilter
import cz.smartbrains.qesu.module.moneyBox.entity.MoneyBoxBalance
import cz.smartbrains.qesu.module.moneyBox.entity.MoneyMovement
import cz.smartbrains.qesu.module.moneyBox.type.AccountingType
import cz.smartbrains.qesu.module.moneyBox.type.MoneyMovementType
import cz.smartbrains.qesu.module.subject.entity.Subject_
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.function.Consumer
import java.util.stream.Collectors
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate

@Repository
class MoneyMovementRepositoryImpl : MoneyMovementRepositoryCustom {
    @PersistenceContext
    private val entityManager: EntityManager? = null
    override fun findByFilter(filter: MoneyMovementFilter): List<MoneyMovement> {
        val cb = entityManager!!.criteriaBuilder
        val q = cb.createQuery(MoneyMovement::class.java)
        val moneyMovement = q.from(MoneyMovement::class.java)
        moneyMovement.fetch<Any, Any>(MoneyMovement_.MONEY_BOX, JoinType.LEFT)
        moneyMovement.fetch<Any, Any>(MoneyMovement_.INVOICE, JoinType.LEFT)
        moneyMovement.fetch<Any, Any>(MoneyMovement_.CATEGORY, JoinType.LEFT)
        val allPredicates: MutableList<Predicate> = ArrayList()
        if (filter.moneyBoxId != null) {
            allPredicates.add(cb.equal(moneyMovement.get<Any>(MoneyMovement_.MONEY_BOX).get<Any>(MoneyBox_.ID), cb.literal(filter.moneyBoxId)))
        }
        if (filter.subjectId != null) {
            allPredicates.add(cb.equal(moneyMovement.get<Any>(MoneyMovement_.SUBJECT).get<Any>(Subject_.ID), cb.literal(filter.subjectId)))
        }
        if (filter.currency != null) {
            allPredicates.add(cb.equal(moneyMovement.get<Any>(MoneyMovement_.MONEY_BOX).get<Any>(MoneyBox_.CURRENCY), cb.literal(filter.currency)))
        }
        if (filter.active != null) {
            allPredicates.add(cb.equal(moneyMovement.get<Any>(MoneyMovement_.ACTIVE), cb.literal(filter.active)))
        }
        if (filter.dateFrom != null) {
            allPredicates.add(cb.greaterThanOrEqualTo(moneyMovement.get(MoneyMovement_.DATE), cb.literal(filter.dateFrom!!.atStartOfDay())))
        }
        if (filter.dateTo != null) {
            allPredicates.add(cb.lessThan(moneyMovement.get(MoneyMovement_.DATE), cb.literal(filter.dateTo!!.plus(1, ChronoUnit.DAYS).atStartOfDay())))
        }
        if (filter.taxableDateFrom != null) {
            allPredicates.add(cb.greaterThanOrEqualTo(moneyMovement.get(MoneyMovement_.TAXABLE_DATE), cb.literal(filter.taxableDateFrom)))
        }
        if (filter.taxableDateTo != null) {
            allPredicates.add(cb.lessThanOrEqualTo(moneyMovement.get(MoneyMovement_.TAXABLE_DATE), cb.literal(filter.taxableDateTo)))
        }
        if (filter.types != null && !filter.types!!.isEmpty()) {
            allPredicates.add(cb.or(*filter.types!!.stream().map { moneyMovementType: MoneyMovementType -> cb.equal(moneyMovement.get<Any>(MoneyMovement_.TYPE), cb.literal(moneyMovementType)) }.collect(Collectors.toList()).toTypedArray()))
        }
        if (filter.accountingTypes != null && filter.accountingTypes!!.isNotEmpty()) {
            allPredicates.add(cb.or(*filter.accountingTypes!!.stream().map { accountingType: AccountingType -> cb.equal(moneyMovement.get<Any>(MoneyMovement_.ACCOUNTING_TYPE), cb.literal(accountingType)) }.collect(Collectors.toList()).toTypedArray()))
        }
        if (filter.categories != null && filter.categories!!.isNotEmpty()) {
            allPredicates.add(cb.or(*filter.categories!!.stream()
                    .map { categoryId: Long -> if (categoryId == UNCATEGORIZED_ID) cb.isNull(moneyMovement.get<Any>(MoneyMovement_.CATEGORY)) else cb.equal(moneyMovement.get<Any>(MoneyMovement_.CATEGORY).get<Any>(MoneyMovementCategory_.ID), cb.literal(categoryId)) }.collect(Collectors.toList()).toTypedArray()))
        }
        q.orderBy(cb.desc(moneyMovement.get<Any>(MoneyMovement_.DATE)))
        if (!allPredicates.isEmpty()) {
            q.where(*allPredicates.toTypedArray())
        }
        return entityManager.createQuery(q).resultList
    }

    override fun computeBalancesForDate(forDate: LocalDate, currency: String): List<MoneyBoxBalance> {
        val cb = entityManager!!.criteriaBuilder
        val q = cb.createQuery(MoneyBoxBalance::class.java)
        val moneyMovement = q.from(MoneyMovement::class.java)
        q.multiselect(cb.sum(moneyMovement.get(MoneyMovement_.TOTAL_AMOUNT)), moneyMovement.get<Any>(MoneyMovement_.MONEY_BOX))
        q.groupBy(moneyMovement.get<Any>(MoneyMovement_.MONEY_BOX))
        val allPredicates: MutableList<Predicate> = ArrayList()
        allPredicates.add(cb.lessThan(moneyMovement.get(MoneyMovement_.DATE), cb.literal(forDate.plus(1, ChronoUnit.DAYS).atStartOfDay())))
        allPredicates.add(cb.equal(moneyMovement.get<Any>(MoneyMovement_.MONEY_BOX).get<Any>(MoneyBox_.CURRENCY), cb.literal(currency)))
        allPredicates.add(cb.equal(moneyMovement.get<Any>(MoneyMovement_.ACTIVE), cb.literal(true)))
        q.where(*allPredicates.toTypedArray())
        val balances = entityManager.createQuery(q).resultList
        balances.forEach(Consumer { moneyBoxBalance: MoneyBoxBalance -> moneyBoxBalance.date = forDate })
        return balances
    }

    companion object {
        const val UNCATEGORIZED_ID = -1L
    }
}