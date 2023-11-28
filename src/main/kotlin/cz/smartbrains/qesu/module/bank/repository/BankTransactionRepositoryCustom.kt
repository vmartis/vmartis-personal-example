package cz.smartbrains.qesu.module.bank.repository

import cz.smartbrains.qesu.module.bank.dto.BankTransactionFilter
import cz.smartbrains.qesu.module.bank.entity.BankTransaction
import java.time.LocalDate

interface BankTransactionRepositoryCustom {
    fun findByFilter(filter: BankTransactionFilter): List<BankTransaction>

    /**
     * Find all bank transactions with empty movements ordered by date ASC.
     * @param dateFrom minimum date of transaction
     * @param limit max number of bank transactions
     * @return list of unmapped transactions.
     */
    fun findAllUnmapped(dateFrom: LocalDate, limit: Int): List<BankTransaction>
}