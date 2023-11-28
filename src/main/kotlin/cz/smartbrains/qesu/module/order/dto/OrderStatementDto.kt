package cz.smartbrains.qesu.module.order.dto

import cz.smartbrains.qesu.module.invoice.dto.InvoiceDto
import cz.smartbrains.qesu.module.user.dto.UserDto
import cz.smartbrains.qesu.module.common.dto.AbstractDto
import cz.smartbrains.qesu.module.common.dto.AuditableDto
import lombok.EqualsAndHashCode
import lombok.ToString
import org.hibernate.validator.constraints.Length
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, exclude = ["orders", "invoices"])
class OrderStatementDto : AbstractDto(), AuditableDto {
    var number: BigInteger? = null
    var date: @NotNull LocalDate? = null
    var orders: @NotEmpty MutableList<OrderDto>? = ArrayList()
    var invoices: List<InvoiceDto> = ArrayList()
    var note: @Length(max = 300) String? = null
    override var created: LocalDateTime? = null
    override var createdBy: UserDto? = null
    override var updated: LocalDateTime? = null
    override var updatedBy: UserDto? = null
}