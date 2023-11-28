package cz.smartbrains.qesu.module.moneyBox.entity

import lombok.EqualsAndHashCode
import lombok.ToString
import java.math.BigDecimal
import javax.persistence.*

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "CASH_MONEY_MOVEMENT")
@PrimaryKeyJoinColumn(foreignKey = ForeignKey(name = "CASH_MONEY_MOVEMENT_MONEY_MOVEMENT_FK"))
class CashMoneyMovement : MoneyMovement() {
    @Column(name = "AMOUNT_CASH", precision = 10, scale = 2, columnDefinition = "numeric", nullable = false)
    var amountCash: BigDecimal? = null

    @Column(name = "AMOUNT_CHEQUE", precision = 10, scale = 2, columnDefinition = "numeric", nullable = false)
    var amountCheque: BigDecimal? = null
}