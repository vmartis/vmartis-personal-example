package cz.smartbrains.qesu.module.moneyBox.repository

import cz.smartbrains.qesu.module.common.repository.OrderableEntityRepository
import cz.smartbrains.qesu.module.moneyBox.entity.MoneyBox
import org.springframework.data.jpa.repository.Query

interface MoneyBoxRepository : OrderableEntityRepository<MoneyBox> {
    @Query("SELECT mb from MoneyBox mb WHERE mb.active=TRUE")
    fun findAllActive(): List<MoneyBox>

    @Query("SELECT mb from MoneyBox mb WHERE mb.active=TRUE and mb.currency=?1")
    fun findAllActiveByCurrency(currency: String): List<MoneyBox>
}