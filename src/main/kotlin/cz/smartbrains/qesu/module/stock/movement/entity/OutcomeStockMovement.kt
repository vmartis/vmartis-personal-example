package cz.smartbrains.qesu.module.stock.movement.entity

import cz.smartbrains.qesu.module.stock.movement.type.OutcomeStockMovementType
import cz.smartbrains.qesu.module.stock.movement.type.StockMovementType
import lombok.EqualsAndHashCode
import lombok.ToString
import javax.persistence.*

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, exclude = ["incomeMovement"])
@Entity
@Table(name = "OUTCOME_STOCK_MOVEMENT")
@PrimaryKeyJoinColumn(foreignKey = ForeignKey(name = "OUTCOME_STOCK_MOVEMENT_STOCK_MOVEMENT_FK"))
class OutcomeStockMovement : StockMovement(StockMovementType.OUTCOME) {
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false, length = 20)
    var type: OutcomeStockMovementType? = null

    @OneToOne(mappedBy = "outcomeMovement", fetch = FetchType.EAGER)
    var incomeMovement: IncomeStockMovement? = null
}