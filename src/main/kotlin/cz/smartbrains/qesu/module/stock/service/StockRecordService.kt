package cz.smartbrains.qesu.module.stock.service

import java.time.LocalDateTime

interface StockRecordService {
    fun lastRecordDate(stockId: Long): LocalDateTime?
    fun validateDateForCreate(stockId: Long, date: LocalDateTime)
    fun validateDateForDelete(stockId: Long, date: LocalDateTime)

    /**
     * New record can be before edited movement, but must be after all others movements and inventories
     */
    fun validateDateForUpdate(stockId: Long, newDate: LocalDateTime)
}