package cz.smartbrains.qesu.module.common.mapper

import cz.smartbrains.qesu.module.common.dto.CodeListDto
import cz.smartbrains.qesu.module.common.entity.CodeList
import org.mapstruct.Mapping
import org.mapstruct.Mappings

interface CodeListMapper<DomainT : CodeList?, DtoT : CodeListDto> {
    @Mappings(Mapping(target = "created", ignore = true), Mapping(target = "updated", ignore = true))
    fun dtoToDo(dto: DtoT?): DomainT
    fun doToDto(entity: DomainT?): DtoT
}