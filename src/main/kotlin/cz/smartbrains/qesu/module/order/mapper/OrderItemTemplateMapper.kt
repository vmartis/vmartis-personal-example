package cz.smartbrains.qesu.module.order.mapper

import cz.smartbrains.qesu.module.item.dto.ItemDto
import cz.smartbrains.qesu.module.item.entity.Item
import cz.smartbrains.qesu.module.company.dto.CompanyDto
import cz.smartbrains.qesu.module.company.entity.Company
import cz.smartbrains.qesu.module.order.dto.OrderItemTemplateDto
import cz.smartbrains.qesu.module.order.entity.OrderItemTemplate
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(componentModel = "spring")
abstract class OrderItemTemplateMapper {
    @Mappings(Mapping(target = "created", ignore = true), Mapping(target = "updated", ignore = true))
    abstract fun dtoToDo(dto: OrderItemTemplateDto?): OrderItemTemplate?
    abstract fun doToDto(entity: OrderItemTemplate?): OrderItemTemplateDto?
    protected fun itemDoToDto(entity: Item?): ItemDto? {
        if (entity == null) {
            return null
        }
        val dto = ItemDto()
        dto.id = entity.id
        dto.name = entity.name
        return dto
    }

    protected fun itemDtoToDo(dto: ItemDto?): Item? {
        if (dto == null) {
            return null
        }
        val entity = Item()
        entity.id = dto.id
        return entity
    }

    protected fun companyDoToDto(entity: Company?): CompanyDto? {
        if (entity == null) {
            return null
        }
        val dto = CompanyDto()
        dto.id = entity.id
        return dto
    }

    protected fun companyDtoToDo(dto: CompanyDto?): Company? {
        if (dto == null) {
            return null
        }
        val entity = Company()
        entity.id = dto.id
        return entity
    }
}