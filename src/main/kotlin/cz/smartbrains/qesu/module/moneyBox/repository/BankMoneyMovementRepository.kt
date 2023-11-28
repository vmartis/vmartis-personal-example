package cz.smartbrains.qesu.module.moneyBox.repository

import cz.smartbrains.qesu.module.moneyBox.entity.BankMoneyMovement
import cz.smartbrains.qesu.module.subject.entity.Subject
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface BankMoneyMovementRepository : JpaRepository<BankMoneyMovement, Long> {
    @Query("SELECT bmm FROM BankMoneyMovement bmm LEFT JOIN bmm.bankTransaction bt " +
            "WHERE bmm.correspondingBankAccount IS NULL AND ((bt.correspondingAccountNumber=:accountId AND bt.correspondingBankId=:bankId) OR bt.correspondingAccountNumber=:iban)")
    fun findUnpairedByAccountNumber(accountId: String, bankId: String, iban: String?): List<BankMoneyMovement>

    @Modifying
    @Query("UPDATE BankMoneyMovement bmm SET bmm.correspondingBankAccount = null, bmm.subject = null where bmm.correspondingBankAccount.id=:bankAccountId")
    fun unpairBankAccount(@Param("bankAccountId") bankAccountId: Long): Int

    @Modifying
    @Query("UPDATE BankMoneyMovement bmm SET bmm.subject = :subject where bmm.correspondingBankAccount.id=:bankAccountId")
    fun updateSubjectForCorrespondingAccount(@Param("bankAccountId") bankAccountId: Long, @Param("subject") subject: Subject): Int
}