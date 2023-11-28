package cz.smartbrains.qesu.module.stock.movement.repository

import cz.smartbrains.qesu.module.stock.entity.Stock_
import cz.smartbrains.qesu.module.stock.movement.dto.OutcomeStockMovementFilter
import cz.smartbrains.qesu.module.stock.movement.entity.IncomeStockMovement
import cz.smartbrains.qesu.module.stock.movement.entity.IncomeStockMovement_
import cz.smartbrains.qesu.module.stock.movement.entity.OutcomeStockMovement
import cz.smartbrains.qesu.module.stock.movement.entity.OutcomeStockMovement_
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate

@Repository
class OutcomeStockMovementRepositoryImpl : OutcomeStockMovementRepositoryCustom {
    @PersistenceContext
    private val entityManager: EntityManager? = null
    override fun findByFilter(filter: OutcomeStockMovementFilter): List<OutcomeStockMovement> {
        val cb = entityManager!!.criteriaBuilder
        val q = cb.createQuery(OutcomeStockMovement::class.java)
        val stockMovement = q.from(OutcomeStockMovement::class.java)
        stockMovement.fetch<Any, Any>(OutcomeStockMovement_.STOCK, JoinType.LEFT)
        stockMovement.fetch<Any, Any>(OutcomeStockMovement_.INCOME_MOVEMENT, JoinType.LEFT)
        stockMovement.fetch<Any, Any>(OutcomeStockMovement_.UPDATED_BY, JoinType.LEFT)
        stockMovement.fetch<Any, Any>(OutcomeStockMovement_.CREATED_BY, JoinType.LEFT)
        val allPredicates: MutableList<Predicate> = ArrayList()
        if (filter.stockId != null) {
            allPredicates.add(cb.equal(stockMovement.get<Any>(OutcomeStockMovement_.STOCK).get<Any>(Stock_.ID), cb.literal(filter.stockId)))
        }
        if (filter.type != null) {
            allPredicates.add(cb.equal(stockMovement.get<Any>(OutcomeStockMovement_.TYPE), cb.literal(filter.type)))
        }
        if (filter.unpaired) {
            val incomeMovementSubquery = q.subquery(IncomeStockMovement::class.java)
            val incomeMovement = incomeMovementSubquery.from(IncomeStockMovement::class.java)
            incomeMovementSubquery.select(incomeMovement)
                    .where(cb.equal(incomeMovement.get<Any>(IncomeStockMovement_.OUTCOME_MOVEMENT), stockMovement))
            allPredicates.add(cb.not(cb.exists(incomeMovementSubquery)))
        }
        q.orderBy(cb.desc(stockMovement.get<Any>(OutcomeStockMovement_.DATE)))
        if (allPredicates.isNotEmpty()) {
            q.where(*allPredicates.toTypedArray())
        }
        return entityManager.createQuery(q).resultList
    }
}