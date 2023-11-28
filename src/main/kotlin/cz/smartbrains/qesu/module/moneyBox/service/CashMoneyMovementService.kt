package cz.smartbrains.qesu.module.moneyBox.service

import cz.smartbrains.qesu.module.moneyBox.dto.CashMoneyMovementDto
import cz.smartbrains.qesu.security.AlfaUserDetails
import org.springframework.security.access.prepost.PreAuthorize

interface CashMoneyMovementService {
    @PreAuthorize("hasAuthority('FINANCE_MONEY_BOX')")
    fun create(moneyMovement: CashMoneyMovementDto, user: AlfaUserDetails): CashMoneyMovementDto

    @PreAuthorize("hasAuthority('FINANCE_MONEY_BOX')")
    fun update(moneyMovement: CashMoneyMovementDto, user: AlfaUserDetails): CashMoneyMovementDto

    @PreAuthorize("hasAuthority('FINANCE_MONEY_BOX')")
    fun delete(id: Long)
}