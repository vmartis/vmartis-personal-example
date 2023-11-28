package cz.smartbrains.qesu.module.moneyBox.entity

import cz.smartbrains.qesu.module.company.entity.CompanyBranch
import cz.smartbrains.qesu.module.common.entity.AbstractEntity
import cz.smartbrains.qesu.module.common.entity.Orderable
import lombok.EqualsAndHashCode
import lombok.ToString
import java.math.BigDecimal
import java.time.LocalDate
import javax.persistence.*

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true, exclude = ["companyBranch"])
@Entity
@Table(name = "MONEY_BOX", uniqueConstraints = [UniqueConstraint(name = "MONEY_BOX_ITEM_ORDER_UC", columnNames = ["ITEM_ORDER"])])
@Inheritance(strategy = InheritanceType.JOINED)
abstract class MoneyBox : AbstractEntity(), Orderable {
    @Column(name = "NAME", length = 200, nullable = false)
    open var name: String? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "COMPANY_BRANCH_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "MONEY_BOX_COMPANY_BRANCH_FK"))
    open var companyBranch: CompanyBranch? = null

    @Column(name = "AMOUNT", precision = 10, scale = 2, columnDefinition = "numeric", nullable = false)
    open var amount: BigDecimal? = null

    @Column(name = "CURRENCY", length = 3, nullable = false)
    open var currency: String? = null

    @Column(name = "ACTIVE", columnDefinition = "boolean default true", nullable = false)
    open var active = true

    @Column(name = "ITEM_ORDER", columnDefinition = "integer", nullable = false)
    override var order = 0

    open val initialAmount: BigDecimal?
        get() = BigDecimal.ZERO


    fun getInitialAmount(date: LocalDate): BigDecimal {
        return if (initialDate == null || initialDate!!.isAfter(date)) {
            BigDecimal.ZERO
        } else {
            initialAmount!!
        }
    }

    open val initialDate: LocalDate?
        get() = LocalDate.MIN
}