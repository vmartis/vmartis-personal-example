package cz.smartbrains.qesu.module.stock.movement.repository

import cz.smartbrains.qesu.module.stock.movement.entity.StockMovement
import cz.smartbrains.qesu.module.stock.movement.type.StockMovementType
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.math.BigInteger
import java.time.LocalDateTime
import java.util.*

interface StockMovementRepository : JpaRepository<StockMovement, Long>, StockMovementRepositoryCustom {
    @Query("SELECT m from StockMovement m left join m.stock s where s.id = :stockId and m.date >= :date")
    fun findOlderThen(stockId: Long, date: LocalDateTime): List<StockMovement>

    @Query("SELECT m from StockMovement m left join m.stock s where s.id = :stockId order by m.id desc")
    fun findLatest(stockId: Long, pageable: Pageable): List<StockMovement>

    @Query("SELECT MAX(m.number) FROM StockMovement m LEFT JOIN m.stock s WHERE s.id=:stockId AND m.mainType=:type AND m.date BETWEEN :dateFrom AND :dateTo")
    fun findLastByNumber(@Param("stockId") stockId: Long, @Param("type") type: StockMovementType, @Param("dateFrom") dateFrom: LocalDateTime, @Param("dateTo") dateTo: LocalDateTime): Optional<BigInteger>

    @Query("SELECT MAX(m.date) FROM StockMovement m LEFT JOIN m.stock s WHERE s.id=:stockId")
    fun findMaxDateByStock(@Param("stockId") stockId: Long): Optional<LocalDateTime>
}