package cz.smartbrains.qesu.module.takings.mapper

import cz.smartbrains.qesu.module.cashbox.dto.CashBoxDto
import cz.smartbrains.qesu.module.cashbox.entity.CashBox
import cz.smartbrains.qesu.module.common.mapper.AuditableMapper
import cz.smartbrains.qesu.module.takings.dto.TakingsDto
import cz.smartbrains.qesu.module.takings.entity.Takings
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(uses = [AuditableMapper::class], componentModel = "spring")
abstract class TakingsMapper {
    @Mappings(Mapping(target = "created", ignore = true), Mapping(target = "updated", ignore = true), Mapping(target = "createdBy", ignore = true), Mapping(target = "updatedBy", ignore = true))
    abstract fun dtoToDo(dto: TakingsDto?): Takings?
    abstract fun doToDto(entity: Takings?): TakingsDto?
    protected fun cashBoxDtoToDo(dto: CashBoxDto?): CashBox? {
        if (dto == null) {
            return null
        }
        val entity = CashBox()
        entity.id = dto.id
        return entity
    }

    protected fun cashBoxDoToDto(entity: CashBox?): CashBoxDto? {
        if (entity == null) {
            return null
        }
        val dto = CashBoxDto()
        dto.id = entity.id
        dto.name = entity.name
        dto.acceptCash = entity.acceptCash
        dto.acceptCheque = entity.acceptCheque
        dto.acceptCard = entity.acceptCard
        return dto
    }
}