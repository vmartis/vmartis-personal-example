package cz.smartbrains.qesu.module.stock.entity

import cz.smartbrains.qesu.module.common.entity.AbstractEntity
import cz.smartbrains.qesu.module.common.entity.Orderable
import cz.smartbrains.qesu.module.company.entity.CompanyBranch
import lombok.EqualsAndHashCode
import lombok.ToString
import javax.persistence.*

@ToString(callSuper = true, exclude = ["companyBranch"])
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "STOCK", uniqueConstraints = [UniqueConstraint(name = "STOCK_COMPANY_BRANCH_DEFAULT_FOR_DISPATCH_UC", columnNames = ["COMPANY_BRANCH_ID"]), UniqueConstraint(name = "STOCK_ITEM_ORDER_UC", columnNames = ["ITEM_ORDER"]), UniqueConstraint(name = "STOCK_NAME_UC", columnNames = ["NAME"])])
class Stock : AbstractEntity(), Orderable {
    @Column(name = "NAME", length = 100, nullable = false)
    var name: String? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "COMPANY_BRANCH_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "STOCK_COMPANY_BRANCH_FK"))
    var companyBranch: CompanyBranch? = null

    @Column(name = "CURRENCY", length = 3, nullable = false)
    var currency: String? = null

    @Column(name = "ITEM_ORDER", columnDefinition = "integer", nullable = false)
    override var order = 0

    @Column(name = "DEFAULT_FOR_DISPATCH", columnDefinition = "boolean default false", nullable = false)
    var defaultForDispatch = false

    @Column(name = "ACTIVE", columnDefinition = "boolean default true", nullable = false)
    var active = true
}