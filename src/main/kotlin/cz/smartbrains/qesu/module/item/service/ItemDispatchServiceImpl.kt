package cz.smartbrains.qesu.module.item.service

import com.google.common.collect.Lists
import cz.smartbrains.qesu.module.common.exception.RecordNotFoundException
import cz.smartbrains.qesu.module.common.exception.ServiceRuntimeException
import cz.smartbrains.qesu.module.item.dto.ItemDispatchDto
import cz.smartbrains.qesu.module.item.dto.ItemDispatchFilter
import cz.smartbrains.qesu.module.item.entity.ItemDispatch
import cz.smartbrains.qesu.module.item.mapper.ItemDispatchMapper
import cz.smartbrains.qesu.module.item.repository.ItemDispatchRepository
import cz.smartbrains.qesu.module.item.repository.ItemRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Service
@Transactional
class ItemDispatchServiceImpl(private val itemRepository: ItemRepository, private val repository: ItemDispatchRepository, private val mapper: ItemDispatchMapper) : ItemDispatchService {
    @Transactional(readOnly = true)
    override fun findByFilter(filter: ItemDispatchFilter): List<ItemDispatchDto> {
        return Lists.newArrayList(repository.findByFilter(filter))
                .stream()
                .map { item: ItemDispatch? -> mapper.doToDto(item)!! }
                .collect(Collectors.toList())
    }

    override fun create(itemDispatch: ItemDispatchDto): ItemDispatchDto {
        validateItem(itemDispatch.item!!.id!!)
        val newItemDispatch = mapper.dtoToDo(itemDispatch)!!
        val savedEntity = repository.save(newItemDispatch)
        return mapper.doToDto(savedEntity)!!
    }

    override fun update(itemDispatch: ItemDispatchDto): ItemDispatchDto {
        val newEntity = mapper.dtoToDo(itemDispatch)
        val originItemDispatch = repository.findById(itemDispatch.id!!).orElseThrow { RecordNotFoundException() }
        originItemDispatch.subItem = newEntity!!.subItem
        originItemDispatch.amount = newEntity.amount
        return mapper.doToDto(originItemDispatch)!!
    }

    override fun delete(id: Long) {
        repository.deleteById(id)
    }

    private fun validateItem(itemId: Long) {
        val itemEntity = itemRepository.findById(itemId).orElseThrow { RecordNotFoundException() }
        if (itemEntity.forStock) {
            throw ServiceRuntimeException("item.dispatch.item.invalid")
        }
    }
}