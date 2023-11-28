package cz.smartbrains.qesu.module.order.controller

import cz.smartbrains.qesu.module.common.dto.validation.OnCreate
import cz.smartbrains.qesu.module.order.dto.OrderDto
import cz.smartbrains.qesu.module.order.dto.OrderFilter
import cz.smartbrains.qesu.module.order.service.OrderService
import cz.smartbrains.qesu.module.order.type.OrderState
import cz.smartbrains.qesu.security.AlfaUserDetails
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import javax.validation.groups.Default

@RestController
@RequestMapping("/api/order")
class OrderController(private val service: OrderService) {
    @GetMapping(path = ["/{id}"])
    fun find(@PathVariable id: Long): ResponseEntity<OrderDto> {
        val order = service.find(id)
        return if (order == null) ResponseEntity.notFound().build() else ResponseEntity.ok(order)
    }

    @GetMapping
    fun findAll(@RequestParam(value = "supplier-branch-id", required = false) supplierBranchId: Long?,
                @RequestParam(value = "subscriber-id", required = false) subscriberId: Long?,
                @RequestParam(value = "region-id", required = false) regionId: Long?,
                @RequestParam(value = "no-statement", required = false) noStatement: Boolean?,
                @RequestParam(value = "currency", required = false) currency: String?,
                @RequestParam(value = "state", required = false) state: OrderState?,
                @RequestParam(value = "date", required = false) date: LocalDate?,
                @RequestParam(value = "date-from", required = false) dateFrom: LocalDate?,
                @RequestParam(value = "date-to", required = false) dateTo: LocalDate?): List<OrderDto> {
        return service.findAll(OrderFilter(supplierBranchId,
                subscriberId,
                regionId,
                noStatement,
                currency,
                state,
                date,
                dateFrom,
                dateTo))
    }

    @PostMapping
    fun create(@Validated(OnCreate::class, Default::class) @RequestBody item: OrderDto,
               @AuthenticationPrincipal userDetails: AlfaUserDetails): OrderDto {
        return service.create(item, userDetails)
    }

    // Default.class can be applied because of cascading validation of items
    @PutMapping
    fun update(@Validated(Default::class) @RequestBody order: OrderDto,
               @AuthenticationPrincipal userDetails: AlfaUserDetails): OrderDto {
        return service.update(order, userDetails)
    }

    // Default.class can be applied because of cascading validation of items
    @PutMapping(path = ["/bulk"])
    fun update(@Validated(Default::class) @RequestBody orders: Set<OrderDto>,
               @AuthenticationPrincipal userDetails: AlfaUserDetails) {
        service.updateBulk(orders, userDetails)
    }

    @DeleteMapping(path = ["/{id}"])
    fun delete(@PathVariable id: Long) {
        service.delete(id)
    }
}