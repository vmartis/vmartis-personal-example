package cz.smartbrains.qesu.module.stock.movement.entity

import cz.smartbrains.qesu.module.common.entity.AbstractEntity
import cz.smartbrains.qesu.module.item.entity.Item
import lombok.EqualsAndHashCode
import lombok.ToString
import java.math.BigDecimal
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Min

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "STOCK_MOVEMENT_ITEM")
class StockMovementItem : AbstractEntity() {
    @ManyToOne(optional = false)
    @JoinColumn(name = "STOCK_MOVEMENT_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "STOCK_MOVEMENT_ITEM_STOCK_MOVEMENT_FK"), nullable = false)
    var movement: StockMovement? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "ITEM_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "STOCK_MOVEMENT_ITEM_ITEM_FK"), nullable = false)
    var item: Item? = null

    @Column(name = "AMOUNT", precision = 10, scale = 3, columnDefinition = "numeric", nullable = false)
    var amount: BigDecimal? = null

    @Column(name = "PRICE", precision = 10, scale = 2, nullable = false)
    var price: @Min(0) BigDecimal? = null

    @Column(name = "TOTAL_PRICE", precision = 10, scale = 2, nullable = false)
    var totalPrice: BigDecimal? = null

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "STOCK_MOVEMENT_ITEM_ITEM_BATCH", joinColumns = [JoinColumn(name = "STOCK_MOVEMENT_ITEM_ID")], foreignKey = ForeignKey(name = "STOCK_MOVEMENT_ITEM_ITEM_BATCH_STOCK_MOVEMENT_FK"))
    @Column(name = "ITEM_BATCH")
    var itemBatches: List<String> = ArrayList()
    override fun toString(): String {
        return "StockMovementItem(movement=${movement?.id}, item=${item?.id}, amount=$amount, price=$price, totalPrice=$totalPrice, itemBatches=$itemBatches)"
    }
}