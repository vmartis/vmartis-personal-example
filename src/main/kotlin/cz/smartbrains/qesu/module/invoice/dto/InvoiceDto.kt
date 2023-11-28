package cz.smartbrains.qesu.module.invoice.dto

import com.fasterxml.jackson.annotation.JsonProperty
import cz.smartbrains.qesu.module.bank.dto.BankAccountDto
import cz.smartbrains.qesu.module.common.dto.AbstractDto
import cz.smartbrains.qesu.module.company.dto.CompanyDto
import cz.smartbrains.qesu.module.invoice.entity.InvoiceSeries
import cz.smartbrains.qesu.module.invoice.type.InvoiceType
import cz.smartbrains.qesu.module.moneyBox.dto.MoneyMovementDto
import cz.smartbrains.qesu.module.order.dto.OrderStatementDto
import lombok.EqualsAndHashCode
import lombok.ToString
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@ToString(callSuper = true, exclude = ["supplier", "subscriber", "bankAccount", "invoiceSeries", "items", "orderStatement"])
@EqualsAndHashCode(callSuper = true)
class InvoiceDto : AbstractDto() {
    var invoiceSeries: InvoiceSeries? = null
    @field:NotNull
    var type: InvoiceType? = null
    @field:Size(max = 20)
    var number: String? = null
    @field:Pattern(regexp = "^[0-9]{0,4}$")
    var constantSymbol: String? = null
    @field:Pattern(regexp = "^[0-9]{0,10}$")
    var variableSymbol: String? = null
    @field:Pattern(regexp = "^[0-9]{0,10}$")
    var specificSymbol: String? = null
    @field:NotNull
    var supplier: CompanyDto? = null
    @field:NotNull
    var subscriber: CompanyDto? = null
    @field:NotNull
    var bankAccount: BankAccountDto? = null
    var totalAmount: BigDecimal? = null
    var totalVat: BigDecimal? = null

    @field:JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var unpaidAmount: BigDecimal? = null
    @field:NotEmpty
    @field:Size(max = 3)
    var currency: String? = null
    @field:NotEmpty
    @field:Size(max = 20)
    var paymentMethod: String? = null
    @field:NotNull
    var dateOfIssue: LocalDate? = null
    @field:NotNull
    var dueDate: LocalDate? = null
    var taxableDate: LocalDate? = null
    @field:Size(max = 200)
    var reference: String? = null
    var transferredVat = false
    @field:Valid
    @field:NotNull
    var items: MutableList<InvoiceItemDto>? = ArrayList()
    var movements: List<MoneyMovementDto> = ArrayList()
    var orderStatement: OrderStatementDto? = null
}