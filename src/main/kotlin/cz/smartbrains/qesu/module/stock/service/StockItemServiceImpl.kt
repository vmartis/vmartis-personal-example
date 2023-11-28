package cz.smartbrains.qesu.module.stock.service

import cz.smartbrains.qesu.module.common.exception.RecordNotFoundException
import cz.smartbrains.qesu.module.common.exception.ServiceRuntimeException
import cz.smartbrains.qesu.module.item.repository.ItemRepository
import cz.smartbrains.qesu.module.stock.dto.StockItemDto
import cz.smartbrains.qesu.module.stock.dto.StockItemFilter
import cz.smartbrains.qesu.module.stock.entity.StockItem
import cz.smartbrains.qesu.module.stock.mapper.StockItemMapper
import cz.smartbrains.qesu.module.stock.repository.StockItemRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.stream.Collectors

@Service
@Transactional
class StockItemServiceImpl(private val repository: StockItemRepository,
                           private val itemRepository: ItemRepository,
                           private val mapper: StockItemMapper) : StockItemService {
    @Transactional(readOnly = true)
    override fun find(id: Long): StockItemDto {
        return mapper.doToDto(repository.findById(id).orElseThrow { RecordNotFoundException() })!!
    }

    @Transactional(readOnly = true)
    override fun findAll(filter: StockItemFilter): List<StockItemDto> {
        return repository.findByFilter(filter)
                .stream()
                .map { stockItem: StockItem? -> mapper.doToDto(stockItem)!! }
                .collect(Collectors.toList())
    }

    override fun create(stockItem: StockItemDto): StockItemDto {
        validateItem(stockItem.item!!.id!!)
        val entity = mapper.dtoToDo(stockItem)
        entity!!.amount = BigDecimal.ZERO
        entity.price = BigDecimal.ZERO
        entity.active = true
        val savedEntity = repository.save(entity)
        return mapper.doToDto(savedEntity)!!
    }

    override fun update(stockItem: StockItemDto): StockItemDto {
        val originStockItem = repository.findById(stockItem.id!!).orElseThrow { RecordNotFoundException() }
        if (!stockItem.active && originStockItem.amount!!.compareTo(BigDecimal.ZERO) > 0) {
            throw ServiceRuntimeException("stock.item.update.active.amount.not.zero")
        }
        val stockItemEntity = mapper.dtoToDo(stockItem)
        originStockItem.minAmount = stockItem.minAmount
        originStockItem.stock = stockItemEntity!!.stock
        originStockItem.active = stockItemEntity.active
        return mapper.doToDto(originStockItem)!!
    }

    // only item marked as for stock are able to be set in stock item
    private fun validateItem(itemId: Long) {
        val itemEntity = itemRepository.findById(itemId).orElseThrow { RecordNotFoundException() }
        if (!itemEntity.forStock) {
            throw ServiceRuntimeException("stock.item.update.item.invalid")
        }
    }
}