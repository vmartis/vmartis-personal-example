package cz.smartbrains.qesu.module.moneyBox.dto

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import cz.smartbrains.qesu.module.company.dto.CompanyBranchDto
import cz.smartbrains.qesu.module.common.dto.AbstractDto
import cz.smartbrains.qesu.module.common.entity.Orderable
import lombok.EqualsAndHashCode
import lombok.ToString
import org.hibernate.validator.constraints.Length
import java.math.BigDecimal
import javax.validation.constraints.Digits
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, exclude = ["companyBranch"])
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes(JsonSubTypes.Type(value = CashMoneyBoxDto::class, name = "cash"), JsonSubTypes.Type(value = BankMoneyBoxDto::class, name = "bank"))
abstract class MoneyBoxDto : AbstractDto(), Orderable {
    var name: @NotNull @Length(max = 200) String? = null
    var companyBranch: @NotNull CompanyBranchDto? = null
    var amount: @Digits(integer = 10, fraction = 2) BigDecimal? = null
    var currency: @Length(max = 3) @Pattern(regexp = "^[A-Z]{3}$") String? = null
    var active = false
    override var order = 0
}