package cz.smartbrains.qesu.module.order.entity

import cz.smartbrains.qesu.module.item.entity.Item
import cz.smartbrains.qesu.module.common.entity.AbstractEntity
import cz.smartbrains.qesu.module.common.entity.Orderable
import cz.smartbrains.qesu.module.company.entity.Company
import lombok.EqualsAndHashCode
import lombok.ToString
import javax.persistence.*

@ToString(callSuper = true, exclude = ["item", "company"])
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "ORDER_ITEM_TEMPLATE", uniqueConstraints = [UniqueConstraint(name = "ORDER_ITEM_TEMPLATE_COMPANY_ITEM_UC", columnNames = ["COMPANY_ID", "ITEM_ID"]), UniqueConstraint(name = "ORDER_ITEM_TEMPLATE_ITEM_ORDER_UC", columnNames = ["ITEM_ORDER"])])
class OrderItemTemplate : AbstractEntity(), Orderable {
    @ManyToOne(optional = false)
    @JoinColumn(name = "ITEM_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "ORDER_ITEM_TEMPLATE_ITEM_FK"), nullable = false)
    var item: Item? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "COMPANY_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "ORDER_ITEM_TEMPLATE_COMPANY_FK"))
    var company: Company? = null

    @Column(name = "ITEM_ORDER", columnDefinition = "integer", nullable = false)
    override var order = 0
}