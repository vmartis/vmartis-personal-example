package cz.smartbrains.qesu.module.stock.movement.controller

import cz.smartbrains.qesu.module.common.dto.validation.OnCreate
import cz.smartbrains.qesu.module.common.dto.validation.OnUpdate
import cz.smartbrains.qesu.module.stock.movement.dto.IncomeStockMovementDto
import cz.smartbrains.qesu.module.stock.movement.service.IncomeStockMovementService
import cz.smartbrains.qesu.security.AlfaUserDetails
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.groups.Default

@RestController
@RequestMapping("/api/income-stock-movement")
class IncomeStockMovementController(private val incomeStockMovementService: IncomeStockMovementService) {
    @GetMapping(path = ["/{id}"])
    fun find(@PathVariable id: Long): ResponseEntity<IncomeStockMovementDto> {
        val movement = incomeStockMovementService.find(id)
        return ResponseEntity.ok(movement)
    }

    @PostMapping
    fun create(@Validated(OnCreate::class, Default::class) @RequestBody incomeStockMovement: IncomeStockMovementDto, @AuthenticationPrincipal user: AlfaUserDetails): IncomeStockMovementDto {
        return incomeStockMovementService.create(incomeStockMovement, user)
    }

    @PutMapping
    fun update(@Validated(OnUpdate::class, Default::class) @RequestBody incomeStockMovement: IncomeStockMovementDto, @AuthenticationPrincipal user: AlfaUserDetails): IncomeStockMovementDto {
        return incomeStockMovementService.update(incomeStockMovement, user)
    }

    @DeleteMapping(path = ["/{id}"])
    fun delete(@PathVariable id: @Valid Long) {
        incomeStockMovementService.delete(id)
    }
}