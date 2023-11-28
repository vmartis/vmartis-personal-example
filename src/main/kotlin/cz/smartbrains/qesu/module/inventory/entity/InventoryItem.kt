package cz.smartbrains.qesu.module.inventory.entity

import cz.smartbrains.qesu.module.common.entity.AbstractEntity
import cz.smartbrains.qesu.module.item.entity.Item
import lombok.EqualsAndHashCode
import lombok.ToString
import java.math.BigDecimal
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Min

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true, exclude = ["item", "inventory"])
@Entity
@Table(name = "INVENTORY_ITEM")
class InventoryItem : AbstractEntity() {
    @Column(name = "AMOUNT", precision = 13, scale = 3, columnDefinition = "numeric", nullable = false)
    var amount: @Min(0) BigDecimal? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "ITEM_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "INVENTORY_ITEM_ITEM_FK"), nullable = false)
    var item: Item? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "INVENTORY_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "INVENTORY_ITEM_INVENTORY_FK"), nullable = false)
    var inventory: Inventory? = null

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "INVENTORY_ITEM_ITEM_BATCH", joinColumns = [JoinColumn(name = "INVENTORY_ITEM_ID")], foreignKey = ForeignKey(name = "INVENTORY_ITEM_ITEM_BATCH_INVENTORY_ITEM_FK"))
    @Column(name = "ITEM_BATCH")
    var itemBatches: List<String> = ArrayList()
}