package cz.smartbrains.qesu.module.item.dto

import cz.smartbrains.qesu.module.stock.dto.StockDto
import cz.smartbrains.qesu.module.common.dto.AbstractDto
import lombok.EqualsAndHashCode
import lombok.ToString
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
class ItemBatchDto : AbstractDto() {
    var name: @NotEmpty @Length(min = 1, max = 100) String? = null
    var item: @NotNull ItemDto? = null
    var stock: @NotNull StockDto? = null
    var active = false
}