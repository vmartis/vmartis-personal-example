package cz.smartbrains.qesu.module.stock.movement.service

import cz.smartbrains.qesu.module.user.repository.UserRepository
import cz.smartbrains.qesu.module.common.exception.RecordNotFoundException
import cz.smartbrains.qesu.module.common.exception.ServiceRuntimeException
import cz.smartbrains.qesu.module.item.dto.ItemDto
import cz.smartbrains.qesu.module.item.entity.Item
import cz.smartbrains.qesu.module.item.service.ItemBatchService
import cz.smartbrains.qesu.module.stock.entity.StockItem
import cz.smartbrains.qesu.module.stock.entity.StockItem_
import cz.smartbrains.qesu.module.stock.movement.dto.IncomeStockMovementDto
import cz.smartbrains.qesu.module.stock.movement.dto.StockMovementItemDto
import cz.smartbrains.qesu.module.stock.movement.entity.StockMovementItem
import cz.smartbrains.qesu.module.stock.movement.mapper.IncomeStockMovementMapper
import cz.smartbrains.qesu.module.stock.movement.repository.IncomeStockMovementRepository
import cz.smartbrains.qesu.module.stock.movement.repository.OutcomeStockMovementRepository
import cz.smartbrains.qesu.module.stock.movement.repository.StockMovementNumberFactory
import cz.smartbrains.qesu.module.stock.movement.type.IncomeStockMovementType
import cz.smartbrains.qesu.module.stock.movement.type.StockMovementType
import cz.smartbrains.qesu.module.stock.repository.StockItemRepository
import cz.smartbrains.qesu.module.stock.repository.StockRepository
import cz.smartbrains.qesu.module.stock.service.StockBalanceService
import cz.smartbrains.qesu.module.stock.service.StockRecordService
import cz.smartbrains.qesu.security.AlfaUserDetails
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
import java.util.function.Consumer
import java.util.stream.Collectors

@Service
@Transactional
class IncomeStockMovementServiceImpl(private val incomeStockMovementRepository: IncomeStockMovementRepository,
                                     private val stockItemRepository: StockItemRepository,
                                     private val stockRepository: StockRepository,
                                     private val incomeStockMovementMapper: IncomeStockMovementMapper,
                                     private val outcomeStockMovementRepository: OutcomeStockMovementRepository,
                                     private val stockMovementNumberFactory: StockMovementNumberFactory,
                                     private val balanceService: StockBalanceService,
                                     private val userRepository: UserRepository,
                                     private val stockRecordService: StockRecordService, private val itemBatchService: ItemBatchService) : IncomeStockMovementService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun find(id: Long): IncomeStockMovementDto {
        val movement = incomeStockMovementRepository.findById(id).orElseThrow { RecordNotFoundException() }
        val maxMovementDate = stockRecordService.lastRecordDate(movement.stock!!.id!!)
        return incomeStockMovementMapper.doToDto(movement, maxMovementDate)!!
    }

    override fun create(incomeStockMovementDto: IncomeStockMovementDto, user: AlfaUserDetails): IncomeStockMovementDto {
        val incomeStockMovement = incomeStockMovementMapper.dtoToDo(incomeStockMovementDto)!!
        stockRecordService.validateDateForCreate(incomeStockMovementDto.stock!!.id!!, incomeStockMovementDto.date!!)
        validateMovement(incomeStockMovementDto)
        incomeStockMovement.createdBy = userRepository.getById(user.id)
        incomeStockMovement.number = stockMovementNumberFactory.nextNumber(StockMovementType.INCOME, incomeStockMovement.stock!!.id!!, incomeStockMovement.date!!.year)
        incomeStockMovement.items.forEach(Consumer { invoiceItem: StockMovementItem -> invoiceItem.movement = incomeStockMovement })
        log.debug("Income stock movement to be stored: {}", incomeStockMovement)
        val movementPersisted = incomeStockMovementRepository.save(incomeStockMovement)
        balanceService.updateBalance(incomeStockMovement.stock!!.id!!, true)
        itemBatchService.createFromStockMovement(incomeStockMovementDto)
        return incomeStockMovementMapper.doToDto(movementPersisted)!!
    }

    override fun update(incomeStockMovement: IncomeStockMovementDto, user: AlfaUserDetails): IncomeStockMovementDto {
        val originalMovement = incomeStockMovementRepository.findById(incomeStockMovement.id!!).orElseThrow { RecordNotFoundException() }
        stockRecordService.validateDateForUpdate(originalMovement.stock!!.id!!, incomeStockMovement.date!!)
        validateMovement(incomeStockMovement)
        val newMovement = incomeStockMovementMapper.dtoToDo(incomeStockMovement)!!
        originalMovement.type = newMovement.type
        if (originalMovement.date!!.year != newMovement.date!!.year) {
            originalMovement.number = stockMovementNumberFactory.nextNumber(StockMovementType.INCOME, originalMovement.stock!!.id!!, newMovement.date!!.year)
        }
        originalMovement.date = newMovement.date
        originalMovement.note = newMovement.note
        originalMovement.totalPrice = newMovement.totalPrice
        originalMovement.supplier = newMovement.supplier
        originalMovement.deliveryNote = newMovement.deliveryNote
        originalMovement.outcomeMovement = newMovement.outcomeMovement
        newMovement.items.forEach(Consumer { stockMovementItem: StockMovementItem -> stockMovementItem.movement = originalMovement })
        originalMovement.items.clear()
        originalMovement.items.addAll(newMovement.items)
        originalMovement.updatedBy = userRepository.getById(user.id)
        val persistedMovement = incomeStockMovementRepository.save(originalMovement)
        balanceService.updateBalance(originalMovement.stock!!.id!!, true)
        itemBatchService.createFromStockMovement(incomeStockMovement)
        return incomeStockMovementMapper.doToDto(persistedMovement)!!
    }

    override fun delete(id: Long) {
        val movement = incomeStockMovementRepository.findById(id).orElseThrow { RecordNotFoundException() }
        val stockId = movement.stock!!.id
        stockRecordService.validateDateForDelete(stockId!!, movement.date!!)
        incomeStockMovementRepository.deleteById(id)
        balanceService.updateBalance(stockId, true)
    }

    private fun validateMovement(incomeStockMovement: IncomeStockMovementDto) {
        validateNotInventoryType(incomeStockMovement)
        validateOutcomeMovement(incomeStockMovement)
        validateItems(incomeStockMovement)
        validateTransferItems(incomeStockMovement)
        validateSupplierAndDeliveryNote(incomeStockMovement)
    }

    fun validateOutcomeMovement(incomeStockMovement: IncomeStockMovementDto) {
        if (incomeStockMovement.type === IncomeStockMovementType.TRANSFER) {
            if (incomeStockMovement.outcomeMovement == null) {
                throw ServiceRuntimeException("stock.movement.transfer.outcome.movement.required")
            }
            // validate if currency matches
            val incomeMovementStock = stockRepository.findById(incomeStockMovement.stock!!.id!!).orElseThrow { RecordNotFoundException() }!!
            val outcomeMovementStock = outcomeStockMovementRepository.findById(incomeStockMovement.outcomeMovement!!.id!!).orElseThrow { RecordNotFoundException() }.stock
            if (incomeMovementStock.currency != outcomeMovementStock!!.currency) {
                throw ServiceRuntimeException("stock.movement.transfer.currency.not.match")
            }
        } else if (incomeStockMovement.type !== IncomeStockMovementType.TRANSFER && incomeStockMovement.outcomeMovement != null) {
            throw ServiceRuntimeException("stock.movement.not.transfer.outcome.movement.useless")
        }
    }

    private fun validateSupplierAndDeliveryNote(incomeStockMovement: IncomeStockMovementDto) {
        if (incomeStockMovement.type === IncomeStockMovementType.PURCHASE && incomeStockMovement.supplier == null) {
            throw ServiceRuntimeException("stock.movement.purchase.supplier.required")
        } else if (incomeStockMovement.type !== IncomeStockMovementType.PURCHASE && incomeStockMovement.supplier != null) {
            throw ServiceRuntimeException("stock.movement.purchase.supplier.useless")
        } else if (incomeStockMovement.type !== IncomeStockMovementType.PURCHASE && StringUtils.hasText(incomeStockMovement.deliveryNote)) {
            throw ServiceRuntimeException("stock.movement.purchase.delivery.note.useless")
        }
    }

    /**
     * Validate if all items are in stock available items.
     */
    fun validateItems(movement: IncomeStockMovementDto) {
        val availableItemIdsForStock = stockItemRepository.findAllByStockId(movement.stock!!.id!!, Sort.by(StockItem_.ID))
                .stream()
                .filter { stockItem: StockItem -> stockItem.active || movement.id != null } // check if item is active for create
                .map(StockItem::item)
                .map { item: Item? -> item!!.id!! }
                .collect(Collectors.toList())
        val movementItemIds = movement.items!!
                .stream()
                .map { obj: StockMovementItemDto -> obj.item!! }
                .map { item: ItemDto -> item.id!! }
                .collect(Collectors.toList())
        if (!availableItemIdsForStock.containsAll(movementItemIds)) {
            throw ServiceRuntimeException("stock.movement.income.invalid.item")
        }
    }

    /**
     * Check if items in income transfer movement are with proper value copied from origin outcome transfer movement.
     */
    fun validateTransferItems(incomeStockMovement: IncomeStockMovementDto) {
        if (incomeStockMovement.type === IncomeStockMovementType.TRANSFER) {
            val outcomeStockMovement = outcomeStockMovementRepository.findById(incomeStockMovement.outcomeMovement!!.id!!).orElseThrow { RecordNotFoundException() }
            if (outcomeStockMovement.items.size != incomeStockMovement.items!!.size) {
                throw ServiceRuntimeException("stock.movement.transfer.invalid.items.size")
            }
            for (i in incomeStockMovement.items!!.indices) {
                val incomeItem = incomeStockMovement.items!![i]
                val outcomeItem = outcomeStockMovement.items[i]
                // check item, price, amount and total price
                if (incomeItem.price!!.compareTo(outcomeItem.price) != 0 || incomeItem.item!!.id != outcomeItem.item!!.id
                        || incomeItem.amount!!.compareTo(outcomeItem.amount!!.abs()) != 0) {
                    throw ServiceRuntimeException("stock.movement.transfer.invalid.items")
                }
            }
        }
    }

    /**
     * It is not possible to create or updated inventory type movement
     */
    fun validateNotInventoryType(movement: IncomeStockMovementDto) {
        if (movement.type === IncomeStockMovementType.INVENTORY_SURPLUS && movement.inventory == null) {
            throw ServiceRuntimeException("stock.movement.type.inventory")
        }
    }
}