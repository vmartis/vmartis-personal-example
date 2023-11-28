package cz.smartbrains.qesu.module.inventory.service

import cz.smartbrains.qesu.module.common.exception.RecordNotFoundException
import cz.smartbrains.qesu.module.common.exception.ServiceRuntimeException
import cz.smartbrains.qesu.module.common.service.Messages
import cz.smartbrains.qesu.module.inventory.dto.InventoryDto
import cz.smartbrains.qesu.module.inventory.repository.InventoryRepository
import cz.smartbrains.qesu.module.item.dto.ItemDto
import cz.smartbrains.qesu.module.stock.dto.StockDto
import cz.smartbrains.qesu.module.stock.entity.StockItem
import cz.smartbrains.qesu.module.stock.entity.StockItem_
import cz.smartbrains.qesu.module.stock.movement.dto.IncomeStockMovementDto
import cz.smartbrains.qesu.module.stock.movement.dto.OutcomeStockMovementDto
import cz.smartbrains.qesu.module.stock.movement.dto.StockMovementItemDto
import cz.smartbrains.qesu.module.stock.movement.repository.StockMovementRepository
import cz.smartbrains.qesu.module.stock.movement.service.IncomeStockMovementService
import cz.smartbrains.qesu.module.stock.movement.service.OutcomeStockMovementService
import cz.smartbrains.qesu.module.stock.movement.type.IncomeStockMovementType
import cz.smartbrains.qesu.module.stock.movement.type.OutcomeStockMovementType
import cz.smartbrains.qesu.module.stock.repository.StockItemRepository
import cz.smartbrains.qesu.module.stock.service.StockBalanceService
import cz.smartbrains.qesu.security.AlfaUserDetails
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.temporal.ChronoUnit
import java.util.*

@Service
@Transactional
class InventoryMovementServiceImpl(
    private val inventoryRepository: InventoryRepository,
    private val stockItemRepository: StockItemRepository, private val balanceService: StockBalanceService,
    private val stockMovementRepository: StockMovementRepository,
    private val outcomeStockMovementService: OutcomeStockMovementService,
    private val incomeStockMovementService: IncomeStockMovementService,
    private val messages: Messages) : InventoryMovementService {
    override fun updateMovements(inventoryId: Long, alfaUserDetails: AlfaUserDetails) {
        val inventory = inventoryRepository.findById(inventoryId).orElseThrow { RecordNotFoundException() }

        // delete current movements of inventory - need to be deleted with deleteInBatch to ensure real deletion from DB
        // before new movements are created. Otherwise, creation of new movements fail on date check
        stockMovementRepository.deleteInBatch(inventory.movements)
        balanceService.updateBalance(inventory.stock!!.id!!)


        // load current state
        val stockItems = stockItemRepository.findAllByStockId(inventory.stock!!.id!!, Sort.by(StockItem_.ID))
        val outcomeItems: MutableList<StockMovementItemDto> = ArrayList()
        val incomeItems: MutableList<StockMovementItemDto> = ArrayList()
        for (inventoryItem in inventory.items) {
            val currentStockItem = stockItems.stream()
                .filter { stockItem: StockItem -> stockItem.item!!.id == inventoryItem.item!!.id }
                .findFirst()
                .orElseThrow { ServiceRuntimeException("inventory.invalid.item") }
            if (currentStockItem.price == null) {
                throw ServiceRuntimeException("inventory.invalid.item.price")
            }


            // skip zero changes
            if (inventoryItem.amount!!.compareTo(currentStockItem.amount) == 0) {
                continue
            }
            val stockMovementItem = StockMovementItemDto()
            val itemDto = ItemDto()
            itemDto.id = inventoryItem.item!!.id
            stockMovementItem.item = itemDto
            stockMovementItem.price = currentStockItem.price
            if (inventoryItem.amount!!.compareTo(currentStockItem.amount) > 0) {
                stockMovementItem.amount = inventoryItem.amount!!.subtract(currentStockItem.amount)
                incomeItems.add(stockMovementItem)
            } else if (inventoryItem.amount!!.compareTo(currentStockItem.amount) <= 0) {
                stockMovementItem.amount = currentStockItem.amount!!.subtract(inventoryItem.amount)
                outcomeItems.add(stockMovementItem)
            }
        }
        if (!incomeItems.isEmpty()) {
            val incomeMovement = IncomeStockMovementDto()
            val stock = StockDto()
            stock.id = inventory.stock!!.id
            incomeMovement.type = IncomeStockMovementType.INVENTORY_SURPLUS
            incomeMovement.date = inventory.date!!.plus(1L, ChronoUnit.MILLIS)
            incomeMovement.stock = stock
            incomeMovement.items = incomeItems
            val inventoryDto = InventoryDto()
            inventoryDto.id = inventoryId
            incomeMovement.inventory = inventoryDto
            incomeMovement.note = messages.getMessage("inventory.movement.note", inventory.formattedNumber)
            incomeStockMovementService.create(incomeMovement, alfaUserDetails)
        }
        if (!outcomeItems.isEmpty()) {
            val outcomeMovement = OutcomeStockMovementDto()
            val stock = StockDto()
            stock.id = inventory.stock!!.id
            outcomeMovement.type = OutcomeStockMovementType.INVENTORY_SHORTAGE
            outcomeMovement.date = inventory.date!!.plus(2L, ChronoUnit.MILLIS)
            outcomeMovement.stock = stock
            outcomeMovement.items = outcomeItems
            val inventoryDto = InventoryDto()
            inventoryDto.id = inventoryId
            outcomeMovement.inventory = inventoryDto
            outcomeMovement.note = messages.getMessage("inventory.movement.note", inventory.formattedNumber)
            outcomeStockMovementService.create(outcomeMovement, alfaUserDetails)
        }
    }
}