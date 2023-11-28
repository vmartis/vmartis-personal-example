package cz.smartbrains.qesu.module.invoice.entity

import cz.smartbrains.qesu.module.common.entity.AbstractEntity
import cz.smartbrains.qesu.module.common.entity.Orderable
import cz.smartbrains.qesu.module.invoice.type.InvoiceSeriesFormat
import lombok.EqualsAndHashCode
import lombok.ToString
import javax.persistence.*

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Entity
@Table(name = "INVOICE_SERIES", indexes = [Index(name = "INVOICE_SERIES_PREFIX_IDX", columnList = "PREFIX"), Index(name = "INVOICE_SERIES_ITEM_ORDER_IDX", columnList = "ITEM_ORDER"), Index(name = "INVOICE_SERIES_PREFIX_YEAR_IDX", columnList = "PREFIX,YEAR", unique = true)])
class InvoiceSeries : AbstractEntity(), Orderable {
    @Column(name = "NAME", length = 100, nullable = false)
    var name: String? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "FORMAT", nullable = false, length = 20)
    var format: InvoiceSeriesFormat? = null

    @Column(name = "PREFIX", length = 5, nullable = false)
    var prefix: String? = null

    @Column(name = "YEAR", nullable = false)
    var year = 0

    @Column(name = "INDEX", nullable = false)
    var index = 0

    @Column(name = "ACTIVE", columnDefinition = "boolean default true", nullable = false)
    var active = true

    @Column(name = "DEFAULT_FLAG", columnDefinition = "boolean default false", nullable = false)
    var defaultFlag = false

    @Column(name = "ITEM_ORDER", columnDefinition = "integer", nullable = false)
    override var order = 0
}