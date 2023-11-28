package cz.smartbrains.qesu.module.order.service

import cz.smartbrains.qesu.module.common.exception.ServiceRuntimeException
import cz.smartbrains.qesu.module.order.repository.OrderStatementRepository
import org.springframework.stereotype.Service
import java.math.BigInteger
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@Service
class OrderStatementNumberFactoryImpl(private val repository: OrderStatementRepository) : OrderStatementNumberFactory {
    override fun nextNumber(year: Int): BigInteger {
        val date = LocalDate.now().withYear(year)
        val nowYearStart = date.with(TemporalAdjusters.firstDayOfYear())
        val nowYearEnd = date.with(TemporalAdjusters.lastDayOfYear())
        val currentNumber = repository.findLastByNumber(nowYearStart, nowYearEnd)
        return if (currentNumber.isPresent) {
            val newNumber = currentNumber.get().add(BigInteger.ONE)
            val maxNumber = BigInteger(String.format("%d%06d", date.year % 100, MAX_INDEX))
            if (newNumber.toInt() > maxNumber.toInt()) {
                throw ServiceRuntimeException(String.format("Maximum number (%d) of order statement exceeds.", MAX_INDEX))
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