package cz.smartbrains.qesu.module.stock.movement.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import cz.smartbrains.qesu.module.user.dto.UserDto
import cz.smartbrains.qesu.module.common.dto.AbstractDto
import cz.smartbrains.qesu.module.common.dto.AuditableDto
import cz.smartbrains.qesu.module.inventory.dto.InventoryDto
import cz.smartbrains.qesu.module.stock.dto.StockDto
import lombok.EqualsAndHashCode
import lombok.ToString
import org.hibernate.validator.constraints.Length
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDateTime
import java.util.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@EqualsAndHashCode(callSuper = false)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes(JsonSubTypes.Type(value = IncomeStockMovementDto::class, name = "income"), JsonSubTypes.Type(value = OutcomeStockMovementDto::class, name = "outcome"))
abstract class StockMovementDto : AbstractDto(), AuditableDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var number: BigInteger? = null
    var stock: @NotNull StockDto? = null
    var date: @NotNull LocalDateTime? = null
    var note: @Length(max = 300) String? = null

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var totalPrice: BigDecimal? = null

    // not possible to validate because of cascade storing
    // @Valid
    var items: @NotNull @NotEmpty MutableList<StockMovementItemDto>? = ArrayList()

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var inventory: InventoryDto? = null
    override var created: LocalDateTime? = null
    override var createdBy: UserDto? = null
    override var updated: LocalDateTime? = null
    override var updatedBy: UserDto? = null

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var editable = false
    override fun toString(): String {
        return "StockMovementDto(number=$number, stock=$stock, date=$date, note=$note, totalPrice=$totalPrice, items=$items, inventory=$inventory, created=$created, createdBy=$createdBy, updated=$updated, updatedBy=$updatedBy, editable=$editable)"
    }


}