package cz.smartbrains.qesu.module.bank.repository

import cz.smartbrains.qesu.module.bank.entity.BankTransaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface BankTransactionRepository : JpaRepository<BankTransaction?, Long?>, BankTransactionRepositoryCustom {
    @Query("SELECT COUNT(bt) from BankTransaction bt where bt.account.id=:bankAccountId AND bt.transactionId=:transactionId")
    fun countBy(@Param("bankAccountId") bankAccountId: Long, @Param("transactionId") transactionId: String?): Int
}