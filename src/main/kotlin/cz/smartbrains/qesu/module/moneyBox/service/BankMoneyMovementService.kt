package cz.smartbrains.qesu.module.moneyBox.service

import cz.smartbrains.qesu.module.moneyBox.dto.BankMoneyMovementDto
import cz.smartbrains.qesu.module.moneyBox.dto.BankMoneyMovementSplitDto
import cz.smartbrains.qesu.security.AlfaUserDetails
import org.springframework.security.access.prepost.PreAuthorize

interface BankMoneyMovementService {
    @PreAuthorize("hasAuthority('FINANCE_MONEY_BOX')")
    fun update(moneyMovement: BankMoneyMovementDto, user: AlfaUserDetails): BankMoneyMovementDto

    @PreAuthorize("hasAuthority('FINANCE_MONEY_BOX')")
    fun split(moneyMovementSplit: BankMoneyMovementSplitDto, user: AlfaUserDetails): BankMoneyMovementDto
}