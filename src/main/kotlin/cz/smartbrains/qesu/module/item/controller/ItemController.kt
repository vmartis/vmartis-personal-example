package cz.smartbrains.qesu.module.item.controller

import cz.smartbrains.qesu.module.common.dto.validation.OnCreate
import cz.smartbrains.qesu.module.common.dto.validation.OnUpdate
import cz.smartbrains.qesu.module.item.dto.ItemDto
import cz.smartbrains.qesu.module.item.dto.ItemFilter
import cz.smartbrains.qesu.module.item.service.ItemService
import cz.smartbrains.qesu.module.item.type.ItemDeterminationType
import cz.smartbrains.qesu.module.item.type.ItemOriginType
import cz.smartbrains.qesu.module.item.type.ItemType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.groups.Default

@RestController
@RequestMapping("/api/item")
class ItemController(private val service: ItemService) {

    @GetMapping
    fun list(@RequestParam(name = "type", required = false) type: ItemType?,
             @RequestParam(name = "origin", required = false) origin: ItemOriginType?,
             @RequestParam(name = "determination", required = false) determination: ItemDeterminationType?,
             @RequestParam(name = "categories[]", required = false) categories: List<Long>? = ArrayList(),
             @RequestParam(name = "batch-evidence", required = false) batchEvidence: Boolean?,
             @RequestParam(name = "active-only", required = false, defaultValue = "false") activeOnly: Boolean): List<ItemDto> {
        return service.findByFilter(ItemFilter(type, origin, determination, categories?:ArrayList(), batchEvidence, activeOnly))
    }

    @GetMapping(path = ["/{id}"])
    fun find(@PathVariable id: Long): ResponseEntity<ItemDto> {
        val item = service.find(id)
        return ResponseEntity.ok(item)
    }

    @PostMapping
    fun create(@Validated(OnCreate::class, Default::class) @RequestBody item: ItemDto): ItemDto {
        return service.create(item)
    }

    @PutMapping
    fun update(@Validated(OnUpdate::class, Default::class) @RequestBody item: ItemDto): ItemDto {
        return service.update(item)
    }
}