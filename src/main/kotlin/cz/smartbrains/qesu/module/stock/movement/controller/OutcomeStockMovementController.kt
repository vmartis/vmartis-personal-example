package cz.smartbrains.qesu.module.stock.movement.controller

import cz.smartbrains.qesu.module.common.dto.validation.OnCreate
import cz.smartbrains.qesu.module.common.dto.validation.OnUpdate
import cz.smartbrains.qesu.module.stock.movement.dto.OutcomeStockMovementDto
import cz.smartbrains.qesu.module.stock.movement.dto.OutcomeStockMovementFilter
import cz.smartbrains.qesu.module.stock.movement.service.OutcomeStockMovementService
import cz.smartbrains.qesu.module.stock.movement.type.OutcomeStockMovementType
import cz.smartbrains.qesu.security.AlfaUserDetails
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.groups.Default

@RestController
@RequestMapping("/api/outcome-stock-movement")
class OutcomeStockMovementController(private val service: OutcomeStockMovementService) {
    @GetMapping(path = ["/{id}"])
    fun find(@PathVariable id: Long): ResponseEntity<OutcomeStockMovementDto> {
        val movement = service.find(id)
        return ResponseEntity.ok(movement)
    }

    @GetMapping
    fun list(@RequestParam(required = false, name = "stock-id") stockId: Long?,
             @RequestParam(required = false) type: OutcomeStockMovementType?,
             @RequestParam(required = false) unpaired: Boolean): List<OutcomeStockMovementDto> {
        return service.findByFilter(OutcomeStockMovementFilter(stockId, type, unpaired))
    }

    @PostMapping
    fun create(@Validated(OnCreate::class, Default::class) @RequestBody outcomeStockMovement: OutcomeStockMovementDto, @AuthenticationPrincipal user: AlfaUserDetails): OutcomeStockMovementDto {
        return service.create(outcomeStockMovement, user)
    }

    @PutMapping
    fun update(@Validated(OnUpdate::class, Default::class) @RequestBody outcomeStockMovement: OutcomeStockMovementDto, @AuthenticationPrincipal user: AlfaUserDetails): OutcomeStockMovementDto {
        return service.update(outcomeStockMovement, user)
    }

    @DeleteMapping(path = ["/{id}"])
    fun delete(@PathVariable id: @Valid Long) {
        service.delete(id)
    }
}