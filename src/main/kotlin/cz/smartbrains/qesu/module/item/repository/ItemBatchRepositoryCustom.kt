package cz.smartbrains.qesu.module.item.repository

import cz.smartbrains.qesu.module.item.dto.ItemBatchFilter
import cz.smartbrains.qesu.module.item.entity.ItemBatch

interface ItemBatchRepositoryCustom {
    fun findByFilter(filter: ItemBatchFilter): List<ItemBatch>
}