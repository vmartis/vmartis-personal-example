package cz.smartbrains.qesu.module.stock.dto

import cz.smartbrains.qesu.module.common.dto.AbstractDto
import cz.smartbrains.qesu.module.common.entity.Orderable
import cz.smartbrains.qesu.module.company.dto.CompanyBranchDto
import lombok.ToString
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

@ToString(callSuper = true, exclude = ["companyBranch"])
class StockDto : AbstractDto(), Orderable {
    var name: @Length(max = 100) @NotEmpty String? = null
    var companyBranch: @NotNull CompanyBranchDto? = null
    var currency: @NotNull @Length(max = 3) @Pattern(regexp = "^[A-Z]{3}$") String? = null
    override var order = 0
    var defaultForDispatch = false
    var active = false
}