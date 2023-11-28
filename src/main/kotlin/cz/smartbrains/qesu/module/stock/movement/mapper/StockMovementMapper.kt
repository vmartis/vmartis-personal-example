package cz.smartbrains.qesu.module.stock.movement.mapper

import cz.smartbrains.qesu.module.stock.movement.dto.StockMovementDto
import cz.smartbrains.qesu.module.stock.movement.entity.IncomeStockMovement
import cz.smartbrains.qesu.module.stock.movement.entity.OutcomeStockMovement
import cz.smartbrains.qesu.module.stock.movement.entity.StockMovement
import org.hibernate.Hibernate
import org.mapstruct.Mapper
import org.springframework.beans.factory.annotation.Autowired

@Mapper(componentModel = "spring")
abstract class StockMovementMapper {
    @Autowired
    private val incomeStockMovementMapper: IncomeStockMovementMapper? = null

    @Autowired
    private val outcomeStockMovementMapper: OutcomeStockMovementMapper? = null
    fun doToDto(stockMovement: StockMovement?): StockMovementDto? {
        if (stockMovement == null) {
            return null
        }
        return if (Hibernate.getClass(stockMovement) == IncomeStockMovement::class.java) {
            incomeStockMovementMapper!!.doToDto(Hibernate.unproxy(stockMovement) as IncomeStockMovement)
        } else if (Hibernate.getClass(stockMovement) == OutcomeStockMovement::class.java) {
            outcomeStockMovementMapper!!.doToDto(Hibernate.unproxy(stockMovement) as OutcomeStockMovement)
        } else {
            throw IllegalStateException("Not supported money movement type.")
        }
    }
}