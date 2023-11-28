package cz.smartbrains.qesu.module.item.entity

import cz.smartbrains.qesu.module.common.entity.AbstractEntity
import lombok.ToString
import java.math.BigDecimal
import javax.persistence.*
import javax.validation.constraints.Positive

@ToString(callSuper = true)
@Entity
@Table(name = "ITEM_DISPATCH")
class ItemDispatch : AbstractEntity() {
    @ManyToOne(optional = false)
    @JoinColumn(name = "ITEM_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "ITEM_EXPEDITION_MAIN_ITEM_FK"), nullable = false)
    var item: Item? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "SUB_ITEM_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "ITEM_EXPEDITION_SUB_ITEM_FK"), nullable = false)
    var subItem: Item? = null

    @Column(name = "AMOUNT", precision = 13, scale = 3, columnDefinition = "numeric", nullable = false)
    var amount: @Positive BigDecimal? = null
}