package cz.smartbrains.qesu.module.item.controller

import cz.smartbrains.qesu.module.common.dto.validation.OnCreate
import cz.smartbrains.qesu.module.common.dto.validation.OnUpdate
import cz.smartbrains.qesu.module.item.dto.ItemDispatchDto
import cz.smartbrains.qesu.module.item.dto.ItemDispatchFilter
import cz.smartbrains.qesu.module.item.service.ItemDispatchService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.groups.Default

@RestController
@RequestMapping("/api/item-dispatch")
class ItemDispatchController(private val service: ItemDispatchService) {
    @GetMapping
    fun list(@RequestParam(name = "item-id", required = false) itemId: Long?): List<ItemDispatchDto> {
        return service.findByFilter(ItemDispatchFilter(itemId))
    }

    @PostMapping
    fun create(@Validated(OnCreate::class, Default::class) @RequestBody itemDispatch: ItemDispatchDto): ItemDispatchDto {
        return service.create(itemDispatch)
    }

    @PutMapping
    fun update(@Validated(OnUpdate::class, Default::class) @RequestBody itemDispatch: ItemDispatchDto): ItemDispatchDto {
        return service.update(itemDispatch)
    }

    @DeleteMapping(path = ["/{id}"])
    fun delete(@PathVariable id: @Valid Long) {
        service.delete(id)
    }
}