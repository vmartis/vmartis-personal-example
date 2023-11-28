package cz.smartbrains.qesu.module.invoice.dto

import cz.smartbrains.qesu.module.common.dto.AbstractDto
import cz.smartbrains.qesu.module.invoice.type.InvoiceSeriesFormat
import lombok.EqualsAndHashCode
import lombok.ToString
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotNull

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
class InvoiceSeriesDto : AbstractDto() {
    var name: @Length(max = 100) @NotNull String? = null
    var prefix: @NotNull @Length(max = 5) String? = null
    var format: @NotNull InvoiceSeriesFormat? = null
    var year = 0
    var index = 0
    var active = false
    var defaultFlag = false
    var order = 0
}