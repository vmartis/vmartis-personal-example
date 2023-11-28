package cz.smartbrains.qesu.module.moneyBox.dto

import cz.smartbrains.qesu.module.moneyBox.type.AccountingType
import cz.smartbrains.qesu.module.moneyBox.type.MoneyMovementType
import java.time.LocalDate

class MoneyMovementFilter(var currency: String? = null,
                          var active: Boolean? = null,
                          var moneyBoxId: Long? = null,
                          var subjectId: Long? = null,
                          var dateFrom: LocalDate? = null,
                          var dateTo: LocalDate? = null,
                          var taxableDateFrom: LocalDate? = null,
                          var taxableDateTo: LocalDate? = null,
                          var types: Set<MoneyMovementType>? = null,
                          var accountingTypes: Set<AccountingType>? = null,
                          var categories: Set<Long>? = null) {
}