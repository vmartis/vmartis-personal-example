package cz.smartbrains.qesu.module.item.entity

import cz.smartbrains.qesu.module.stock.entity.Stock
import cz.smartbrains.qesu.module.common.entity.AbstractEntity
import lombok.EqualsAndHashCode
import lombok.ToString
import javax.persistence.*

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "ITEM_BATCH", indexes = [Index(name = "ITEM_BATCH_NAME_IDX", columnList = "NAME")], uniqueConstraints = [UniqueConstraint(name = "ITEM_BATCH_NAME_STOCK_ITEM_UC", columnNames = ["NAME", "STOCK_ID", "ITEM_ID"])])
class ItemBatch : AbstractEntity() {
    @Column(name = "NAME", length = 100, nullable = false)
    var name: String? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "ITEM_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "ITEM_BATCH_ITEM_FK"), nullable = false)
    var item: Item? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "STOCK_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "ITEM_BATCH_STOCK_FK"), nullable = false)
    var stock: Stock? = null

    @Column(name = "ACTIVE", nullable = false)
    var active = true
}