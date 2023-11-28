package cz.smartbrains.qesu.module.common.dto

import lombok.EqualsAndHashCode
import lombok.ToString
import javax.validation.constraints.Pattern

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
open class ColorableCodeListDto : CodeListBaseDto() {
    var color: @Pattern(regexp = "^[#][a-zA-Z0-9]{6}$") String? = null
}