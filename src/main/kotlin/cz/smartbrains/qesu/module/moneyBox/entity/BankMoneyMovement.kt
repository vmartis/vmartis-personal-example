package cz.smartbrains.qesu.module.moneyBox.entity

import cz.smartbrains.qesu.module.bank.entity.BankAccount
import cz.smartbrains.qesu.module.bank.entity.BankTransaction
import lombok.EqualsAndHashCode
import lombok.ToString
import javax.persistence.*

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, exclude = ["bankTransaction", "correspondingBankAccount"])
@Entity
@Table(name = "BANK_MONEY_MOVEMENT")
@PrimaryKeyJoinColumn(foreignKey = ForeignKey(name = "BANK_MONEY_MOVEMENT_MONEY_MOVEMENT_FK"))
class BankMoneyMovement : MoneyMovement() {
    @ManyToOne(optional = false)
    @JoinColumn(name = "BANK_TRANSACTION_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "BANK_MONEY_MOVEMENT_BANK_TRANSACTION_FK"), nullable = false)
    var bankTransaction: BankTransaction? = null

    @ManyToOne
    @JoinColumn(name = "CORRESPONDING_BANK_ACCOUNT_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "BANK_MONEY_MOVEMENT_CORRESPONDING_BANK_ACCOUNT_FK"))
    var correspondingBankAccount: BankAccount? = null

    //FK is created independent in flyway script
    @Column(name = "MAIN_MOVEMENT_ID")
    var mainMovementId: Long? = null
}