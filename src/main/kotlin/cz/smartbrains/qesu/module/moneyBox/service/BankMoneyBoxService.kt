package cz.smartbrains.qesu.module.moneyBox.service

import cz.smartbrains.qesu.module.moneyBox.dto.BankMoneyBoxDto
import org.springframework.security.access.prepost.PreAuthorize

interface BankMoneyBoxService {
    @PreAuthorize("hasAuthority('SETTING_FINANCE')")
    fun create(moneyBoxDto: BankMoneyBoxDto): BankMoneyBoxDto

    @PreAuthorize("hasAuthority('SETTING_FINANCE')")
    fun update(moneyBoxDto: BankMoneyBoxDto): BankMoneyBoxDto
    fun updateAccount(moneyBoxId: Long)
}