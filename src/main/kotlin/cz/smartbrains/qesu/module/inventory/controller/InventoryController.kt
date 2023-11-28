package cz.smartbrains.qesu.module.inventory.controller

import cz.smartbrains.qesu.module.common.dto.validation.OnCreate
import cz.smartbrains.qesu.module.common.dto.validation.OnUpdate
import cz.smartbrains.qesu.module.inventory.dto.InventoryDto
import cz.smartbrains.qesu.module.inventory.dto.InventoryFilter
import cz.smartbrains.qesu.module.inventory.service.InventoryService
import cz.smartbrains.qesu.security.AlfaUserDetails
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import javax.validation.Valid
import javax.validation.groups.Default

@RestController
@RequestMapping("/api/inventory")
class InventoryController(private val service: InventoryService) {
    @GetMapping(path = ["/{id}"])
    fun find(@PathVariable id: Long): ResponseEntity<InventoryDto> {
        val inventory = service.find(id)
        return ResponseEntity.ok(inventory)
    }

    @GetMapping
    fun list(@RequestParam(name = "stock-id") stockId: Long,
             @RequestParam(required = false, name = "date-from") dateFrom: LocalDate?,
             @RequestParam(required = false, name = "date-to") dateTo: LocalDate?,
             @RequestParam(required = false, name = "item-id") itemId: Long?,
             @RequestParam(required = false, name = "item-batch-number") itemBatchNumber: String?): List<InventoryDto> {
        return service.findByFilter(InventoryFilter(stockId, dateFrom, dateTo, itemId, itemBatchNumber))
    }

    @PostMapping
    fun create(@Validated(OnCreate::class, Default::class) @RequestBody inventory: InventoryDto, @AuthenticationPrincipal user: AlfaUserDetails): InventoryDto {
        return service.create(inventory, user)
    }

    @PutMapping
    fun update(@Validated(OnUpdate::class, Default::class) @RequestBody inventory: InventoryDto, @AuthenticationPrincipal user: AlfaUserDetails): InventoryDto {
        return service.update(inventory, user)
    }

    @DeleteMapping(path = ["/{id}"])
    fun delete(@PathVariable id: @Valid Long) {
        service.delete(id)
    }
}