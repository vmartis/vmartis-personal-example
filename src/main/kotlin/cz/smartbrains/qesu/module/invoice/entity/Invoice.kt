package cz.smartbrains.qesu.module.invoice.entity

import cz.smartbrains.qesu.module.bank.entity.BankAccount
import cz.smartbrains.qesu.module.common.entity.AbstractEntity
import cz.smartbrains.qesu.module.company.entity.Company
import cz.smartbrains.qesu.module.invoice.type.InvoiceType
import cz.smartbrains.qesu.module.invoice.type.PaymentMethod
import cz.smartbrains.qesu.module.moneyBox.entity.MoneyMovement
import lombok.EqualsAndHashCode
import lombok.ToString
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Pattern

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true, exclude = ["supplier", "bankAccount", "subscriber", "items", "movements"])
@Entity
@Table(name = "INVOICE", indexes = [Index(name = "IDX_INVOICE_NUMBER", columnList = "NUMBER"), Index(name = "IDX_INVOICE_DATE_OF_ISSUE", columnList = "DATE_OF_ISSUE"), Index(name = "IDX_INVOICE_DUE_DATE", columnList = "DUE_DATE"), Index(name = "IDX_INVOICE_TAXABLE_DATE", columnList = "TAXABLE_DATE"), Index(name = "IDX_INVOICE_TYPE", columnList = "TYPE")])
class Invoice : AbstractEntity() {
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false, length = 20)
    var type: InvoiceType? = null

    @Column(name = "NUMBER", length = 20, nullable = false)
    var number: String? = null

    @Column(name = "CONSTANT_SYMBOL", length = 4)
    var constantSymbol: @Pattern(regexp = "^[0-9]{0,4}$") String? = null

    @Column(name = "VARIABLE_SYMBOL", length = 10)
    var variableSymbol: @Pattern(regexp = "^[0-9]{0,10}$") String? = null

    @Column(name = "SPECIFIC_SYMBOL", length = 10)
    var specificSymbol: @Pattern(regexp = "^[0-9]{0,10}$") String? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "SUPPLIER_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "INVOICE_SUPPLIER_FK"), nullable = false)
    var supplier: Company? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "SUBSCRIBER_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "INVOICE_SUBSCRIBER_FK"), nullable = false)
    var subscriber: Company? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "BANK_ACCOUNT_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "INVOICE_BANK_ACCOUNT_FK"))
    var bankAccount: BankAccount? = null

    @Column(name = "TOTAL_AMOUNT", precision = 10, scale = 2, nullable = false)
    var totalAmount: BigDecimal? = null

    @Column(name = "TOTAL_VAT", precision = 10, scale = 2, nullable = false)
    var totalVat: BigDecimal? = null

    @Column(name = "UNPAID_AMOUNT", precision = 10, scale = 2, nullable = false)
    var unpaidAmount: BigDecimal? = null

    @Column(name = "CURRENCY", length = 3, nullable = false)
    var currency: String? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "PAYMENT_METHOD", length = 20, nullable = false)
    var paymentMethod: PaymentMethod? = null

    @Column(name = "DATE_OF_ISSUE", nullable = false)
    var dateOfIssue: LocalDate? = null

    @Column(name = "DUE_DATE", nullable = false)
    var dueDate: LocalDate? = null

    @Column(name = "TAXABLE_DATE")
    var taxableDate: LocalDate? = null

    @Column(name = "TRANSFERRED_VAT", columnDefinition = "boolean default false", nullable = false)
    var transferredVat = false

    @Column(name = "REFERENCE", length = 100)
    var reference: String? = null

    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "invoice")
    @OrderBy("ID ASC")
    var items: MutableList<InvoiceItem> = ArrayList()

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "invoice")
    var movements: MutableList<MoneyMovement> = ArrayList()

    @get:Transient
    val totalAmountWithoutVat: BigDecimal
        get() = totalAmount!!.subtract(totalVat)

    @get:Transient
    val isPaid: Boolean
        get() = unpaidAmount!!.compareTo(BigDecimal.ZERO) == 0

    @get:Transient
    val isPartiallyPaid: Boolean
        get() = unpaidAmount!!.compareTo(BigDecimal.ZERO) > 0 && unpaidAmount!!.compareTo(totalAmount) < 0
}