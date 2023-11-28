package cz.smartbrains.qesu.module.invoice.controller

import cz.smartbrains.qesu.module.common.dto.validation.OnCreate
import cz.smartbrains.qesu.module.invoice.dto.InvoiceDto
import cz.smartbrains.qesu.module.invoice.dto.InvoiceFilter
import cz.smartbrains.qesu.module.invoice.service.InvoiceService
import cz.smartbrains.qesu.module.invoice.type.InvoiceStatus
import cz.smartbrains.qesu.module.invoice.type.InvoiceType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import javax.validation.Valid
import javax.validation.groups.Default

@RestController
@RequestMapping("/api/invoice")
class InvoiceController(private val service: InvoiceService) {
    @GetMapping(path = ["/{id}"])
    fun find(@PathVariable id: Long): ResponseEntity<InvoiceDto> {
        val invoice = service.find(id)
        return ResponseEntity.ok(invoice)
    }

    @GetMapping
    fun list(@RequestParam(name = "subscriber-id", required = false) subscriberId: Long?,
             @RequestParam(required = false) dateOfIssueFrom: LocalDate?,
             @RequestParam(required = false) dateOfIssueTo: LocalDate?,
             @RequestParam(required = false) type: InvoiceType?,
             @RequestParam(required = false) status: InvoiceStatus?,
             @RequestParam(required = false) excludeId: Long?,
             @RequestParam(required = false) currency: String?,
             @RequestParam(required = false) limit: Int?): List<InvoiceDto> {
        val filter = InvoiceFilter(subscriberId, dateOfIssueFrom, dateOfIssueTo, type, status, limit, currency, excludeId)
        return service.findAll(filter)
    }

    @PostMapping
    fun create(@Validated(OnCreate::class, Default::class) @RequestBody invoice: InvoiceDto): InvoiceDto {
        return service.create(invoice)
    }

    // OnUpdate validation need to be disabled, because is not possible to update invoice with new invoice item
    @PutMapping
    fun update(@Validated(Default::class) @RequestBody invoice: InvoiceDto): InvoiceDto {
        return service.update(invoice)
    }

    @DeleteMapping(path = ["/{id}"])
    fun delete(@PathVariable id: @Valid Long) {
        service.delete(id)
    }
}