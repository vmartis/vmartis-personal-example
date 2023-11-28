package cz.smartbrains.qesu.module.item.repository

import cz.smartbrains.qesu.module.item.dto.ItemDispatchFilter
import cz.smartbrains.qesu.module.item.entity.ItemDispatch

interface ItemDispatchRepositoryCustom {
    fun findByFilter(filter: ItemDispatchFilter): List<ItemDispatch>
}