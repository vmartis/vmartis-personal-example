package cz.smartbrains.qesu.module.moneyBox.mapper

import cz.smartbrains.qesu.module.moneyBox.dto.MoneyMovementDto
import cz.smartbrains.qesu.module.moneyBox.entity.BankMoneyMovement
import cz.smartbrains.qesu.module.moneyBox.entity.CashMoneyMovement
import cz.smartbrains.qesu.module.moneyBox.entity.MoneyMovement
import org.hibernate.Hibernate
import org.mapstruct.Mapper
import org.springframework.beans.factory.annotation.Autowired

@Mapper(componentModel = "spring")
abstract class MoneyMovementMapper {
    @Autowired
    private val bankMoneyMovementMapper: BankMoneyMovementMapper? = null
    @Autowired
    private val cashMoneyMovementMapper: CashMoneyMovementMapper? = null

    fun doToDto(moneyMovement: MoneyMovement?): MoneyMovementDto? {
        if (moneyMovement == null) {
            return null
        }
        return if (Hibernate.getClass(moneyMovement) == BankMoneyMovement::class.java) {
            bankMoneyMovementMapper!!.doToDto(Hibernate.unproxy(moneyMovement) as BankMoneyMovement)
        } else if (Hibernate.getClass(moneyMovement) == CashMoneyMovement::class.java) {
            cashMoneyMovementMapper!!.doToDto(Hibernate.unproxy(moneyMovement) as CashMoneyMovement)
        } else {
            throw IllegalStateException("Not supported money movement type.")
        }
    }
}