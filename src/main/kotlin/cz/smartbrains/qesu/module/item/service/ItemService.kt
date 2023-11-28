package cz.smartbrains.qesu.module.item.service

import cz.smartbrains.qesu.module.item.dto.ItemDto
import cz.smartbrains.qesu.module.item.dto.ItemFilter
import org.springframework.security.access.prepost.PreAuthorize

interface ItemService {
    @PreAuthorize("hasAnyAuthority('SETTING_ITEM', 'COMPANY_PARTNER', 'REGULAR_SALE', 'STOCK')")
    fun findByFilter(filter: ItemFilter): List<ItemDto>

    @PreAuthorize("hasAuthority('SETTING_ITEM')")
    fun find(id: Long): ItemDto

    @PreAuthorize("hasAuthority('SETTING_ITEM')")
    fun create(item: ItemDto): ItemDto

    @PreAuthorize("hasAuthority('SETTING_ITEM')")
    fun update(item: ItemDto): ItemDto
}