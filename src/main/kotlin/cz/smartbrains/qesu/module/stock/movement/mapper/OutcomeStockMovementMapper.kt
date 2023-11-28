package cz.smartbrains.qesu.module.stock.movement.mapper

import cz.smartbrains.qesu.module.common.mapper.AuditableMapper
import cz.smartbrains.qesu.module.inventory.dto.InventoryDto
import cz.smartbrains.qesu.module.inventory.entity.Inventory
import cz.smartbrains.qesu.module.item.mapper.ItemMapper
import cz.smartbrains.qesu.module.stock.dto.StockDto
import cz.smartbrains.qesu.module.stock.mapper.StockMapper
import cz.smartbrains.qesu.module.stock.movement.dto.IncomeStockMovementDto
import cz.smartbrains.qesu.module.stock.movement.dto.OutcomeStockMovementDto
import cz.smartbrains.qesu.module.stock.movement.dto.StockMovementItemDto
import cz.smartbrains.qesu.module.stock.movement.entity.IncomeStockMovement
import cz.smartbrains.qesu.module.stock.movement.entity.OutcomeStockMovement
import cz.smartbrains.qesu.module.stock.movement.entity.StockMovementItem
import cz.smartbrains.qesu.module.stock.movement.type.OutcomeStockMovementType
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime
import java.util.*
import java.util.stream.Collectors

@Mapper(componentModel = "spring", uses = [AuditableMapper::class, StockMovementItemMapper::class, StockMapper::class])
abstract class OutcomeStockMovementMapper {
    @Autowired
    private var itemMapper: ItemMapper? = null

    @Mappings(Mapping(target = "editable", constant = "false"))
    abstract fun doToDto(movement: OutcomeStockMovement?): OutcomeStockMovementDto?
    @Mappings(Mapping(target = "editable", expression = "java(isEditable(movement, lastEditableDate))"))
    abstract fun doToDto(movement: OutcomeStockMovement?, lastEditableDate: LocalDateTime?): OutcomeStockMovementDto?
    @Mappings(Mapping(target = "totalPrice", expression = "java(calcTotalPrice(dto))"), Mapping(target = "incomeMovement", ignore = true), Mapping(target = "created", ignore = true), Mapping(target = "createdBy", ignore = true), Mapping(target = "updated", ignore = true), Mapping(target = "updatedBy", ignore = true), Mapping(target = "mainType", ignore = true))
    abstract fun dtoToDo(dto: OutcomeStockMovementDto?): OutcomeStockMovement?
    fun inventoryDoToDto(inventory: Inventory?): InventoryDto? {
        if (inventory == null) {
            return null
        }
        val inventoryDto = InventoryDto()
        inventoryDto.id = inventory.id
        inventoryDto.number = inventory.number
        return inventoryDto
    }

    fun inventoryDtoToDo(inventoryDto: InventoryDto?): Inventory? {
        if (inventoryDto == null) {
            return null
        }
        val inventory = Inventory()
        inventory.id = inventoryDto.id
        return inventory
    }

    fun stockMovementItemDtoToDo(items: List<StockMovementItemDto>?): List<StockMovementItem> {
        return if (items == null) {
            emptyList()
        } else items.stream().map { movementItem: StockMovementItemDto ->
            val entity = StockMovementItem()
            entity.id = movementItem.id
            entity.amount = movementItem.amount!!.abs().negate()
            entity.price = movementItem.price
            entity.item = itemMapper!!.dtoToDo(movementItem.item)
            entity.itemBatches = ArrayList(movementItem.itemBatches)
            entity.totalPrice = movementItem.amount!!.multiply(movementItem.price).setScale(PRICE_SCALE, RoundingMode.HALF_UP).abs().negate()
            entity
        }.collect(Collectors.toList())
    }

    fun incomeStockMovementDoToDto(incomeStockMovement: IncomeStockMovement?): IncomeStockMovementDto? {
        if (incomeStockMovement == null) {
            return null
        }
        val dto = IncomeStockMovementDto()
        dto.id = incomeStockMovement.id
        dto.number = incomeStockMovement.number
        val stock = StockDto()
        stock.id = incomeStockMovement.stock!!.id
        stock.name = incomeStockMovement.stock!!.name
        dto.stock = stock
        return dto
    }

    protected fun calcTotalPrice(movement: OutcomeStockMovementDto?): BigDecimal? {
        if (movement == null) {
            return null
        }
        var totalPrice = BigDecimal.ZERO
        for (item in movement.items!!) {
            totalPrice = totalPrice.add(item.amount!!.multiply(item.price).setScale(PRICE_SCALE, RoundingMode.HALF_UP))
        }
        totalPrice = totalPrice.abs().negate()
        return totalPrice
    }

    protected fun isEditable(movement: OutcomeStockMovement, lastEditableDate: LocalDateTime?): Boolean {
        // is last and is not paired
        return (!movement.date!!.isBefore(lastEditableDate)
                && movement.incomeMovement == null
                && movement.type !== OutcomeStockMovementType.INVENTORY_SHORTAGE)
    }

    companion object {
        private const val PRICE_SCALE = 2
    }
}