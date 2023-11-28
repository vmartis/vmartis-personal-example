package cz.smartbrains.qesu.module.common.dto

import lombok.ToString
import org.hibernate.validator.constraints.Length
import java.time.LocalDate
import javax.validation.constraints.NotNull

@ToString(callSuper = true)
open class CodeListBaseDto : AbstractDto(), CodeListDto {
    override var label: @NotNull @Length(min = 2, max = 100) String? = null
    override var validFrom: LocalDate? = null
    override var validTo: LocalDate? = null
    override var order = 0
}