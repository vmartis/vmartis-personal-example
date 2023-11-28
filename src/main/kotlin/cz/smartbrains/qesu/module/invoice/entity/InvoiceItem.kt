package cz.smartbrains.qesu.module.invoice.entity

import cz.smartbrains.qesu.module.common.entity.AbstractEntity
import cz.smartbrains.qesu.module.unit.entity.Unit
import lombok.EqualsAndHashCode
import lombok.ToString
import java.math.BigDecimal
import java.math.RoundingMode
import javax.persistence.*
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true, exclude = ["invoice"])
@Entity
@Table(name = "INVOICE_ITEM")
class InvoiceItem : AbstractEntity() {
    @Column(name = "NAME", length = 200, nullable = false)
    var name: String? = null

    @Column(name = "PRICE", precision = 10, scale = 2, nullable = false)
    var price: BigDecimal? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "UNIT_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "INVOICE_ITEM_UNIT_FK"), nullable = false)
    var unit: Unit? = null

    @Column(name = "NUMBER", precision = 13, scale = 5, nullable = false)
    var number: BigDecimal? = null

    @Column(name = "VAT_RATE", precision = 5, scale = 2, nullable = false)
    var vatRate: @Min(0) @Max(100) BigDecimal? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "INVOICE_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "INVOICE_INVOICE_ITEM_FK"), nullable = false)
    var invoice: Invoice? = null
    val total: BigDecimal
        get() = number!!.multiply(price)
    val vat: BigDecimal
        get() = total.multiply(vatRate).divide(VAT_DIVISOR, VAT_SCALE, RoundingMode.HALF_UP)
    val totalWithVat: BigDecimal
        get() = total.add(vat)

    companion object {
        const val VAT_SCALE = 2
        private val VAT_DIVISOR = BigDecimal.valueOf(100)
    }
}