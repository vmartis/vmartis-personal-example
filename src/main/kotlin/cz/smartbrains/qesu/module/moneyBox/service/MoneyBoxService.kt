package cz.smartbrains.qesu.module.moneyBox.service

import cz.smartbrains.qesu.module.moneyBox.dto.MoneyBoxBalanceDto
import cz.smartbrains.qesu.module.moneyBox.dto.MoneyBoxDto
import org.springframework.security.access.prepost.PreAuthorize
import java.time.LocalDate

interface MoneyBoxService {
    @PreAuthorize("hasAuthority('FINANCE_MONEY_BOX')")
    fun find(id: Long): MoneyBoxDto

    @PreAuthorize("hasAuthority('FINANCE_OVERVIEW')")
    fun balances(forDate: LocalDate, currency: String): List<MoneyBoxBalanceDto>

    @PreAuthorize("hasAnyAuthority('FINANCE_MONEY_BOX', 'FINANCE_OVERVIEW', 'SETTING_FINANCE', 'TAKINGS_TRANSFER')")
    fun findAll(): List<MoneyBoxDto>

    @PreAuthorize("hasAuthority('SETTING_FINANCE')")
    fun updatePosition(moneyBoxId: Long, position: Int): MoneyBoxDto
}