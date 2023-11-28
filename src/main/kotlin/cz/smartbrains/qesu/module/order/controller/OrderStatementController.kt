package cz.smartbrains.qesu.module.order.controller

import cz.smartbrains.qesu.module.common.dto.validation.OnCreate
import cz.smartbrains.qesu.module.common.dto.validation.OnUpdate
import cz.smartbrains.qesu.module.order.dto.OrderStatementDto
import cz.smartbrains.qesu.module.order.dto.OrderStatementFilter
import cz.smartbrains.qesu.module.order.service.OrderStatementService
import cz.smartbrains.qesu.security.AlfaUserDetails
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import javax.validation.groups.Default

@RestController
@RequestMapping("/api/order-statement")
class OrderStatementController(private val service: OrderStatementService) {
    @GetMapping(path = ["/{id}"])
    fun find(@PathVariable id: Long): ResponseEntity<OrderStatementDto> {
        val orderStatement = service.find(id)
        return ResponseEntity.ok(orderStatement)
    }

    @GetMapping
    fun findAll(@RequestParam(value = "payment-type-id", required = false) paymentTypeId: Long?,
                @RequestParam(value = "subscriber-id", required = false) subscriberId: Long?,
                @RequestParam(value = "date-from", required = false) dateFrom: LocalDate?,
                @RequestParam(value = "date-to", required = false) dateTo: LocalDate?): List<OrderStatementDto> {
        return service.findAll(OrderStatementFilter(dateFrom, dateTo, paymentTypeId, subscriberId))
    }

    @PostMapping
    fun create(@Validated(OnCreate::class, Default::class) @RequestBody item: OrderStatementDto,
               @AuthenticationPrincipal userDetails: AlfaUserDetails): OrderStatementDto {
        return service.create(item, userDetails)
    }

    @PutMapping
    fun update(@Validated(OnUpdate::class, Default::class) @RequestBody orderStatementDto: OrderStatementDto,
               @AuthenticationPrincipal userDetails: AlfaUserDetails): OrderStatementDto {
        return service.update(orderStatementDto, userDetails)
    }

    @DeleteMapping(path = ["/{id}"])
    fun delete(@PathVariable id: Long) {
        service.delete(id)
    }
}