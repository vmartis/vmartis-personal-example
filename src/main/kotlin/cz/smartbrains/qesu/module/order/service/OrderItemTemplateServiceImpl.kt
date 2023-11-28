package cz.smartbrains.qesu.module.order.service

import cz.smartbrains.qesu.module.common.exception.RecordNotFoundException
import cz.smartbrains.qesu.module.common.service.OrderableEntityService
import cz.smartbrains.qesu.module.order.dto.OrderItemTemplateDto
import cz.smartbrains.qesu.module.order.entity.OrderItemTemplate
import cz.smartbrains.qesu.module.order.mapper.OrderItemTemplateMapper
import cz.smartbrains.qesu.module.order.repository.OrderItemTemplateRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Service
@Transactional
class OrderItemTemplateServiceImpl(private val repository: OrderItemTemplateRepository,
                                   private val mapper: OrderItemTemplateMapper,
                                   private val orderableEntityService: OrderableEntityService) : OrderItemTemplateService {
    @Transactional(readOnly = true)
    override fun findAllByCompany(companyId: Long): List<OrderItemTemplateDto> {
        return repository.findAllByCompany(companyId)
                .stream()
                .map { entity: OrderItemTemplate? -> mapper.doToDto(entity)!! }
                .collect(Collectors.toList())
    }

    override fun create(orderItemTemplateDto: OrderItemTemplateDto): OrderItemTemplateDto {
        val orderItemTemplateEntity = mapper.dtoToDo(orderItemTemplateDto)
        orderItemTemplateEntity!!.order = orderableEntityService.newItemOrder(repository)
        val savedEntity = repository.save(orderItemTemplateEntity)
        return mapper.doToDto(savedEntity)!!
    }

    override fun update(orderItemTemplateDto: OrderItemTemplateDto): OrderItemTemplateDto {
        val originOrderItemTemplate = repository.findById(orderItemTemplateDto.id!!).orElseThrow { RecordNotFoundException() }
        val newOrderItemTemplate = mapper.dtoToDo(orderItemTemplateDto)
        originOrderItemTemplate.item = newOrderItemTemplate!!.item
        return mapper.doToDto(originOrderItemTemplate)!!
    }

    override fun updatePosition(id: Long, position: Int): OrderItemTemplateDto {
        val orderItemTemplate = orderableEntityService.updatePosition(repository, id, position)
        return mapper.doToDto(orderItemTemplate)!!
    }

    override fun delete(id: Long) {
        val entityToDelete = repository.getById(id)
        repository.deleteById(id)
        repository.decrementOrderRange(entityToDelete.order + 1, Int.MAX_VALUE)
    }
}