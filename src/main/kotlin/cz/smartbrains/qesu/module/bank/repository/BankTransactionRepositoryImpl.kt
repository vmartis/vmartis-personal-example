package cz.smartbrains.qesu.module.bank.repository

import cz.smartbrains.qesu.module.bank.dto.BankTransactionFilter
import cz.smartbrains.qesu.module.bank.entity.BankTransaction
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate

@Repository
class BankTransactionRepositoryImpl : BankTransactionRepositoryCustom {
    @PersistenceContext
    private val entityManager: EntityManager? = null

    override fun findByFilter(filter: BankTransactionFilter): List<BankTransaction> {
        val cb = entityManager!!.criteriaBuilder
        val q = cb.createQuery(BankTransaction::class.java)
        val transaction = q.from(BankTransaction::class.java)
        transaction.join<Any, Any>("account", JoinType.LEFT)
        val allPredicates: MutableList<Predicate> = ArrayList()
        allPredicates.add(cb.equal(transaction.get<Any>("account").get<Any>("id"), cb.literal(filter.bankAccountId)))
        if (filter.dateFrom != null) {
            allPredicates.add(cb.greaterThanOrEqualTo(transaction.get("date"), cb.literal(filter.dateFrom)))
        }
        if (filter.dateTo != null) {
            allPredicates.add(cb.lessThanOrEqualTo(transaction.get("date"), cb.literal(filter.dateTo)))
        }
        q.orderBy(cb.desc(transaction.get<Any>("date")))
        if (!allPredicates.isEmpty()) {
            q.where(*allPredicates.toTypedArray())
        }
        return entityManager.createQuery(q).resultList
    }

    override fun findAllUnmapped(dateFrom: LocalDate, limit: Int): List<BankTransaction> {
        return entityManager!!
                .createQuery("SELECT bt FROM BankTransaction bt WHERE bt.date >= :dateFrom AND bt.movements IS EMPTY ORDER BY bt.date", BankTransaction::class.java)
                .setParameter("dateFrom", dateFrom)
                .setMaxResults(limit)
                .resultList
    }
}