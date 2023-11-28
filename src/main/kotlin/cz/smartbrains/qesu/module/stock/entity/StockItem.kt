package cz.smartbrains.qesu.module.stock.entity

import cz.smartbrains.qesu.module.common.entity.AbstractEntity
import cz.smartbrains.qesu.module.item.entity.Item
import lombok.EqualsAndHashCode
import lombok.ToString
import java.math.BigDecimal
import javax.persistence.*
import javax.validation.constraints.Min

@ToString(callSuper = true, exclude = ["stock", "item"])
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "STOCK_ITEM", uniqueConstraints = [UniqueConstraint(name = "STOCK_ITEM_STOCK_ID_ITEM_ID_UC", columnNames = ["STOCK_ID", "ITEM_ID"])])
class StockItem : AbstractEntity() {
    @ManyToOne(optional = false)
    @JoinColumn(name = "STOCK_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "STOCK_ITEM_STOCK_FK"), nullable = false)
    var stock: Stock? = null

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ITEM_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "PRICE_LIST_ITEM_FK"), nullable = false)
    var item: Item? = null

    @Column(name = "PRICE", precision = 10, scale = 2, columnDefinition = "numeric", nullable = false)
    var price: @Min(0) BigDecimal? = null

    @Column(name = "AMOUNT", precision = 10, scale = 3, columnDefinition = "numeric", nullable = false)
    var amount: @Min(0) BigDecimal? = null

    @Column(name = "MIN_AMOUNT", precision = 10, scale = 3, columnDefinition = "numeric", nullable = false)
    var minAmount: @Min(0) BigDecimal? = null

    @Column(name = "ACTIVE", columnDefinition = "boolean default true", nullable = false)
    var active = true
}