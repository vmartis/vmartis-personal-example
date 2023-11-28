package cz.smartbrains.qesu.module.moneyBox.repository

import cz.smartbrains.qesu.module.moneyBox.entity.CashMoneyBox
import cz.smartbrains.qesu.module.moneyBox.entity.MoneyBoxBalance
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CashMoneyBoxRepository : JpaRepository<CashMoneyBox, Long> {
    @Query("SELECT new cz.smartbrains.qesu.module.moneyBox.entity.MoneyBoxBalance(SUM(m.totalAmount), SUM(m.amountCash), SUM(m.amountCheque)) " +
            "FROM CashMoneyMovement m LEFT JOIN m.moneyBox mb WHERE mb.id=:moneyBoxId AND m.active = TRUE")
    fun computeBalance(@Param("moneyBoxId") moneyBoxId: Long): MoneyBoxBalance
}