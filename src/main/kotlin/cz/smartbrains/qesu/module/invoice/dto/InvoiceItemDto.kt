package cz.smartbrains.qesu.module.invoice.dto

import cz.smartbrains.qesu.module.common.dto.AbstractDto
import cz.smartbrains.qesu.module.unit.entity.Unit
import lombok.EqualsAndHashCode
import lombok.ToString
import java.math.BigDecimal
import javax.validation.constraints.*

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
class InvoiceItemDto : AbstractDto() {
    @field:NotEmpty
    @field:Size(max = 200)
    var name: String? = null
    @field:NotNull
    @field:Digits(integer = 8, fraction = 2)
    var price: BigDecimal? = null
    @field:NotNull
    var unit: Unit? = null
    @field:NotNull
    @field:Digits(integer = 8, fraction = 5)
    var number: BigDecimal? = null
    @field:NotNull
    @field:Digits(integer = 3, fraction = 2)
    @field:Max(100)
    var vatRate: BigDecimal? = null
}