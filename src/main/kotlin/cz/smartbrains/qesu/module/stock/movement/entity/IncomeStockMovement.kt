package cz.smartbrains.qesu.module.stock.movement.entity

import cz.smartbrains.qesu.module.company.entity.Company
import cz.smartbrains.qesu.module.stock.movement.type.IncomeStockMovementType
import cz.smartbrains.qesu.module.stock.movement.type.StockMovementType
import lombok.EqualsAndHashCode
import lombok.ToString
import javax.persistence.*

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "INCOME_STOCK_MOVEMENT")
@PrimaryKeyJoinColumn(foreignKey = ForeignKey(name = "INCOME_STOCK_MOVEMENT_STOCK_MOVEMENT_FK"))
class IncomeStockMovement : StockMovement(StockMovementType.INCOME) {
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false, length = 20)
    var type: IncomeStockMovementType? = null

    @ManyToOne
    @JoinColumn(name = "SUPPLIER_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "INCOME_STOCK_MOVEMENT_SUPPLIER_FK"))
    var supplier: Company? = null

    @Column(name = "DELIVERY_NOTE", length = 50)
    var deliveryNote: String? = null

    @OneToOne
    @JoinColumn(name = "OUTCOME_MOVEMENT_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "INCOME_STOCK_MOVEMENT_OUTCOME_MOVEMENT_ID_FK"))
    var outcomeMovement: OutcomeStockMovement? = null

    override fun toString(): String {
        return "IncomeStockMovement(type=$type, supplier=${supplier?.id}, deliveryNote=$deliveryNote, outcomeMovement=${outcomeMovement?.id}) ${super.toString()}"
    }
}