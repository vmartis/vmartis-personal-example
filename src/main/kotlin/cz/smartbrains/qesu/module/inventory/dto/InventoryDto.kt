package cz.smartbrains.qesu.module.inventory.dto

import com.fasterxml.jackson.annotation.JsonProperty
import cz.smartbrains.qesu.module.common.dto.AbstractDto
import cz.smartbrains.qesu.module.common.dto.AuditableDto
import cz.smartbrains.qesu.module.stock.dto.StockDto
import cz.smartbrains.qesu.module.stock.movement.dto.StockMovementDto
import cz.smartbrains.qesu.module.user.dto.UserDto
import lombok.EqualsAndHashCode
import lombok.ToString
import org.hibernate.validator.constraints.Length
import java.math.BigInteger
import java.time.LocalDateTime
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@ToString(callSuper = true, exclude = ["stock", "items", "movements"])
@EqualsAndHashCode(callSuper = true)
class InventoryDto : AbstractDto(), AuditableDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var number: BigInteger? = null
    var date: @NotNull LocalDateTime? = null
    var stock: @NotNull StockDto? = null
    var note: @Length(max = 300) String? = null
    var items: @Valid @NotEmpty MutableList<InventoryItemDto>? = ArrayList()

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var movements: List<StockMovementDto> = ArrayList()

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    override var created: LocalDateTime? = null

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    override var createdBy: UserDto? = null

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    override var updated: LocalDateTime? = null

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    override var updatedBy: UserDto? = null

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var editable = false
}