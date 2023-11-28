package cz.smartbrains.qesu.module.order.entity

import cz.smartbrains.qesu.module.item.entity.Item
import cz.smartbrains.qesu.module.common.entity.AbstractEntity
import lombok.ToString
import java.math.BigDecimal
import javax.persistence.*
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@ToString(callSuper = true, exclude = ["order", "item"])
@Entity
@Table(name = "ORDER_ITEM")
class OrderItem : AbstractEntity() {
    @ManyToOne(optional = false)
    @JoinColumn(name = "ORDER_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "ORDER_ITEM_ORDER_FK"), nullable = false)
    var order: Order? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "ITEM_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "ORDER_ITEM_ITEM_FK"), nullable = false)
    var item: Item? = null

    @Column(name = "ORDERED", precision = 13, scale = 3, nullable = false)
    var ordered = BigDecimal.ZERO

    @Column(name = "DELIVERED", precision = 13, scale = 3, nullable = false)
    var delivered = BigDecimal.ZERO

    @Column(name = "RETURNED", precision = 13, scale = 3, nullable = false)
    var returned = BigDecimal.ZERO

    @Column(name = "ITEM_PRICE", precision = 10, scale = 2, nullable = false)
    var itemPrice: BigDecimal? = null

    @Column(name = "VAT_RATE", precision = 5, scale = 2, nullable = false)
    var vatRate: @Min(0) @Max(100) BigDecimal? = null
}