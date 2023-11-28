package cz.smartbrains.qesu.module.finance.service

import cz.smartbrains.qesu.module.finance.dto.AmountSummaryDto
import org.springframework.security.access.prepost.PreAuthorize
import java.time.LocalDate

interface FinanceService {
    @PreAuthorize("hasAuthority('FINANCE_OVERVIEW')")
    fun vatSummary(dateFrom: LocalDate, dateTo: LocalDate, currency: String): AmountSummaryDto

    @PreAuthorize("hasAuthority('FINANCE_OVERVIEW')")
    fun taxBaseSummaryByYear(dateFrom: LocalDate, dateTo: LocalDate, currency: String): AmountSummaryDto
}