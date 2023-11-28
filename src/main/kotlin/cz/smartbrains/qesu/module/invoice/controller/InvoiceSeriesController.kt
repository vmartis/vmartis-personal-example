package cz.smartbrains.qesu.module.invoice.controller

import cz.smartbrains.qesu.module.common.dto.validation.OnCreate
import cz.smartbrains.qesu.module.common.dto.validation.OnUpdate
import cz.smartbrains.qesu.module.invoice.dto.InvoiceSeriesDto
import cz.smartbrains.qesu.module.invoice.service.InvoiceSeriesService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.groups.Default

@RestController
@RequestMapping("/api/invoice-series")
class InvoiceSeriesController(private val service: InvoiceSeriesService) {
    @GetMapping
    fun list(): List<InvoiceSeriesDto> {
        return service.findAll()
    }

    @PostMapping
    fun create(@Validated(OnCreate::class, Default::class) @RequestBody invoiceSeries: InvoiceSeriesDto): InvoiceSeriesDto {
        return service.create(invoiceSeries)
    }

    @PutMapping
    fun update(@Validated(OnUpdate::class, Default::class) @RequestBody invoiceSeries: InvoiceSeriesDto): InvoiceSeriesDto {
        return service.update(invoiceSeries)
    }

    @PutMapping(path = ["/{id}/position/{position}"])
    fun updatePosition(@PathVariable id: Long, @PathVariable position: Int): InvoiceSeriesDto {
        return service.updatePosition(id, position)
    }
}