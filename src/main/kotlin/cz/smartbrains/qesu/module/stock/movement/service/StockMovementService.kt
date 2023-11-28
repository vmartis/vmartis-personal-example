package cz.smartbrains.qesu.module.stock.movement.service

import cz.smartbrains.qesu.module.stock.movement.dto.StockMovementDto
import cz.smartbrains.qesu.module.stock.movement.dto.StockMovementFilter
import org.springframework.security.access.prepost.PreAuthorize

interface StockMovementService {
    @PreAuthorize("hasAuthority('STOCK')")
    fun findByFilter(filter: StockMovementFilter): List<StockMovementDto>
}