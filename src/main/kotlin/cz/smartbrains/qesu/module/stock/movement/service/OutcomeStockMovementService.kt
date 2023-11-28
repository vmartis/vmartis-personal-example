package cz.smartbrains.qesu.module.stock.movement.service

import cz.smartbrains.qesu.module.stock.movement.dto.OutcomeStockMovementDto
import cz.smartbrains.qesu.module.stock.movement.dto.OutcomeStockMovementFilter
import cz.smartbrains.qesu.security.AlfaUserDetails
import org.springframework.security.access.prepost.PreAuthorize

interface OutcomeStockMovementService {
    @PreAuthorize("hasAuthority('STOCK')")
    fun find(id: Long): OutcomeStockMovementDto

    @PreAuthorize("hasAuthority('STOCK')")
    fun findByFilter(filter: OutcomeStockMovementFilter): List<OutcomeStockMovementDto>

    @PreAuthorize("hasAuthority('STOCK')")
    fun create(incomeStockMovement: OutcomeStockMovementDto, user: AlfaUserDetails): OutcomeStockMovementDto

    @PreAuthorize("hasAuthority('STOCK')")
    fun update(incomeStockMovement: OutcomeStockMovementDto, user: AlfaUserDetails): OutcomeStockMovementDto

    @PreAuthorize("hasAuthority('STOCK')")
    fun delete(id: Long)
}