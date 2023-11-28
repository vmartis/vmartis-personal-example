package cz.smartbrains.qesu.module.moneyBox.service

import cz.smartbrains.qesu.module.moneyBox.dto.MoneyMovementDto
import cz.smartbrains.qesu.module.moneyBox.dto.MoneyMovementFilter
import org.springframework.security.access.prepost.PreAuthorize

interface MoneyMovementService {
    @PreAuthorize("hasAnyAuthority('FINANCE_MONEY_BOX', 'FINANCE_OVERVIEW')")
    fun findByFilter(filter: MoneyMovementFilter): List<MoneyMovementDto>
}