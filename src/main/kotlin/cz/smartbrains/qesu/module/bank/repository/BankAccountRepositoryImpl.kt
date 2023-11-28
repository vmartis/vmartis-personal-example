package cz.smartbrains.qesu.module.bank.repository

import cz.smartbrains.qesu.module.bank.entity.BankAccount_
import cz.smartbrains.qesu.module.company.entity.Company
import cz.smartbrains.qesu.module.company.entity.Company_
import cz.smartbrains.qesu.module.company.type.CompanyRole
import cz.smartbrains.qesu.module.subject.entity.Subject
import cz.smartbrains.qesu.module.subject.entity.Subject_
import cz.smartbrains.qesu.module.bank.dto.BankAccountFilter
import cz.smartbrains.qesu.module.bank.entity.BankAccount
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.Join
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate

@Repository
@Suppress("UNCHECKED_CAST")
class BankAccountRepositoryImpl : BankAccountRepositoryCustom {
    @PersistenceContext
    private val entityManager: EntityManager? = null

    override fun findByFilter(filter: BankAccountFilter): List<BankAccount> {
        val cb = entityManager!!.criteriaBuilder
        val q = cb.createQuery(BankAccount::class.java)
        val bankAccount = q.from(BankAccount::class.java)
        val subject = bankAccount.fetch<BankAccount, Subject>(BankAccount_.SUBJECT, JoinType.LEFT) as Join<BankAccount, Subject> //need to type to Join because of getting attribute later
        q.distinct(true)
        val allPredicates: MutableList<Predicate> = ArrayList()
        if (filter.subjectId != null) {
            allPredicates.add(cb.equal(subject, cb.literal(filter.subjectId)))
        }
        if (filter.ownershipped) {
            val companySubquery = q.subquery(Company::class.java)
            val company = companySubquery.from(Company::class.java)
            companySubquery.select(company)
                    .where(cb.equal(company.get<Any>(Company_.ID), subject.get<Any>(Subject_.ID)), cb.equal(company.get<Any>(Company_.ROLE), CompanyRole.OWNED))
            allPredicates.add(cb.exists(companySubquery))
        }
        if (allPredicates.size > 0) {
            q.where(*allPredicates.toTypedArray())
        }
        q.orderBy(cb.asc(bankAccount.get<Any>(BankAccount_.NAME)))
        return entityManager.createQuery(q).resultList
    }
}