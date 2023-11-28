package cz.smartbrains.qesu.module.moneyBox.repository

import cz.smartbrains.qesu.module.moneyBox.entity.MoneyMovement
import cz.smartbrains.qesu.module.moneyBox.type.MoneyMovementType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

interface MoneyMovementRepository : JpaRepository<MoneyMovement, Long>, MoneyMovementRepositoryCustom {
    @Query("SELECT SUM(m.totalVat) FROM MoneyMovement m WHERE m.taxableDate BETWEEN ?1 AND ?2 AND m.type=?3 AND m.invoice IS NULL AND m.moneyBox.currency = ?4 AND m.active = TRUE")
    fun sumVatNoInvoice(dateFrom: LocalDate, to: LocalDate, type: MoneyMovementType, currency: String): Optional<BigDecimal>

    @Query("SELECT SUM(m.totalWithoutVat) FROM MoneyMovement m WHERE m.taxableDate BETWEEN ?1 AND ?2 AND m.type = ?3 AND m.invoice IS NULL AND m.moneyBox.currency = ?4 AND m.accountingType='FISCAL' AND m.active = TRUE")
    fun sumAmountNoInvoice(dateFrom: LocalDate, to: LocalDate, type: MoneyMovementType, currency: String): Optional<BigDecimal>

    @Query("SELECT SUM(m.totalWithoutVat) FROM MoneyMovement m where m.taxableDate BETWEEN ?1 AND ?2 AND m.type=?3 AND m.moneyBox.currency = ?4 AND m.accountingType='FISCAL' AND m.active = TRUE")
    fun sumAmount(dateFrom: LocalDate, to: LocalDate, type: MoneyMovementType, currency: String): Optional<BigDecimal>
}