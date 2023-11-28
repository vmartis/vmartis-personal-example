package cz.smartbrains.qesu.module.stock.movement.service

import cz.smartbrains.qesu.module.stock.movement.dto.IncomeStockMovementDto
import cz.smartbrains.qesu.security.AlfaUserDetails
import org.springframework.security.access.prepost.PreAuthorize

interface IncomeStockMovementService {
    @PreAuthorize("hasAuthority('STOCK')")
    fun find(id: Long): IncomeStockMovementDto

    @PreAuthorize("hasAuthority('STOCK')")
    fun create(incomeStockMovement: IncomeStockMovementDto, user: AlfaUserDetails): IncomeStockMovementDto

    @PreAuthorize("hasAuthority('STOCK')")
    fun update(incomeStockMovement: IncomeStockMovementDto, user: AlfaUserDetails): IncomeStockMovementDto

    @PreAuthorize("hasAuthority('STOCK')")
    fun delete(id: Long)
}