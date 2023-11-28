package cz.smartbrains.qesu.module.moneyBox.controller

import cz.smartbrains.qesu.module.moneyBox.dto.MoneyBoxBalanceDto
import cz.smartbrains.qesu.module.moneyBox.dto.MoneyBoxDto
import cz.smartbrains.qesu.module.moneyBox.service.MoneyBoxService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/money-box")
class MoneyBoxController(private val service: MoneyBoxService) {
    @GetMapping(path = ["/{id}"])
    fun find(@PathVariable id: Long): ResponseEntity<MoneyBoxDto> {
        val invoice = service.find(id)
        return ResponseEntity.ok(invoice)
    }

    @GetMapping
    fun list(): List<MoneyBoxDto> {
        return service.findAll()
    }

    @GetMapping(path = ["/balances"])
    fun balances(@RequestParam dateTo: LocalDate, @RequestParam currency: String): List<MoneyBoxBalanceDto> {
        return service.balances(dateTo, currency)
    }

    @PutMapping(path = ["/{id}/position/{position}"])
    fun updatePosition(@PathVariable id: Long, @PathVariable position: Int): MoneyBoxDto {
        return service.updatePosition(id, position)
    }
}