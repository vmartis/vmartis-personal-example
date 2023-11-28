package cz.smartbrains.qesu.module.item.repository

import cz.smartbrains.qesu.module.item.dto.ItemFilter
import cz.smartbrains.qesu.module.item.entity.Item

interface ItemRepositoryCustom {
    fun findByFilter(filter: ItemFilter): List<Item>
}