package cz.smartbrains.qesu.module.invoice.entity

import cz.smartbrains.qesu.module.common.entity.CodeList
import lombok.EqualsAndHashCode
import lombok.ToString
import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Index
import javax.persistence.Table
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Entity
@Table(name = "VAT_RATE", indexes = [Index(name = "IDX_VAT_RATE_LABEL", columnList = "LABEL")])
class VatRate : CodeList() {
    @Column(name = "RATE", precision = 5, scale = 2, nullable = false)
    var rate: @Min(0) @Max(100) BigDecimal? = null

    @Column(name = "COUNTRY", length = 3, nullable = false)
    var country: String? = null
}