package cz.smartbrains.qesu.module.moneyBox.service

import cz.smartbrains.qesu.module.moneyBox.dto.CashMoneyBoxDto
import org.springframework.security.access.prepost.PreAuthorize

interface CashMoneyBoxService {
    @PreAuthorize("hasAuthority('SETTING_FINANCE')")
    fun create(moneyBoxDto: CashMoneyBoxDto): CashMoneyBoxDto

    @PreAuthorize("hasAuthority('SETTING_FINANCE')")
    fun update(moneyBoxDto: CashMoneyBoxDto): CashMoneyBoxDto
    fun updateAccount(moneyBoxId: Long)
}