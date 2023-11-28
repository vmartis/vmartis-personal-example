package cz.smartbrains.qesu.module.order.controller

import cz.smartbrains.qesu.module.common.dto.validation.OnCreate
import cz.smartbrains.qesu.module.common.dto.validation.OnUpdate
import cz.smartbrains.qesu.module.order.dto.OrderItemTemplateDto
import cz.smartbrains.qesu.module.order.service.OrderItemTemplateService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.groups.Default

@RestController
@RequestMapping("/api/order-item-template")
class OrderItemTemplateController(private val service: OrderItemTemplateService) {
    @GetMapping
    fun list(@RequestParam(name = "company-id") companyId: Long): List<OrderItemTemplateDto> {
        return service.findAllByCompany(companyId)
    }

    @PostMapping
    fun create(@Validated(OnCreate::class, Default::class) @RequestBody orderItemTemplate: OrderItemTemplateDto): OrderItemTemplateDto {
        return service.create(orderItemTemplate)
    }

    @PutMapping
    fun update(@Validated(OnUpdate::class, Default::class) @RequestBody orderItemTemplate: OrderItemTemplateDto): OrderItemTemplateDto {
        return service.update(orderItemTemplate)
    }

    @DeleteMapping(path = ["/{id}"])
    fun delete(@PathVariable id: Long) {
        service.delete(id)
    }

    @PutMapping(path = ["/{id}/position/{position}"])
    fun updatePosition(@PathVariable id: Long, @PathVariable position: Int): OrderItemTemplateDto {
        return service.updatePosition(id, position)
    }
}