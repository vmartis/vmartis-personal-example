package cz.smartbrains.qesu.module.moneyBox.repository

import cz.smartbrains.qesu.module.moneyBox.entity.BankMoneyBox
import cz.smartbrains.qesu.module.moneyBox.entity.MoneyBoxBalance
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface BankMoneyBoxRepository : JpaRepository<BankMoneyBox, Long> {
    fun findByBankAccountId(bankAccountId: Long): Optional<BankMoneyBox>

    @Query("SELECT new cz.smartbrains.qesu.module.moneyBox.entity.MoneyBoxBalance(SUM(m.totalAmount)) " +
            "FROM BankMoneyMovement m LEFT JOIN m.moneyBox mb WHERE mb.id=:moneyBoxId  AND m.active = TRUE")
    fun computeBalance(@Param("moneyBoxId") moneyBoxId: Long): MoneyBoxBalance
}