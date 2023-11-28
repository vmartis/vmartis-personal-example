package cz.smartbrains.qesu.module.stock.movement.repository

import cz.smartbrains.qesu.module.common.exception.ServiceRuntimeException
import cz.smartbrains.qesu.module.stock.movement.type.StockMovementType
import org.springframework.stereotype.Service
import java.math.BigInteger
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters

@Service
class StockMovementNumberFactoryImpl(private val stockMovementRepository: StockMovementRepository) : StockMovementNumberFactory {
    override fun nextNumber(type: StockMovementType, stockId: Long, year: Int): BigInteger {
        val date = LocalDateTime.now().withYear(year)
        val nowYearStart = date.with(TemporalAdjusters.firstDayOfYear()).with(LocalTime.MIN)
        val nowYearEnd = date.with(TemporalAdjusters.lastDayOfYear()).with(LocalTime.MAX)
        val currentNumber = stockMovementRepository.findLastByNumber(stockId, type, nowYearStart, nowYearEnd)
        return if (currentNumber.isPresent) {
            val newNumber = currentNumber.get().add(BigInteger.ONE)
            val maxNumber = BigInteger(String.format("%d%06d", date.year % 100, MAX_INDEX))
            if (newNumber.toInt() > maxNumber.toInt()) {
                throw ServiceRuntimeException(String.format("Maximum number (%d) of stock movement  exceeds.", MAX_INDEX))
            }
            newNumber
        } else {
            BigInteger(String.format("%d%06d", date.year % 100, DEFAULT_NUMBER_INDEX))
        }
    }

    companion object {
        private val DEFAULT_NUMBER_INDEX = BigInteger.ONE
        const val MAX_INDEX = 999999
    }
}