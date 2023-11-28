package cz.smartbrains.qesu.module.moneyBox.repository

import cz.smartbrains.qesu.module.moneyBox.dto.MoneyMovementFilter
import cz.smartbrains.qesu.module.moneyBox.entity.MoneyBoxBalance
import cz.smartbrains.qesu.module.moneyBox.entity.MoneyMovement
import java.time.LocalDate

interface MoneyMovementRepositoryCustom {
    fun findByFilter(filter: MoneyMovementFilter): List<MoneyMovement>
    fun computeBalancesForDate(forDate: LocalDate, currency: String): List<MoneyBoxBalance>
}