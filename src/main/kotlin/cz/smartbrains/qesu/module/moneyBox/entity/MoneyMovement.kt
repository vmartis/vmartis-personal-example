package cz.smartbrains.qesu.module.moneyBox.entity

import cz.smartbrains.qesu.module.invoice.entity.Invoice
import cz.smartbrains.qesu.module.user.entity.User
import cz.smartbrains.qesu.module.common.entity.AbstractEntity
import cz.smartbrains.qesu.module.common.entity.AuditableEntity
import cz.smartbrains.qesu.module.moneyBox.type.AccountingType
import cz.smartbrains.qesu.module.moneyBox.type.MoneyMovementType
import cz.smartbrains.qesu.module.subject.entity.Subject
import lombok.EqualsAndHashCode
import lombok.ToString
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true, exclude = ["moneyBox", "category", "subject", "createdBy", "updatedBy"])
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name = "MONEY_MOVEMENT", indexes = [Index(name = "MONEY_MOVEMENT_DATE_IDX", columnList = "DATE"), Index(name = "MONEY_MOVEMENT_TAXABLE_DATE_IDX", columnList = "TAXABLE_DATE"), Index(name = "MONEY_MOVEMENT_TYPE_IDX", columnList = "TYPE"), Index(name = "MONEY_MOVEMENT_ACCOUNTING_TYPE_IDX", columnList = "ACCOUNTING_TYPE"), Index(name = "MONEY_MOVEMENT_ACTIVE_IDX", columnList = "ACTIVE")])
abstract class MoneyMovement : AbstractEntity(), AuditableEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "MONEY_BOX_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "MONEY_MOVEMENT_MONEY_BOX_FK"), nullable = false)
    open var moneyBox: MoneyBox? = null

    @ManyToOne
    @JoinColumn(name = "SUBJECT_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "MONEY_MOVEMENT_SUBJECT_FK"))
    open var subject: Subject? = null

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "MONEY_MOVEMENT_MONEY_MOVEMENT_CATEGORY_FK"))
    open var category: MoneyMovementCategory? = null

    @ManyToOne
    @JoinColumn(name = "INVOICE_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "MONEY_MOVEMENT_INVOICE_FK"))
    open var invoice: Invoice? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false, length = 20)
    open var type: MoneyMovementType? = null

    @Column(name = "DATE", nullable = false)
    open var date: LocalDateTime? = null

    @Column(name = "TAXABLE_DATE")
    open var taxableDate: LocalDate? = null

    @Column(name = "TOTAL_AMOUNT", precision = 10, scale = 2, columnDefinition = "numeric", nullable = false)
    open var totalAmount: BigDecimal? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "ACCOUNTING_TYPE", nullable = false, length = 20)
    open var accountingType: AccountingType? = null

    @Column(name = "TOTAL_VAT", precision = 10, scale = 2, columnDefinition = "numeric")
    open var totalVat: BigDecimal? = null

    @Column(name = "TOTAL_WITHOUT_VAT", precision = 10, scale = 2, columnDefinition = "numeric", nullable = false)
    open var totalWithoutVat: BigDecimal? = null

    @Column(name = "NOTE", length = 300)
    open var note: String? = null

    @Column(name = "ACTIVE", columnDefinition = "boolean default true", nullable = false)
    open var active = true

    @ManyToOne(optional = false)
    @JoinColumn(name = "CREATED_BY_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "MONEY_MOVEMENT_CREATED_BY_FK"), nullable = false)
    override var createdBy: User? = null

    @ManyToOne
    @JoinColumn(name = "UPDATED_BY_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "MONEY_MOVEMENT_UPDATED_BY_FK"))
    override var updatedBy: User? = null
}