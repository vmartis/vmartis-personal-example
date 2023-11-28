package cz.smartbrains.qesu.module.item.mapper

import cz.smartbrains.qesu.module.item.dto.ItemDispatchDto
import cz.smartbrains.qesu.module.item.entity.ItemDispatch
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(componentModel = "spring", uses = [ItemMapper::class])
interface ItemDispatchMapper {
    fun doToDto(item: ItemDispatch?): ItemDispatchDto?

    @Mappings(Mapping(target = "created", ignore = true), Mapping(target = "updated", ignore = true))
    fun dtoToDo(item: ItemDispatchDto?): ItemDispatch?
}