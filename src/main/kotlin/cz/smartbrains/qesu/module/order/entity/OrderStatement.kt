package cz.smartbrains.qesu.module.order.entity

import cz.smartbrains.qesu.module.invoice.entity.Invoice
import cz.smartbrains.qesu.module.user.entity.User
import cz.smartbrains.qesu.module.common.entity.AbstractEntity
import cz.smartbrains.qesu.module.common.entity.AuditableEntity
import lombok.EqualsAndHashCode
import lombok.ToString
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import java.math.BigInteger
import java.time.LocalDate
import java.util.*
import javax.persistence.*

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, exclude = ["orders", "invoices", "createdBy", "updatedBy"])
@Entity
@Table(name = "ORDER_STATEMENT", indexes = [Index(name = "ORDER_STATEMENT_DATE_IDX", columnList = "DATE"), Index(name = "ORDER_STATEMENT_NUMBER_IDX", columnList = "NUMBER", unique = true)])
class OrderStatement : AbstractEntity(), AuditableEntity {
    @Column(name = "NUMBER", nullable = false, unique = true)
    var number: BigInteger? = null

    @Column(name = "DATE", nullable = false)
    var date: LocalDate? = null

    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "statement")
    @OrderBy("DATE ASC")
    var orders: MutableList<Order> = ArrayList()

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "ORDER_STATEMENT_ID", foreignKey = ForeignKey(name = "INVOICE_ORDER_STATEMENT_FK"))
    var invoices: MutableSet<Invoice> = HashSet()

    @Column(name = "NOTE", columnDefinition = "text")
    var note: String? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "CREATED_BY_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "ORDER_CREATED_BY_FK"), nullable = false)
    override var createdBy: User? = null

    @ManyToOne
    @JoinColumn(name = "UPDATED_BY_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "ORDER_UPDATED_BY_FK"))
    override var updatedBy: User? = null
}