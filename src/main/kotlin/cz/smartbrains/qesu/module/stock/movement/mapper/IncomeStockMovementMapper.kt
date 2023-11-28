package cz.smartbrains.qesu.module.stock.movement.mapper

import cz.smartbrains.qesu.module.common.exception.RecordNotFoundException
import cz.smartbrains.qesu.module.common.mapper.AuditableMapper
import cz.smartbrains.qesu.module.company.mapper.CompanyMapper
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
import cz.smartbrains.qesu.module.stock.movement.repository.OutcomeStockMovementRepository
import cz.smartbrains.qesu.module.stock.movement.type.IncomeStockMovementType
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime
import java.util.*
import java.util.stream.Collectors

@Mapper(componentModel = "spring", uses = [AuditableMapper::class, CompanyMapper::class, StockMovementItemMapper::class, StockMapper::class])
abstract class IncomeStockMovementMapper {
    @Autowired
    var itemMapper: ItemMapper? = null
    @Autowired
    var outcomeStockMovementRepository: OutcomeStockMovementRepository? = null

    @Mappings(Mapping(target = "editable", constant = "false"))
    abstract fun doToDto(movement: IncomeStockMovement?): IncomeStockMovementDto?
    @Mappings(Mapping(target = "editable", expression = "java(isEditable(movement, lastEditableDate))"))
    abstract fun doToDto(movement: IncomeStockMovement?, lastEditableDate: LocalDateTime?): IncomeStockMovementDto?
    @Mappings(Mapping(target = "totalPrice", expression = "java(calcTotalPrice(dto))"), Mapping(target = "created", ignore = true), Mapping(target = "createdBy", ignore = true), Mapping(target = "updated", ignore = true), Mapping(target = "updatedBy", ignore = true), Mapping(target = "mainType", ignore = true))
    abstract fun dtoToDo(dto: IncomeStockMovementDto?): IncomeStockMovement?
    fun outcomeStockMovementDtoToDo(dto: OutcomeStockMovementDto?): OutcomeStockMovement? {
        return if (dto == null) {
            null
        } else outcomeStockMovementRepository!!.findById(dto.id!!).orElseThrow { RecordNotFoundException() }
    }

    fun stockMovementItemDtoToDo(items: List<StockMovementItemDto>?): List<StockMovementItem> {
        return if (items == null) {
            emptyList()
        } else items.stream().map { movementItem: StockMovementItemDto ->
            val entity = StockMovementItem()
            entity.id = movementItem.id
            entity.item = itemMapper!!.dtoToDo(movementItem.item)
            entity.amount = movementItem.amount
            entity.price = movementItem.price
            entity.itemBatches = ArrayList(movementItem.itemBatches)
            entity.totalPrice = movementItem.amount!!.multiply(movementItem.price).setScale(PRICE_SCALE, RoundingMode.HALF_UP)
            entity
        }.collect(Collectors.toList())
    }

    fun outcomeStockMovementDoToDto(outcomeStockMovement: OutcomeStockMovement?): OutcomeStockMovementDto? {
        if (outcomeStockMovement == null) {
            return null
        }
        val stock = StockDto()
        stock.name = outcomeStockMovement.stock!!.name
        stock.id = outcomeStockMovement.stock!!.id
        val outcomeStockMovementDto = OutcomeStockMovementDto()
        outcomeStockMovementDto.id = outcomeStockMovement.id
        outcomeStockMovementDto.number = outcomeStockMovement.number
        outcomeStockMovementDto.stock = stock
        return outcomeStockMovementDto
    }

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

    protected fun calcTotalPrice(movement: IncomeStockMovementDto?): BigDecimal? {
        if (movement == null) {
            return null
        }
        var totalPrice = BigDecimal.ZERO
        for (item in movement.items!!) {
            totalPrice = totalPrice.add(item.amount!!.multiply(item.price).setScale(PRICE_SCALE, RoundingMode.HALF_UP))
        }
        return totalPrice
    }

    protected fun isEditable(movement: IncomeStockMovement, lastEditableDate: LocalDateTime?): Boolean {
        return !movement.date!!.isBefore(lastEditableDate) && movement.type !== IncomeStockMovementType.INVENTORY_SURPLUS
    }

    companion object {
        private const val PRICE_SCALE = 2
    }
}