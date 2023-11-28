package cz.smartbrains.qesu.module.stock.controller

import cz.smartbrains.qesu.module.common.dto.validation.OnCreate
import cz.smartbrains.qesu.module.common.dto.validation.OnUpdate
import cz.smartbrains.qesu.module.stock.dto.StockDto
import cz.smartbrains.qesu.module.stock.service.StockService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.groups.Default

@RestController
@RequestMapping("/api/stock")
class StockController(private val service: StockService) {
    @GetMapping
    fun list(): List<StockDto> {
        return service.findAll()
    }

    @PostMapping
    fun create(@Validated(OnCreate::class, Default::class) @RequestBody stock: StockDto): StockDto {
        return service.create(stock)
    }

    @PutMapping
    fun update(@Validated(OnUpdate::class, Default::class) @RequestBody stock: StockDto): StockDto {
        return service.update(stock)
    }

    @PutMapping(path = ["/{id}/position/{position}"])
    fun updatePosition(@PathVariable id: Long, @PathVariable position: Int): StockDto {
        return service.updatePosition(id, position)
    }
}