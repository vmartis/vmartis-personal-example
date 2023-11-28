package cz.smartbrains.qesu.module.common.entity

import lombok.EqualsAndHashCode
import lombok.ToString
import javax.persistence.Column
import javax.persistence.MappedSuperclass
import javax.validation.constraints.Pattern

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
abstract class ColorableCodeList : CodeList() {
    @Column(name = "COLOR", length = 7)
    var color: @Pattern(regexp = "^[#][a-zA-Z0-9]{6}$") String? = null
}