package cz.smartbrains.qesu.module.bank.dto

import cz.smartbrains.qesu.module.subject.dto.SubjectDto
import cz.smartbrains.qesu.module.common.dto.AbstractDto
import lombok.ToString
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

@ToString(callSuper = true)
class BankAccountDto : AbstractDto() {
    var name: @Length(max = 100) String? = null
    var accountId: @Length(max = 17) @NotNull @Pattern(regexp = "^[0-9-]{1,17}$") String? = null
    var bankId: @NotNull @Length(max = 4) @Pattern(regexp = "^[0-9]{4}$") String? = null
    var currency: @NotNull @Length(max = 3) @Pattern(regexp = "^[A-Z]{3}$") String? = null
    var iban: @Length(min = 1, max = 34) @Pattern(regexp = "^[A-Z,0-9]{3,34}$") String? = null
    var bic: @Length(max = 11) @Pattern(regexp = "^[A-Z,0-9]{1,11}$") String? = null
    var active = false
    var subject: SubjectDto? = null
}