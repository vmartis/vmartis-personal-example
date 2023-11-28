package cz.smartbrains.qesu.module.common.service

import cz.smartbrains.qesu.module.common.dto.CodeListDto
import cz.smartbrains.qesu.module.common.entity.CodeList

interface CodeListService<E : CodeList, T : CodeListDto> : OrdeableEntityService<T> {
    fun findAll(): List<T>
    fun create(codeListDto: T): T
    fun update(codeListDto: T): T
    fun delete(id: Long)
}