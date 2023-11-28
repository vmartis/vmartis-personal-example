package cz.smartbrains.qesu.module.stock.movement.service

import cz.smartbrains.qesu.module.stock.movement.dto.StockMovementDto
import cz.smartbrains.qesu.module.stock.movement.dto.StockMovementFilter
import cz.smartbrains.qesu.module.stock.movement.entity.StockMovement
import cz.smartbrains.qesu.module.stock.movement.mapper.StockMovementMapper
import cz.smartbrains.qesu.module.stock.movement.repository.StockMovementRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Service
class StockMovementServiceImpl(private val stockMovementRepository: StockMovementRepository,
                               private val stockMovementMapper: StockMovementMapper) : StockMovementService {
    @Transactional(readOnly = true)
    override fun findByFilter(filter: StockMovementFilter): List<StockMovementDto> {
        return stockMovementRepository.findByFilter(filter)
                .stream()
                .map { stockMovement: StockMovement? -> stockMovementMapper.doToDto(stockMovement)!! }
                .collect(Collectors.toList())
    }
}