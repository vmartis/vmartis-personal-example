package cz.smartbrains.qesu.module.inventory.mapper

import cz.smartbrains.qesu.module.common.exception.ServiceRuntimeException
import cz.smartbrains.qesu.module.common.mapper.AuditableMapper
import cz.smartbrains.qesu.module.inventory.dto.InventoryDto
import cz.smartbrains.qesu.module.inventory.entity.Inventory
import cz.smartbrains.qesu.module.stock.mapper.StockMapper
import cz.smartbrains.qesu.module.stock.movement.entity.StockMovement
import cz.smartbrains.qesu.module.stock.movement.mapper.StockMovementMapper
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import java.time.LocalDateTime
import java.util.stream.Stream

@Mapper(uses = [StockMapper::class, InventoryItemMapper::class, StockMovementMapper::class, AuditableMapper::class], componentModel = "spring")
abstract class InventoryMapper {
    @Mappings(Mapping(target = "created", ignore = true), Mapping(target = "updated", ignore = true), Mapping(target = "movements", ignore = true), Mapping(target = "createdBy", ignore = true), Mapping(target = "updatedBy", ignore = true))
    abstract fun dtoToDo(inventory: InventoryDto?): Inventory?
    @Mappings(Mapping(target = "editable", constant = "false"))
    abstract fun doToDto(inventory: Inventory?): InventoryDto?
    @Mappings(Mapping(target = "editable", expression = "java(isEditable(inventory, lastEditableDate))"))
    abstract fun doToDto(inventory: Inventory?, lastEditableDate: LocalDateTime?): InventoryDto?
    protected fun isEditable(inventory: Inventory, lastEditableDate: LocalDateTime?): Boolean {
        val maxDate = Stream.concat(
                inventory.movements.stream().map(StockMovement::date),
                Stream.of(inventory.date))
                .max { obj: LocalDateTime?, other: LocalDateTime? -> obj!!.compareTo(other) }
                .orElseThrow { ServiceRuntimeException("inventory.invalid.date") }
        return !maxDate!!.isBefore(lastEditableDate)
    }
}