package cz.smartbrains.qesu.module.inventory.service

import cz.smartbrains.qesu.module.common.exception.ServiceRuntimeException
import cz.smartbrains.qesu.module.inventory.repository.InventoryRepository
import org.springframework.stereotype.Service
import java.math.BigInteger
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters

@Service
class InventoryNumberFactoryImpl(private val inventoryRepository: InventoryRepository) : InventoryNumberFactory {
    override fun nextNumber(stockId: Long, year: Int): BigInteger {
        val date = LocalDateTime.now().withYear(year)
        val nowYearStart = date.with(TemporalAdjusters.firstDayOfYear()).with(LocalTime.MIN)
        val nowYearEnd = date.with(TemporalAdjusters.lastDayOfYear()).with(LocalTime.MAX)
        val currentNumber = inventoryRepository.findLastByNumber(stockId, nowYearStart, nowYearEnd)
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