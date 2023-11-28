package cz.smartbrains.qesu.module.item.controller

import cz.smartbrains.qesu.module.item.dto.ItemBatchDto
import cz.smartbrains.qesu.module.item.dto.ItemBatchFilter
import cz.smartbrains.qesu.module.item.service.ItemBatchService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/item-batch")
class ItemBatchController(private val service: ItemBatchService) {
    @GetMapping
    fun list(@RequestParam(name = "item-id", required = false) itemId: Long?,
             @RequestParam(name = "stock-id", required = false) stockId: Long?,
             @RequestParam(name = "active-only", required = false, defaultValue = "false") activeOnly: Boolean): List<ItemBatchDto> {
        return service.findByFilter(ItemBatchFilter(itemId, stockId, activeOnly))
    }
}