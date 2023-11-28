package cz.smartbrains.qesu.module.moneyBox.dto

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import cz.smartbrains.qesu.module.common.dto.AuditableDto
import cz.smartbrains.qesu.module.invoice.dto.InvoiceDto
import cz.smartbrains.qesu.module.user.dto.UserDto
import cz.smartbrains.qesu.module.common.dto.AbstractDto
import cz.smartbrains.qesu.module.moneyBox.type.AccountingType
import cz.smartbrains.qesu.module.moneyBox.type.MoneyMovementType
import cz.smartbrains.qesu.module.subject.dto.SubjectDto
import lombok.EqualsAndHashCode
import lombok.ToString
import org.hibernate.validator.constraints.Length
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import javax.validation.constraints.Digits
import javax.validation.constraints.NotNull

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, exclude = ["moneyBox", "subject", "category", "invoice", "createdBy", "updatedBy"])
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes(JsonSubTypes.Type(value = CashMoneyMovementDto::class, name = "cash"), JsonSubTypes.Type(value = BankMoneyMovementDto::class, name = "bank"))
abstract class MoneyMovementDto : AbstractDto(), AuditableDto {
    var moneyBox: @NotNull MoneyBoxDto? = null
    open var subject: SubjectDto? = null
    var category: MoneyMovementCategoryDto? = null
    var invoice: InvoiceDto? = null
    var type: @NotNull MoneyMovementType? = null
    var date: @NotNull LocalDateTime? = null
    var taxableDate: LocalDate? = null
    var totalAmount: @NotNull @Digits(integer = 8, fraction = 2) BigDecimal? = null
    var accountingType: @NotNull AccountingType? = null
    var totalVat: @Digits(integer = 8, fraction = 2) BigDecimal? = null
    var totalWithoutVat: @Digits(integer = 8, fraction = 2) BigDecimal? = null
    var note: @Length(max = 300) String? = null
    var active = false
    override var created: LocalDateTime? = null
    override var createdBy: UserDto? = null
    override var updated: LocalDateTime? = null
    override var updatedBy: UserDto? = null
}