package cz.smartbrains.qesu.module.item.dto

import cz.smartbrains.qesu.module.common.dto.AbstractDto
import cz.smartbrains.qesu.module.common.dto.ColorableCodeListDto
import cz.smartbrains.qesu.module.item.type.ItemOriginType
import cz.smartbrains.qesu.module.item.type.ItemType
import cz.smartbrains.qesu.module.unit.dto.UnitDto
import lombok.ToString
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotNull

@ToString(callSuper = true)
class ItemDto : AbstractDto() {
    var type: @NotNull ItemType? = null
    var name: @NotNull @Length(min = 2, max = 200) String? = null
    var description: @NotNull @Length(max = 200) String? = null
    var code: @NotNull @Length(max = 50) String? = null
    var unit: @NotNull UnitDto? = null
    var category: @NotNull ColorableCodeListDto? = null
    var scale = 0
    var origin: @NotNull ItemOriginType? = null
    var active = false
    var forStock = false
    var forPurchase = false
    var batchEvidence = false
}