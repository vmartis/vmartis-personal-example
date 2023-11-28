package cz.smartbrains.qesu.module.inventory.repository

import cz.smartbrains.qesu.module.inventory.entity.Inventory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.math.BigInteger
import java.time.LocalDateTime
import java.util.*

interface InventoryRepository : JpaRepository<Inventory, Long>, InventoryRepositoryCustom {
    @Query("SELECT MAX(i.number) FROM Inventory i LEFT JOIN i.stock s WHERE s.id=:stockId AND i.date BETWEEN :dateFrom AND :dateTo")
    fun findLastByNumber(@Param("stockId") stockId: Long, @Param("dateFrom") dateFrom: LocalDateTime, @Param("dateTo") dateTo: LocalDateTime): Optional<BigInteger>
    fun findTopByOrderByIdDesc(): Optional<Inventory>

    @Query("SELECT MAX(i.date) FROM Inventory i LEFT JOIN i.stock s WHERE s.id=:stockId")
    fun findMaxDateByStock(@Param("stockId") stockId: Long): Optional<LocalDateTime>

    @Query("SELECT i from Inventory i left join i.stock s where s.id = :stockId and i.date >= :date")
    fun findOlderThen(stockId: Long, date: LocalDateTime): List<Inventory>
}