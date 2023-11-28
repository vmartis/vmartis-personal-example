package cz.smartbrains.qesu.module.order.dto

import cz.smartbrains.qesu.module.item.dto.ItemDto
import cz.smartbrains.qesu.module.common.dto.AbstractDto
import cz.smartbrains.qesu.module.common.entity.Orderable
import cz.smartbrains.qesu.module.company.dto.CompanyDto
import lombok.EqualsAndHashCode
import lombok.ToString
import javax.validation.constraints.NotNull

@ToString(callSuper = true, exclude = ["item", "company"])
@EqualsAndHashCode(callSuper = true)
class OrderItemTemplateDto : AbstractDto(), Orderable {
    var item: @NotNull ItemDto? = null
    var company: @NotNull CompanyDto? = null
    override var order = 0
}