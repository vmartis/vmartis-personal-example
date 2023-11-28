package cz.smartbrains.qesu.module.common.mapper

import cz.smartbrains.qesu.module.common.dto.EntityDto

interface DtoProvider<DtoT : EntityDto> {

    fun createDto(): DtoT
}