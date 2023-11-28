package cz.smartbrains.qesu.module.item.service

import com.google.common.collect.Lists
import cz.smartbrains.qesu.module.stock.movement.dto.StockMovementDto
import cz.smartbrains.qesu.module.stock.movement.dto.StockMovementItemDto
import cz.smartbrains.qesu.module.stock.repository.StockRepository
import cz.smartbrains.qesu.module.common.exception.RecordNotFoundException
import cz.smartbrains.qesu.module.inventory.dto.InventoryDto
import cz.smartbrains.qesu.module.inventory.dto.InventoryItemDto
import cz.smartbrains.qesu.module.item.dto.ItemBatchDto
import cz.smartbrains.qesu.module.item.dto.ItemBatchFilter
import cz.smartbrains.qesu.module.item.entity.ItemBatch
import cz.smartbrains.qesu.module.item.mapper.ItemBatchMapper
import cz.smartbrains.qesu.module.item.repository.ItemBatchRepository
import cz.smartbrains.qesu.module.item.repository.ItemRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.function.Consumer
import java.util.function.Predicate
import java.util.stream.Collectors

@Service
@Transactional
class ItemBatchServiceImpl(private val mapper: ItemBatchMapper, private val repository: ItemBatchRepository, private val itemRepository: ItemRepository, private val stockRepository: StockRepository) : ItemBatchService {
    @Transactional(readOnly = true)
    override fun findByFilter(filter: ItemBatchFilter): List<ItemBatchDto> {
        return repository.findByFilter(filter)
                .stream()
                .map { entity: ItemBatch? -> mapper.doToDto(entity)!! }
                .collect(Collectors.toList())
    }

    override fun createFromStockMovement(stockMovement: StockMovementDto) {
        val stock = stockMovement.stock
        stockMovement.items!!
                .stream() // filter out items without enabled batch evidence
                .filter(Predicate { stockMovementItem: StockMovementItemDto ->
                    val item = itemRepository.findById(stockMovementItem.item!!.id!!).orElseThrow { RecordNotFoundException() }
                    item.batchEvidence
                }) // filter out movementItem with empty batches
                .filter { stockMovementItem: StockMovementItemDto -> !stockMovementItem.itemBatches.isEmpty() }
                .forEach { stockMovementItem: StockMovementItemDto -> stockMovementItem.itemBatches.forEach(Consumer { itemBranchName: String -> activateOrCreate(stock!!.id!!, stockMovementItem.item!!.id!!, itemBranchName) }) }
    }

    override fun updateForInventory(inventory: InventoryDto) {
        val stock = inventory.stock
        inventory.items!!
                .stream() // filter out items without enabled batch evidence
                .filter(Predicate { inventoryItem: InventoryItemDto ->
                    val item = itemRepository.findById(inventoryItem.item!!.id!!).orElseThrow { RecordNotFoundException() }
                    item.batchEvidence
                })
                .forEach { inventoryItem: InventoryItemDto ->
                    val itemId = inventoryItem.item!!.id!!
                    if (inventoryItem.itemBatches.isEmpty()) {
                        repository.deactivateAllForItem(stock!!.id!!, itemId)
                    } else {
                        inventoryItem.itemBatches.forEach(Consumer { itemBranchName: String -> activateOrCreate(stock!!.id!!, itemId, itemBranchName) })
                        // deactivate all other batches
                        repository.deactivateAllForItem(stock!!.id!!, itemId, inventoryItem.itemBatches)
                    }
                }
    }

    /**
     * Activate item batch if exists already, or create new if doesn't exist.
     */
    private fun activateOrCreate(stockId: Long, itemId: Long, itemBatchName: String) {
        val itemBatchOpt = repository.find(stockId, itemId, itemBatchName)
        if (itemBatchOpt.isPresent) {
            val itemBatch = itemBatchOpt.get()
            itemBatch.active = true
            repository.saveAndFlush(itemBatch)
        } else {
            val newItemBatch = ItemBatch()
            newItemBatch.item = itemRepository.getById(itemId)
            newItemBatch.stock = stockRepository.getById(stockId)
            newItemBatch.name = itemBatchName
            newItemBatch.active = true
            repository.saveAndFlush(newItemBatch)
        }
    }
}