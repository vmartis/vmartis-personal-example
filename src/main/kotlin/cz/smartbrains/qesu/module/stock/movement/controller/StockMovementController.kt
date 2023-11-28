package cz.smartbrains.qesu.module.stock.movement.controller

import cz.smartbrains.qesu.module.stock.movement.dto.StockMovementDto
import cz.smartbrains.qesu.module.stock.movement.dto.StockMovementFilter
import cz.smartbrains.qesu.module.stock.movement.service.StockMovementService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/stock-movement")
class StockMovementController(private val service: StockMovementService) {
    @GetMapping
    fun list(@RequestParam(required = false, name = "stock-id") stockId: Long?,
             @RequestParam(required = false, name = "item-id") itemId: Long?,
             @RequestParam(required = false, name = "date-from") dateFrom: LocalDate?,
             @RequestParam(required = false, name = "date-to") dateTo: LocalDate?,
             @RequestParam(required = false, name = "item-batch-number") itemBatchNumber: String?): List<StockMovementDto> {
        return service.findByFilter(StockMovementFilter(stockId, itemId, dateFrom, dateTo, itemBatchNumber))
    }
}