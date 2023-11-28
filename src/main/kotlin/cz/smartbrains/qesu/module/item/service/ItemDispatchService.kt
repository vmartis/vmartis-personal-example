package cz.smartbrains.qesu.module.item.service

import cz.smartbrains.qesu.module.item.dto.ItemDispatchDto
import cz.smartbrains.qesu.module.item.dto.ItemDispatchFilter
import org.springframework.security.access.prepost.PreAuthorize

interface ItemDispatchService {
    @PreAuthorize("hasAnyAuthority('SETTING_ITEM', 'STOCK')")
    fun findByFilter(filter: ItemDispatchFilter): List<ItemDispatchDto>

    @PreAuthorize("hasAuthority('SETTING_ITEM')")
    fun create(itemDispatch: ItemDispatchDto): ItemDispatchDto

    @PreAuthorize("hasAuthority('SETTING_ITEM')")
    fun update(itemDispatch: ItemDispatchDto): ItemDispatchDto

    @PreAuthorize("hasAuthority('SETTING_ITEM')")
    fun delete(id: Long)
}