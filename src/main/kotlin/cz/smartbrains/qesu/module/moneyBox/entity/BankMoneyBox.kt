package cz.smartbrains.qesu.module.moneyBox.entity

import cz.smartbrains.qesu.module.bank.entity.BankAccount
import cz.smartbrains.qesu.module.common.entity.Orderable
import lombok.EqualsAndHashCode
import lombok.ToString
import java.math.BigDecimal
import java.time.LocalDate
import javax.persistence.*

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true, exclude = ["bankAccount"])
@Entity
@Table(name = "BANK_MONEY_BOX")
@PrimaryKeyJoinColumn(foreignKey = ForeignKey(name = "BANK_MONEY_BOX_MONEY_BOX_FK"))
class BankMoneyBox : MoneyBox(), Orderable {
    @OneToOne
    @JoinColumn(name = "BANK_ACCOUNT_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "BANK_MONEY_BOX_BANK_ACCOUNT_FK"), nullable = false)
    var bankAccount: BankAccount? = null

    @Column(name = "INITIAL_AMOUNT", precision = 10, scale = 2, columnDefinition = "numeric", nullable = false)
    override var initialAmount: BigDecimal? = BigDecimal.ZERO

    @Column(name = "INITIAL_DATE", nullable = false)
    override var initialDate: LocalDate? = LocalDate.MIN
}