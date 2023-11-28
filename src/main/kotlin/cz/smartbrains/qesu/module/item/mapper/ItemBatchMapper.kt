package cz.smartbrains.qesu.module.item.mapper

import cz.smartbrains.qesu.module.stock.dto.StockDto
import cz.smartbrains.qesu.module.stock.mapper.StockMapper
import cz.smartbrains.qesu.module.item.dto.ItemBatchDto
import cz.smartbrains.qesu.module.item.dto.ItemDto
import cz.smartbrains.qesu.module.item.entity.ItemBatch
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(componentModel = "spring", uses = [ItemMapper::class, StockMapper::class])
abstract class ItemBatchMapper {
    fun doToDto(entity: ItemBatch?): ItemBatchDto? {
        if (entity == null) {
            return null
        }
        val dto = ItemBatchDto()
        dto.name = entity.name
        dto.active = entity.active
        dto.id = entity.id
        val itemDto = ItemDto()
        itemDto.id = entity.item!!.id
        val stockDto = StockDto()
        stockDto.id = entity.stock!!.id
        stockDto.name = entity.stock!!.name
        dto.item = itemDto
        dto.stock = stockDto
        return dto
    }

    @Mappings(Mapping(target = "created", ignore = true), Mapping(target = "updated", ignore = true))
    abstract fun dtoToDo(item: ItemBatchDto?): ItemBatch?
}