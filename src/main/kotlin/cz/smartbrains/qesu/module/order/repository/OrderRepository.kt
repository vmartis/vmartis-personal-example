package cz.smartbrains.qesu.module.order.repository

import cz.smartbrains.qesu.module.order.entity.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.math.BigInteger
import java.time.LocalDate
import java.util.*

interface OrderRepository : JpaRepository<Order, Long>, OrderRepositoryCustom {
    @Query("SELECT MAX(o.number) FROM Order o WHERE o.date BETWEEN :dateFrom AND :dateTo")
    fun findLastByNumber(@Param("dateFrom") dateFrom: LocalDate, @Param("dateTo") dateTo: LocalDate): Optional<BigInteger>
}