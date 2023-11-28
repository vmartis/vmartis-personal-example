package cz.smartbrains.qesu.module.inventory.service

import cz.smartbrains.qesu.module.user.repository.UserRepository
import cz.smartbrains.qesu.module.common.exception.RecordNotFoundException
import cz.smartbrains.qesu.module.common.exception.ServiceRuntimeException
import cz.smartbrains.qesu.module.inventory.dto.InventoryDto
import cz.smartbrains.qesu.module.inventory.dto.InventoryFilter
import cz.smartbrains.qesu.module.inventory.entity.Inventory
import cz.smartbrains.qesu.module.inventory.entity.InventoryItem
import cz.smartbrains.qesu.module.inventory.mapper.InventoryMapper
import cz.smartbrains.qesu.module.inventory.repository.InventoryRepository
import cz.smartbrains.qesu.module.item.service.ItemBatchService
import cz.smartbrains.qesu.module.stock.entity.StockItem
import cz.smartbrains.qesu.module.stock.entity.StockItem_
import cz.smartbrains.qesu.module.stock.movement.entity.StockMovement
import cz.smartbrains.qesu.module.stock.movement.repository.StockMovementRepository
import cz.smartbrains.qesu.module.stock.repository.StockItemRepository
import cz.smartbrains.qesu.module.stock.service.StockBalanceService
import cz.smartbrains.qesu.module.stock.service.StockRecordService
import cz.smartbrains.qesu.security.AlfaUserDetails
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.function.Consumer
import java.util.stream.Collectors

@Service
@Transactional
class InventoryServiceImpl(private val inventoryRepository: InventoryRepository,
                           private val stockMovementRepository: StockMovementRepository,
                           private val stockItemRepository: StockItemRepository,
                           private val userRepository: UserRepository,
                           private val inventoryMapper: InventoryMapper,
                           private val inventoryNumberFactory: InventoryNumberFactory,
                           private val inventoryMovementService: InventoryMovementService,
                           private val stockRecordService: StockRecordService, private val balanceService: StockBalanceService, private val itemBatchService: ItemBatchService) : InventoryService {
    @Transactional(readOnly = true)
    override fun findByFilter(filter: InventoryFilter): List<InventoryDto> {
        return inventoryRepository.findByFilter(filter)
                .stream()
                .map { inventory: Inventory -> inventoryMapper.doToDto(inventory)!! }
                .collect(Collectors.toList())
    }

    @Transactional(readOnly = true)
    override fun find(id: Long): InventoryDto {
        val inventory = inventoryRepository.findById(id).orElseThrow { RecordNotFoundException() }
        return inventoryMapper.doToDto(inventory, stockRecordService.lastRecordDate(inventory.stock!!.id!!))!!
    }

    override fun create(inventoryDto: InventoryDto, user: AlfaUserDetails): InventoryDto {
        val inventory = inventoryMapper.dtoToDo(inventoryDto)
        stockRecordService.validateDateForCreate(inventoryDto.stock!!.id!!, inventory!!.date!!)
        validateItems(inventoryDto)
        inventory.createdBy = userRepository.getById(user.id)
        inventory.number = inventoryNumberFactory.nextNumber(inventory.stock!!.id!!, inventory.date!!.year)
        inventory.items.forEach(Consumer { invoiceItem: InventoryItem -> invoiceItem.inventory = inventory })
        val persistedInventory = inventoryRepository.save(inventory)
        inventoryMovementService.updateMovements(persistedInventory.id!!, user)
        itemBatchService.updateForInventory(inventoryDto)
        return inventoryMapper.doToDto(persistedInventory)!!
    }

    override fun update(inventoryDto: InventoryDto, user: AlfaUserDetails): InventoryDto {
        val originalInventory = inventoryRepository.findById(inventoryDto.id!!).orElseThrow { RecordNotFoundException() }
        validateIsLast(originalInventory) // only last inventory can be edited
        validateNoNewestMovements(originalInventory)
        validateItems(inventoryDto)
        val newInventory = inventoryMapper.dtoToDo(inventoryDto)
        if (originalInventory.date!!.year != newInventory!!.date!!.year) {
            originalInventory.number = inventoryNumberFactory.nextNumber(originalInventory.stock!!.id!!, newInventory.date!!.year)
        }
        originalInventory.date = newInventory.date
        originalInventory.note = newInventory.note
        newInventory.items.forEach(Consumer { stockMovementItem: InventoryItem -> stockMovementItem.inventory = originalInventory })
        originalInventory.items.clear()
        originalInventory.items.addAll(newInventory.items)
        originalInventory.updatedBy = userRepository.getById(user.id)
        val persistedInventory = inventoryRepository.save(originalInventory)

        // updated inventory movements
        inventoryMovementService.updateMovements(persistedInventory.id!!, user)
        itemBatchService.updateForInventory(inventoryDto)
        return inventoryMapper.doToDto(persistedInventory)!!
    }

    override fun delete(id: Long) {
        val inventory = inventoryRepository.findById(id).orElseThrow { RecordNotFoundException() }
        validateIsLast(inventory)
        validateNoNewestMovements(inventory)
        inventoryRepository.delete(inventory)
        balanceService.updateBalance(inventory.stock!!.id!!)
    }

    fun validateIsLast(inventory: Inventory) {
        val last = inventoryRepository.findTopByOrderByIdDesc().orElseThrow { RecordNotFoundException() }
        // check if is last inventory
        if (inventory.id != last.id) {
            throw ServiceRuntimeException("stock.movement.update.not.last")
        }
    }

    fun validateNoNewestMovements(inventory: Inventory) {
        val lastMovements = stockMovementRepository.findOlderThen(inventory.stock!!.id!!, inventory.date!!)
        val isNotLast = lastMovements.stream()
                .anyMatch { stockMovement: StockMovement -> stockMovement.inventory == null || stockMovement.inventory!!.id != inventory.id }
        if (isNotLast) {
            throw ServiceRuntimeException("stock.movement.update.not.last")
        }
    }

    /**
     * Validate items of inventory. Each item need to be present in stock items and each need to have price (at least
     * one stock movement has been done before)
     *
     * @param inventory to be validated.
     */
    fun validateItems(inventory: InventoryDto) {
        val stockItems = stockItemRepository.findAllByStockId(inventory.stock!!.id!!, Sort.by(StockItem_.ID))
        for (inventoryItem in inventory.items!!) {
            val currentStockItem = stockItems.stream()
                    .filter { stockItem: StockItem -> stockItem.item!!.id == inventoryItem.item!!.id }
                    .findFirst()
                    .orElseThrow { ServiceRuntimeException("inventory.invalid.item") }
            if (currentStockItem.price!!.compareTo(BigDecimal.ZERO) == 0) {
                throw ServiceRuntimeException("inventory.invalid.item.price")
            }
        }
    }
}