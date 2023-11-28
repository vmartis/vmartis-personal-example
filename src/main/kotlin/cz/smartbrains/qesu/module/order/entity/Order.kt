package cz.smartbrains.qesu.module.order.entity

import cz.smartbrains.qesu.module.user.entity.User
import cz.smartbrains.qesu.module.common.entity.AbstractEntity
import cz.smartbrains.qesu.module.common.entity.AuditableEntity
import cz.smartbrains.qesu.module.company.entity.Company
import cz.smartbrains.qesu.module.company.entity.CompanyBranch
import cz.smartbrains.qesu.module.order.type.OrderState
import lombok.ToString
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import java.util.*
import javax.persistence.*

@ToString(callSuper = true, exclude = ["supplierBranch", "subscriber", "subscriberBranch", "statement", "items", "createdBy", "updatedBy"])
@Entity
@Table(name = "ORDER", indexes = [Index(name = "ORDER_DATE_IDX", columnList = "DATE")], uniqueConstraints = [UniqueConstraint(name = "ORDER_NUMBER_UC", columnNames = ["NUMBER"])])
class Order : AbstractEntity(), AuditableEntity {
    @Column(name = "NUMBER", nullable = false)
    var number: BigInteger? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "STATE", nullable = false, length = 9)
    var state: OrderState? = null

    @Column(name = "DATE", nullable = false)
    var date: LocalDate? = null

    @Column(name = "CURRENCY", length = 3, nullable = false)
    var currency: String? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "SUPPLIER_BRANCH_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "ORDER_SUPPLIER_BRANCH_FK"), nullable = false)
    var supplierBranch: CompanyBranch? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "SUBSCRIBER_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "ORDER_SUBSCRIBER_FK"), nullable = false)
    var subscriber: Company? = null

    @ManyToOne
    @JoinColumn(name = "SUBSCRIBER_BRANCH_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "ORDER_SUBSCRIBER_BRANCH_FK"))
    var subscriberBranch: CompanyBranch? = null

    @ManyToOne
    @JoinColumn(name = "STATEMENT_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "ORDER_STATEMENT_FK"))
    var statement: OrderStatement? = null

    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "order")
    @OrderBy("ID ASC")
    var items: MutableList<OrderItem> = ArrayList()

    @Column(name = "TOTAL_PRICE", precision = 10, scale = 2, nullable = false)
    var totalPrice: BigDecimal? = null

    @Column(name = "TOTAL_VAT", precision = 10, scale = 2, nullable = false)
    var totalVat: BigDecimal? = null

    @Column(name = "TOTAL_DELIVERED_PRICE", precision = 10, scale = 2, nullable = false)
    var totalDeliveredPrice: BigDecimal? = null

    @Column(name = "TOTAL_DELIVERED_VAT", precision = 10, scale = 2, nullable = false)
    var totalDeliveredVat: BigDecimal? = null

    @Column(name = "SUBSCRIBER_ORDER_NUMBER", length = 50)
    var subscriberOrderNumber: String? = null

    @Column(name = "NOTE", columnDefinition = "text")
    var note: String? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "CREATED_BY_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "ORDER_CREATED_BY_FK"), nullable = false)
    override var createdBy: User? = null

    @ManyToOne
    @JoinColumn(name = "UPDATED_BY_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "ORDER_UPDATED_BY_FK"))
    override var updatedBy: User? = null

    val billed: Boolean
        get() = statement != null
}