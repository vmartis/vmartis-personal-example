package cz.smartbrains.qesu.module.takings.dto

import cz.smartbrains.qesu.module.user.dto.UserDto
import cz.smartbrains.qesu.module.cashbox.dto.CashBoxDto
import cz.smartbrains.qesu.module.common.dto.AbstractDto
import cz.smartbrains.qesu.module.common.dto.AuditableDto
import lombok.EqualsAndHashCode
import lombok.ToString
import org.hibernate.validator.constraints.Length
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true, exclude = ["cashBox"])
class TakingsDto : AbstractDto(), AuditableDto {
    var date: @NotNull LocalDate? = null
    var currency: @Length(max = 3) @Pattern(regexp = "^[A-Z]{3}$") String? = null
    var cashBox: @NotNull CashBoxDto? = null
    var amountCash = BigDecimal.ZERO
    var amountCheque = BigDecimal.ZERO
    var amountCard = BigDecimal.ZERO
    var totalVatAmount = BigDecimal.ZERO
    var totalCashAmount = BigDecimal.ZERO
    var totalChequeAmount = BigDecimal.ZERO
    var transferred = false
    override var created: LocalDateTime? = null
    override var createdBy: UserDto? = null
    override var updated: LocalDateTime? = null
    override var updatedBy: UserDto? = null
}