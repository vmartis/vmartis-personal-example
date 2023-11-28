package cz.smartbrains.qesu.module.item.entity

import cz.smartbrains.qesu.module.common.entity.AbstractEntity
import cz.smartbrains.qesu.module.item.category.entity.ItemCategory
import cz.smartbrains.qesu.module.item.type.ItemOriginType
import cz.smartbrains.qesu.module.item.type.ItemType
import cz.smartbrains.qesu.module.unit.entity.Unit
import lombok.ToString
import javax.persistence.*

@ToString(callSuper = true)
@Entity
@Table(name = "ITEM", indexes = [Index(name = "ITEM_NAME_IDX", columnList = "NAME")])
class Item : AbstractEntity() {
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false, length = 7)
    var type: ItemType? = null

    @Column(name = "NAME", length = 200, nullable = false)
    var name: String? = null

    @Column(name = "DESCRIPTION", length = 200, nullable = false)
    var description: String? = null

    @Column(name = "CODE", length = 50, nullable = false)
    var code: String? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "UNIT_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "ITEM_UNIT_FK"), nullable = false)
    var unit: Unit? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "CATEGORY_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "ITEM_ITEM_CATEGORY_FK"), nullable = false)
    var category: ItemCategory? = null

    @Column(name = "SCALE", nullable = false)
    var scale = 0

    @Enumerated(EnumType.STRING)
    @Column(name = "ORIGIN", nullable = false, length = 10)
    var origin: ItemOriginType? = null

    @Column(name = "ACTIVE", nullable = false)
    var active = true

    @Column(name = "FOR_STOCK", nullable = false)
    var forStock = false

    @Column(name = "FOR_PURCHASE", nullable = false)
    var forPurchase = false

    @Column(name = "BATCH_EVIDENCE", nullable = false)
    var batchEvidence = false
}