package cz.smartbrains.qesu.module.moneyBox.dto

import java.math.BigDecimal
import java.time.LocalDate

class MoneyBoxBalanceDto(var totalAmount: BigDecimal? = null, var date: LocalDate? = null, var moneyBox: MoneyBoxDto? = null)