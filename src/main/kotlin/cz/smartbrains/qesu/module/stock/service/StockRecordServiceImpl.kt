package cz.smartbrains.qesu.module.stock.service

import cz.smartbrains.qesu.module.common.exception.ServiceRuntimeException
import cz.smartbrains.qesu.module.inventory.repository.InventoryRepository
import cz.smartbrains.qesu.module.stock.movement.repository.StockMovementRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*
import java.util.stream.Stream

@Service
class StockRecordServiceImpl(private val stockMovementRepository: StockMovementRepository,
                             private val inventoryRepository: InventoryRepository) : StockRecordService {
    override fun lastRecordDate(stockId: Long): LocalDateTime? {
        val maxMovementDate = stockMovementRepository.findMaxDateByStock(stockId)
        val maxInventoryDate = inventoryRepository.findMaxDateByStock(stockId)
        return Stream.of(maxMovementDate, maxInventoryDate)
                .filter { obj: Optional<LocalDateTime> -> obj.isPresent }
                .map { obj: Optional<LocalDateTime> -> obj.get() }
                .max { obj: LocalDateTime, other: LocalDateTime? -> obj.compareTo(other) }
                .orElse(LocalDateTime.MIN)
    }

    override fun validateDateForCreate(stockId: Long, date: LocalDateTime) {
        val maxDate = lastRecordDate(stockId)
        if (!date.isAfter(maxDate)) {
            throw ServiceRuntimeException("stock.record.date.invalid")
        }
    }

    override fun validateDateForDelete(stockId: Long, date: LocalDateTime) {
        val maxDate = lastRecordDate(stockId)
        if (date != maxDate) {
            throw ServiceRuntimeException("stock.record.date.invalid")
        }
    }

    override fun validateDateForUpdate(stockId: Long, newDate: LocalDateTime) {
        val lastMovement = stockMovementRepository.findOlderThen(stockId, newDate)
        // for update dates of 2 last movements to be checked, because last movement is movement which is persisted
        if (lastMovement.size > 1) {
            throw ServiceRuntimeException("stock.record.date.invalid")
        }
        val inventories = inventoryRepository.findOlderThen(stockId, newDate)
        if (!inventories.isEmpty()) {
            throw ServiceRuntimeException("stock.record.date.invalid")
        }
    }
}