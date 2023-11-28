package cz.smartbrains.qesu.module.bank.dto

import java.time.LocalDate
import javax.validation.constraints.NotNull

class BankTransactionFilter {
    var bankAccountId: @NotNull Long = 0
    var dateFrom: LocalDate? = null
    var dateTo: LocalDate? = null
}