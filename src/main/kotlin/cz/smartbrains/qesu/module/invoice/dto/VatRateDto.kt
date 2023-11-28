package cz.smartbrains.qesu.module.invoice.dto

import cz.smartbrains.qesu.module.common.dto.CodeListBaseDto
import lombok.EqualsAndHashCode
import lombok.ToString
import java.math.BigDecimal
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
class VatRateDto : CodeListBaseDto() {
    var rate: @NotNull @Min(0) @Max(100) BigDecimal? = null
    var country: @NotNull @Pattern(regexp = "^[A-Z]{3}$") String? = null
}