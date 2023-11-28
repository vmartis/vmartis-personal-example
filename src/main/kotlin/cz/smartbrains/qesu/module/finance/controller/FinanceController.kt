package cz.smartbrains.qesu.module.finance.controller

import cz.smartbrains.qesu.module.finance.dto.AmountSummaryDto
import cz.smartbrains.qesu.module.finance.service.FinanceService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@RestController
@RequestMapping("/api/finance-summary")
class FinanceController(private val financeService: FinanceService) {
    @GetMapping(path = ["/vat/{currency}"])
    fun vatOverview(@PathVariable currency: String): List<AmountSummaryDto> {
        val now = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth())
        val prevMonth = now.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth())
        val prev2Month = now.minusMonths(2).with(TemporalAdjusters.firstDayOfMonth())
        return listOf(
                financeService.vatSummary(now, now.with(TemporalAdjusters.lastDayOfMonth()), currency),
                financeService.vatSummary(prevMonth, prevMonth.with(TemporalAdjusters.lastDayOfMonth()), currency),
                financeService.vatSummary(prev2Month, prev2Month.with(TemporalAdjusters.lastDayOfMonth()), currency)
        )
    }

    @GetMapping(path = ["/tax-base/{currency}"])
    fun taxBaseSummary(@PathVariable currency: String): List<AmountSummaryDto> {
        val now = LocalDate.now().with(TemporalAdjusters.firstDayOfYear())
        val prevMonth = now.minusYears(1).with(TemporalAdjusters.firstDayOfYear())
        return listOf(
                financeService.taxBaseSummaryByYear(now, now.with(TemporalAdjusters.lastDayOfYear()), currency),
                financeService.taxBaseSummaryByYear(prevMonth, prevMonth.with(TemporalAdjusters.lastDayOfYear()), currency)
        )
    }
}