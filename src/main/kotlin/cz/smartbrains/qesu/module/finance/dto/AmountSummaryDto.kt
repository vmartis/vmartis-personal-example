package cz.smartbrains.qesu.module.finance.dto

import java.math.BigDecimal
import java.time.LocalDate

class AmountSummaryDto(var dateFrom: LocalDate? = null, var dateTo: LocalDate? = null, var amount: BigDecimal? = null) {
}