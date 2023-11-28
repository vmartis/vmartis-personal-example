package cz.smartbrains.qesu.module.common.entity

import lombok.ToString
import org.hibernate.validator.constraints.Length
import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.MappedSuperclass

@ToString(callSuper = true)
@MappedSuperclass
abstract class CodeList : AbstractEntity(), Orderable {
    @Column(name = "LABEL", length = 100, nullable = false)
    open var label: @Length(min = 2) String? = null

    @Column(name = "VALID_FROM", nullable = false)
    open var validFrom: LocalDate? = null

    @Column(name = "VALID_TO")
    open var validTo: LocalDate? = null

    @Column(name = "ITEM_ORDER", columnDefinition = "integer", nullable = false)
    override var order = 0
}