package cz.smartbrains.qesu.module.order.repository

import cz.smartbrains.qesu.module.order.entity.OrderStatement
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.math.BigInteger
import java.time.LocalDate
import java.util.*

interface OrderStatementRepository : JpaRepository<OrderStatement, Long>, OrderStatementRepositoryCustom {
    @Query("SELECT MAX(os.number) FROM OrderStatement os WHERE os.date BETWEEN ?1 AND ?2")
    fun findLastByNumber(nowYearStart: LocalDate, nowYearEnd: LocalDate): Optional<BigInteger>
}