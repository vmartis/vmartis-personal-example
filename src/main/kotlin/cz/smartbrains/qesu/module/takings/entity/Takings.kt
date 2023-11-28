package cz.smartbrains.qesu.module.takings.entity

import cz.smartbrains.qesu.module.user.entity.User
import cz.smartbrains.qesu.module.cashbox.entity.CashBox
import cz.smartbrains.qesu.module.common.entity.AbstractEntity
import cz.smartbrains.qesu.module.common.entity.AuditableEntity
import lombok.EqualsAndHashCode
import lombok.ToString
import java.math.BigDecimal
import java.time.LocalDate
import javax.persistence.*

@ToString(callSuper = true, exclude = ["cashBox", "createdBy", "updatedBy"])
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "TAKINGS", indexes = [Index(name = "TAKINGS_DATE_IDX", columnList = "DATE"), Index(name = "TAKINGS_CURRENCY_IDX", columnList = "CURRENCY")])
class Takings : AbstractEntity(), AuditableEntity {
    @Column(name = "DATE", nullable = false)
    var date: LocalDate? = null

    @Column(name = "CURRENCY", length = 3, nullable = false)
    var currency: String? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "CASH_BOX_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "TAKING_CASH_BOX_FK"))
    var cashBox: CashBox? = null

    @Column(name = "AMOUNT_CASH", precision = 10, scale = 2, columnDefinition = "numeric", nullable = false)
    var amountCash: BigDecimal? = null

    @Column(name = "AMOUNT_CHEQUE", precision = 10, scale = 2, columnDefinition = "numeric", nullable = false)
    var amountCheque: BigDecimal? = null

    @Column(name = "AMOUNT_CARD", precision = 10, scale = 2, columnDefinition = "numeric", nullable = false)
    var amountCard: BigDecimal? = null

    @Column(name = "TOTAL_VAT_AMOUNT", precision = 10, scale = 2, columnDefinition = "numeric", nullable = false)
    var totalVatAmount: BigDecimal? = null

    @Column(name = "TOTAL_CASH_AMOUNT", precision = 10, scale = 2, columnDefinition = "numeric", nullable = false)
    var totalCashAmount: BigDecimal? = null

    @Column(name = "TOTAL_CHEQUE_AMOUNT", precision = 10, scale = 2, columnDefinition = "numeric", nullable = false)
    var totalChequeAmount: BigDecimal? = null

    @Column(name = "TRANSFERRED", columnDefinition = "boolean default false", nullable = false)
    var transferred = false

    @ManyToOne(optional = false)
    @JoinColumn(name = "CREATED_BY_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "TAKING_CREATED_BY_FK"), nullable = false)
    override var createdBy: User? = null

    @ManyToOne
    @JoinColumn(name = "UPDATED_BY_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "TAKING_UPDATED_BY_FK"))
    override var updatedBy: User? = null

    val totalFiscal: BigDecimal
        get() = if (initialized()) {
            amountCash!!.add(amountCheque)
        } else BigDecimal.ZERO
    val cashNonFiscal: BigDecimal
        get() = if (initialized()) {
            totalCashAmount!!.subtract(amountCash)
        } else BigDecimal.ZERO
    val chequeNonFiscal: BigDecimal
        get() = if (initialized()) {
            totalChequeAmount!!.subtract(amountCheque)
        } else BigDecimal.ZERO
    val totalNonFiscal: BigDecimal
        get() = if (initialized()) {
            totalCashAmount!!.add(totalChequeAmount).subtract(totalFiscal)
        } else BigDecimal.ZERO

    private fun initialized(): Boolean {
        return amountCash != null && amountCheque != null && amountCard != null && totalVatAmount != null && totalCashAmount != null && totalChequeAmount != null
    }
}