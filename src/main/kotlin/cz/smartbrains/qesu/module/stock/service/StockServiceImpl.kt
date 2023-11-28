package cz.smartbrains.qesu.module.stock.service

import cz.smartbrains.qesu.module.common.exception.RecordNotFoundException
import cz.smartbrains.qesu.module.common.service.OrderableEntityService
import cz.smartbrains.qesu.module.stock.dto.StockDto
import cz.smartbrains.qesu.module.stock.entity.Stock
import cz.smartbrains.qesu.module.stock.mapper.StockMapper
import cz.smartbrains.qesu.module.stock.repository.StockRepository
import cz.smartbrains.qesu.module.stock.entity.Stock_
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.function.Function
import java.util.stream.Collectors

@Service
@Transactional
class StockServiceImpl(private val repository: StockRepository,
                       private val mapper: StockMapper,
                       private val orderableEntityService: OrderableEntityService) : StockService {
    @Transactional(readOnly = true)
    override fun findAll(): List<StockDto> {
        return repository
                .findAll(Sort.by(Stock_.ORDER).ascending())
                .stream()
                .map { stock: Stock? -> mapper.doToDto(stock)!! }
                .collect(Collectors.toList())
    }

    override fun create(stockDto: StockDto): StockDto {
        val entity = mapper.dtoToDo(stockDto)
        entity!!.active = true
        entity.order = orderableEntityService.newItemOrder(repository)
        // only one stock can be marked as default for expedition, so disable others
        if (entity.defaultForDispatch) {
            repository.disableDefaultForDispatch(entity.companyBranch!!.id!!)
        }
        val savedEntity = repository.save(entity)
        return mapper.doToDto(savedEntity)!!
    }

    override fun update(stockDto: StockDto): StockDto {
        val originStock = repository.findById(stockDto.id!!).orElseThrow { RecordNotFoundException() }
        val stock = mapper.dtoToDo(stockDto)!!
        // only one stock can be marked as default for expedition, so disable others
        if (!originStock.defaultForDispatch && stock.defaultForDispatch) {
            repository.disableDefaultForDispatch(originStock.companyBranch!!.id!!)
        }
        originStock.name = stockDto.name
        originStock.currency = stockDto.currency
        originStock.companyBranch = stock.companyBranch
        originStock.defaultForDispatch = stock.defaultForDispatch
        originStock.active = stockDto.active
        return mapper.doToDto(originStock)!!
    }

    override fun updatePosition(stockId: Long, position: Int): StockDto {
        val stock = orderableEntityService.updatePosition(repository, stockId, position)
        return mapper.doToDto(stock)!!
    }
}