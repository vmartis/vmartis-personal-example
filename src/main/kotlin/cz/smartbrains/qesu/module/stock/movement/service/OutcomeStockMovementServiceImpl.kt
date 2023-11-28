package cz.smartbrains.qesu.module.stock.movement.service

import com.google.common.collect.Lists
import cz.smartbrains.qesu.module.user.repository.UserRepository
import cz.smartbrains.qesu.module.common.exception.RecordNotFoundException
import cz.smartbrains.qesu.module.common.exception.ServiceRuntimeException
import cz.smartbrains.qesu.module.item.service.ItemBatchService
import cz.smartbrains.qesu.module.stock.entity.StockItem
import cz.smartbrains.qesu.module.stock.entity.StockItem_
import cz.smartbrains.qesu.module.stock.movement.dto.OutcomeStockMovementDto
import cz.smartbrains.qesu.module.stock.movement.dto.OutcomeStockMovementFilter
import cz.smartbrains.qesu.module.stock.movement.entity.OutcomeStockMovement
import cz.smartbrains.qesu.module.stock.movement.entity.StockMovementItem
import cz.smartbrains.qesu.module.stock.movement.mapper.OutcomeStockMovementMapper
import cz.smartbrains.qesu.module.stock.movement.repository.OutcomeStockMovementRepository
import cz.smartbrains.qesu.module.stock.movement.repository.StockMovementNumberFactory
import cz.smartbrains.qesu.module.stock.movement.type.OutcomeStockMovementType
import cz.smartbrains.qesu.module.stock.movement.type.StockMovementType
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
class OutcomeStockMovementServiceImpl(private val outcomeStockMovementRepository: OutcomeStockMovementRepository,
                                      private val stockItemRepository: StockItemRepository,
                                      private val outcomeStockMovementMapper: OutcomeStockMovementMapper,
                                      private val stockMovementNumberFactory: StockMovementNumberFactory,
                                      private val stockRecordService: StockRecordService,
                                      private val balanceService: StockBalanceService,
                                      private val userRepository: UserRepository,
                                      private val itemBatchService: ItemBatchService) : OutcomeStockMovementService {
    override fun find(id: Long): OutcomeStockMovementDto {
        val movement = outcomeStockMovementRepository.findById(id).orElseThrow { RecordNotFoundException() }
        return outcomeStockMovementMapper.doToDto(movement, stockRecordService.lastRecordDate(movement.stock!!.id!!))!!
    }

    @Transactional(readOnly = true)
    override fun findByFilter(filter: OutcomeStockMovementFilter): List<OutcomeStockMovementDto> {
        return outcomeStockMovementRepository.findByFilter(filter)
                .stream()
                .map { movement: OutcomeStockMovement? -> outcomeStockMovementMapper.doToDto(movement)!! }
                .collect(Collectors.toList())
    }

    override fun create(incomeStockMovement: OutcomeStockMovementDto, user: AlfaUserDetails): OutcomeStockMovementDto {
        val outcomeStockMovement = outcomeStockMovementMapper.dtoToDo(incomeStockMovement)!!
        stockRecordService.validateDateForCreate(incomeStockMovement.stock!!.id!!, incomeStockMovement.date!!)
        validateMovement(incomeStockMovement)
        outcomeStockMovement.createdBy = userRepository.getById(user.id)
        outcomeStockMovement.number = stockMovementNumberFactory.nextNumber(StockMovementType.OUTCOME, outcomeStockMovement.stock!!.id!!, outcomeStockMovement.date!!.year)
        outcomeStockMovement.items.forEach(Consumer { invoiceItem: StockMovementItem -> invoiceItem.movement = outcomeStockMovement })
        val movementPersisted = outcomeStockMovementRepository.save(outcomeStockMovement)
        balanceService.updateBalance(outcomeStockMovement.stock!!.id!!)
        itemBatchService.createFromStockMovement(incomeStockMovement)
        return outcomeStockMovementMapper.doToDto(movementPersisted)!!
    }

    override fun update(outcomeStockMovement: OutcomeStockMovementDto, user: AlfaUserDetails): OutcomeStockMovementDto {
        val originalMovement = outcomeStockMovementRepository.findById(outcomeStockMovement.id!!).orElseThrow { RecordNotFoundException() }
        validateUnpaired(originalMovement) // only unpaired movement can be edited
        stockRecordService.validateDateForUpdate(originalMovement.stock!!.id!!, outcomeStockMovement.date!!)
        validateMovement(outcomeStockMovement)
        val newMovement = outcomeStockMovementMapper.dtoToDo(outcomeStockMovement)!!
        if (originalMovement.date!!.year != newMovement.date!!.year) {
            originalMovement.number = stockMovementNumberFactory.nextNumber(StockMovementType.OUTCOME, originalMovement.stock!!.id!!, newMovement.date!!.year)
        }
        originalMovement.type = newMovement.type
        originalMovement.date = newMovement.date
        originalMovement.note = newMovement.note
        originalMovement.totalPrice = newMovement.totalPrice
        newMovement.items.forEach(Consumer { stockMovementItem: StockMovementItem -> stockMovementItem.movement = originalMovement })
        originalMovement.items.clear()
        originalMovement.items.addAll(newMovement.items)
        originalMovement.updatedBy = userRepository.getById(user.id)
        val persistedMovement = outcomeStockMovementRepository.save(originalMovement)
        balanceService.updateBalance(outcomeStockMovement.stock!!.id!!)
        itemBatchService.createFromStockMovement(outcomeStockMovement)
        return outcomeStockMovementMapper.doToDto(persistedMovement)!!
    }

    override fun delete(id: Long) {
        val movement = outcomeStockMovementRepository.findById(id).orElseThrow { RecordNotFoundException() }
        stockRecordService.validateDateForDelete(movement.stock!!.id!!, movement.date!!)
        val stockId = movement.stock!!.id
        outcomeStockMovementRepository.deleteById(id)
        balanceService.updateBalance(stockId!!)
    }

    fun validateMovement(outcomeStockMovement: OutcomeStockMovementDto) {
        validateItems(outcomeStockMovement)
        validateNotInventoryType(outcomeStockMovement)
    }

    fun validateItems(movement: OutcomeStockMovementDto) {
        val stockItems = stockItemRepository.findAllByStockId(movement.stock!!.id!!, Sort.by(StockItem_.ID))
        for (item in movement.items!!) {
            val currentStockItem = stockItems.stream()
                    .filter { stockItem: StockItem -> stockItem.item!!.id == item.item!!.id }
                    .findFirst()
                    .orElseThrow { ServiceRuntimeException("stock.movement.outcome.invalid.item") }

            // for update, max value need to be set to available amount + already used amount
            val availableAmounts: MutableList<BigDecimal?> = Lists.newArrayList(currentStockItem.amount)
            if (movement.id != null) {
                val originMovement = outcomeStockMovementRepository.findById(movement.id!!).orElseThrow { RecordNotFoundException() }
                originMovement
                        .items
                        .stream()
                        .filter { stockMovementItem: StockMovementItem -> stockMovementItem.item!!.id == item.item!!.id }
                        .findFirst().ifPresent { stockMovementItem: StockMovementItem -> availableAmounts.add(stockMovementItem.amount!!.abs()) }
            }
            val maxAmount = availableAmounts.stream().reduce { obj: BigDecimal?, augend: BigDecimal? -> obj!!.add(augend) }.orElseThrow()
            if (maxAmount!!.compareTo(item.amount) < 0) {
                throw ServiceRuntimeException("stock.movement.outcome.invalid.amount")
            } else if (currentStockItem.price!!.compareTo(item.price) != 0) {
                throw ServiceRuntimeException("stock.movement.outcome.invalid.price")
            }
        }
    }

    fun validateUnpaired(movement: OutcomeStockMovement) {
        if (movement.incomeMovement != null) {
            throw ServiceRuntimeException("stock.movement.update.paired")
        }
    }

    /**
     * It is not possible to create or updated inventory type movement
     */
    fun validateNotInventoryType(movement: OutcomeStockMovementDto) {
        if (movement.type === OutcomeStockMovementType.INVENTORY_SHORTAGE && movement.inventory == null) {
            throw ServiceRuntimeException("stock.movement.type.inventory")
        }
    }
}