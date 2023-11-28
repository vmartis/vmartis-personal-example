package cz.smartbrains.qesu.module.moneyBox.entity

import cz.smartbrains.qesu.module.common.entity.Orderable
import lombok.EqualsAndHashCode
import java.math.BigDecimal
import javax.persistence.*

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "CASH_MONEY_BOX")
@PrimaryKeyJoinColumn(foreignKey = ForeignKey(name = "CASH_MONEY_BOX_MONEY_BOX_FK"))
class CashMoneyBox : MoneyBox(), Orderable {
    @Column(name = "AMOUNT_CASH", precision = 10, scale = 2, columnDefinition = "numeric", nullable = false)
    var amountCash: BigDecimal? = null

    @Column(name = "AMOUNT_CHEQUE", precision = 10, scale = 2, columnDefinition = "numeric", nullable = false)
    var amountCheque: BigDecimal? = null
}