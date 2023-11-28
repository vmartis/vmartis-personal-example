package cz.smartbrains.qesu.module.common.service

import cz.smartbrains.qesu.module.common.dto.EntityDto

interface OrdeableEntityService<T : EntityDto> {
    fun updatePosition(id: Long, position: Int): T
}