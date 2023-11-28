package cz.smartbrains.qesu.module.order.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import cz.smartbrains.qesu.module.user.dto.UserDto
import cz.smartbrains.qesu.module.common.dto.AbstractDto
import cz.smartbrains.qesu.module.common.dto.AuditableDto
import cz.smartbrains.qesu.module.common.json.EntitySerializer
import cz.smartbrains.qesu.module.company.dto.CompanyBranchDto
import cz.smartbrains.qesu.module.company.dto.CompanyDto
import cz.smartbrains.qesu.module.order.type.OrderState
import lombok.EqualsAndHashCode
import lombok.ToString
import org.hibernate.validator.constraints.Length
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true,
    exclude = ["supplierBranch", "subscriber", "subscriberBranch", "statement", "items", "createdBy", "updatedBy"])
class OrderDto : AbstractDto(), AuditableDto {
    var number: BigInteger? = null
    var state: @NotNull OrderState? = null
    var date: @NotNull LocalDate? = null
    var currency: @NotEmpty @Size(max = 3) String? = null

    @JsonSerialize(using = EntitySerializer::class)
    var supplierBranch: @NotNull CompanyBranchDto? = null

    @JsonSerialize(using = EntitySerializer::class)
    var subscriber: @NotNull CompanyDto? = null

    @JsonSerialize(using = EntitySerializer::class)
    var subscriberBranch: CompanyBranchDto? = null
    var statement: OrderStatementDto? = null

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var totalPrice: BigDecimal? = null

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var totalVat: BigDecimal? = null

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var totalDeliveredPrice: BigDecimal? = null

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var totalDeliveredVat: BigDecimal? = null
    var items: @NotEmpty MutableList<OrderItemDto>? = ArrayList()
    var subscriberOrderNumber: @Length(max = 50) String? = null
    var note: @Length(max = 300) String? = null
    override var created: LocalDateTime? = null
    override var createdBy: UserDto? = null
    override var updated: LocalDateTime? = null
    override var updatedBy: UserDto? = null
}