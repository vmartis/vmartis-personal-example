package cz.smartbrains.qesu.module.item.dto

import cz.smartbrains.qesu.module.item.type.ItemDeterminationType
import cz.smartbrains.qesu.module.item.type.ItemOriginType
import cz.smartbrains.qesu.module.item.type.ItemType

class ItemFilter(val type: ItemType? = null,
                 val origin: ItemOriginType? = null,
                 val determination: ItemDeterminationType? = null,
                 val categories: List<Long> = ArrayList(),
                 val batchEvidence: Boolean? = null,
                 val activeOnly: Boolean = false)