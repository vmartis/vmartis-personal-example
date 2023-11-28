package cz.smartbrains.qesu.module.stock.controller

import cz.smartbrains.qesu.module.common.dto.validation.OnCreate
import cz.smartbrains.qesu.module.common.dto.validation.OnUpdate
import cz.smartbrains.qesu.module.stock.dto.StockItemDto
import cz.smartbrains.qesu.module.stock.dto.StockItemFilter
import cz.smartbrains.qesu.module.stock.service.StockItemService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.groups.Default

@RestController
@RequestMapping("/api/stock-item")
class StockItemController(private val service: StockItemService) {
    @GetMapping(path = ["/{id}"])
    fun find(@PathVariable id: Long): ResponseEntity<StockItemDto> {
        val stockItem = service.find(id)
        return ResponseEntity.ok(stockItem)
    }

    @GetMapping
    fun list(@RequestParam(value = "stock-id", required = false) stockId: Long?,
             @RequestParam(value = "stock-ids[]", required = false) stockIds: List<Long>?,
             @RequestParam(value = "active", required = false) activeOnly: Boolean = false,
             @RequestParam(value = "item-id", required = false) itemId: Long?): List<StockItemDto> {
        return service.findAll(StockItemFilter(stockId, stockIds, itemId, activeOnly))
    }

    @PostMapping
    fun create(@Validated(OnCreate::class, Default::class) @RequestBody stockItem: StockItemDto): StockItemDto {
        return service.create(stockItem)
    }

    @PutMapping
    fun update(@Validated(OnUpdate::class, Default::class) @RequestBody stockItem: StockItemDto): StockItemDto {
        return service.update(stockItem)
    }
}