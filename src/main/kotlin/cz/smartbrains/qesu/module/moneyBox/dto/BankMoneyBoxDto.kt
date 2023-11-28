package cz.smartbrains.qesu.module.moneyBox.dto

import cz.smartbrains.qesu.module.bank.dto.BankAccountDto
import cz.smartbrains.qesu.module.common.entity.Orderable
import lombok.EqualsAndHashCode
import lombok.ToString
import java.math.BigDecimal
import java.time.LocalDate
import javax.validation.Valid
import javax.validation.constraints.NotNull

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true, exclude = ["bankAccount"])
class BankMoneyBoxDto : MoneyBoxDto(), Orderable {
    var bankAccount: @NotNull @Valid BankAccountDto? = null
    var initialDate: @NotNull LocalDate? = null
    var initialAmount: @NotNull BigDecimal? = null
}