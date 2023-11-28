package cz.smartbrains.qesu.module.item.service

import cz.smartbrains.qesu.module.stock.dto.StockItemFilter
import cz.smartbrains.qesu.module.stock.repository.StockItemRepository
import cz.smartbrains.qesu.module.common.exception.RecordNotFoundException
import cz.smartbrains.qesu.module.common.exception.ServiceRuntimeException
import cz.smartbrains.qesu.module.item.dto.ItemDto
import cz.smartbrains.qesu.module.item.dto.ItemFilter
import cz.smartbrains.qesu.module.item.entity.Item
import cz.smartbrains.qesu.module.item.mapper.ItemMapper
import cz.smartbrains.qesu.module.item.repository.ItemDispatchRepository
import cz.smartbrains.qesu.module.item.repository.ItemRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Service
class ItemServiceImpl(private val itemRepository: ItemRepository,
                      private val stockItemRepository: StockItemRepository,
                      private val itemDispatchRepository: ItemDispatchRepository,
                      private val mapper: ItemMapper) : ItemService {

    @Transactional(readOnly = true)
    override fun findByFilter(filter: ItemFilter): List<ItemDto> {
        return itemRepository.findByFilter(filter)
                .stream()
                .map { entity: Item -> mapper.doToDto(entity)!! }
                .collect(Collectors.toList())
    }

    @Transactional(readOnly = true)
    override fun find(id: Long): ItemDto {
        return mapper.doToDto(itemRepository.findById(id).orElseThrow { RecordNotFoundException() })!!
    }

    @Transactional
    override fun create(item: ItemDto): ItemDto {
        val newItem = mapper.dtoToDo(item)
        newItem!!.active = true
        val savedEntity = itemRepository.save(newItem)
        return mapper.doToDto(savedEntity)!!
    }

    @Transactional
    override fun update(item: ItemDto): ItemDto {
        val newEntity = mapper.dtoToDo(item)!!
        val originItem = itemRepository.findById(item.id!!).orElseThrow { RecordNotFoundException() }!!
        validateItemDispatch(originItem, newEntity)
        checkStockItemStatus(originItem, newEntity)
        originItem.name = newEntity.name
        originItem.type = newEntity.type
        originItem.description = newEntity.description
        originItem.code = newEntity.code
        originItem.unit = newEntity.unit
        originItem.category = newEntity.category
        originItem.scale = newEntity.scale
        originItem.origin = newEntity.origin
        originItem.active = newEntity.active
        originItem.forPurchase = newEntity.forPurchase
        originItem.forStock = newEntity.forStock
        originItem.batchEvidence = newEntity.batchEvidence
        return mapper.doToDto(originItem)!!
    }

    private fun validateItemDispatch(origEntity: Item, newEntity: Item) {
        if (origEntity.forStock && !newEntity.forStock) {
            val count = itemDispatchRepository.countForSubItem(origEntity.id!!)
            if (count > 0) {
                throw ServiceRuntimeException("item.update.dispatch.subitem")
            }
        } else if (!origEntity.forStock && newEntity.forStock) {
            val count = itemDispatchRepository.countForItem(origEntity.id!!)
            if (count > 0) {
                throw ServiceRuntimeException("item.update.dispatch.item")
            }
        }
    }

    /**
     * Deactivate "forStock" property is available only if all stockItems of this item are inactive.
     */
    private fun checkStockItemStatus(origEntity: Item, newEntity: Item) {
        if (origEntity.forStock && !newEntity.forStock) {
            val filter = StockItemFilter(null, null, origEntity.id, true)
            val activeStockItems = stockItemRepository.findByFilter(filter)
            if (activeStockItems.isNotEmpty()) {
                throw ServiceRuntimeException("item.update.stock-item.active")
            }
        }
    }
}