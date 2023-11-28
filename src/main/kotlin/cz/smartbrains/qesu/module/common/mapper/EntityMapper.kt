package cz.smartbrains.qesu.module.common.mapper

import cz.smartbrains.qesu.module.common.dto.EntityDto
import cz.smartbrains.qesu.module.common.entity.AbstractEntity
import org.mapstruct.Mapping
import org.mapstruct.Mappings

abstract class EntityMapper<DomainT : AbstractEntity, DtoT : EntityDto>(
    private val entityClazz: Class<DomainT>,
    private val dtoClass: Class<DtoT>,
) {

    @Mappings(Mapping(target = "created", ignore = true), Mapping(target = "updated", ignore = true))
    fun dtoToDo(dto: DtoT?): DomainT? {
        if (dto == null) {
            return null
        }
        val entity = entityClazz.getDeclaredConstructor().newInstance()
        entity.id = dto.id
        return entity
    }

    fun doToDto(entity: DomainT?): DtoT? {
        if (entity == null) {
            return null
        }
        val dto = dtoClass.getDeclaredConstructor().newInstance()
        dto.id = entity.id
        return dto
    }

}