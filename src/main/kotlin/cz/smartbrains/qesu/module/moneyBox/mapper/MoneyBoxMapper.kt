package cz.smartbrains.qesu.module.moneyBox.mapper

import cz.smartbrains.qesu.module.moneyBox.dto.BankMoneyBoxDto
import cz.smartbrains.qesu.module.moneyBox.dto.CashMoneyBoxDto
import cz.smartbrains.qesu.module.moneyBox.dto.MoneyBoxDto
import cz.smartbrains.qesu.module.moneyBox.entity.BankMoneyBox
import cz.smartbrains.qesu.module.moneyBox.entity.CashMoneyBox
import cz.smartbrains.qesu.module.moneyBox.entity.MoneyBox
import org.hibernate.Hibernate
import org.mapstruct.Mapper
import org.springframework.beans.factory.annotation.Autowired

@Mapper(componentModel = "spring")
abstract class MoneyBoxMapper {
    @Autowired
    protected var cashMoneyBoxMapper: CashMoneyBoxMapper? = null

    @Autowired
    protected var bankMoneyBoxMapper: BankMoneyBoxMapper? = null
    fun doToDto(moneyBox: MoneyBox?): MoneyBoxDto? {
        if (moneyBox == null) return null
        return if (Hibernate.getClass(moneyBox) == CashMoneyBox::class.java) {
            cashMoneyBoxMapper!!.doToDto(Hibernate.unproxy(moneyBox) as CashMoneyBox)
        } else if (Hibernate.getClass(moneyBox) == BankMoneyBox::class.java) {
            bankMoneyBoxMapper!!.doToDto(Hibernate.unproxy(moneyBox) as BankMoneyBox)
        } else {
            throw IllegalStateException("not supported money box type.")
        }
    }

    fun dtoToDo(moneyBoxDto: MoneyBoxDto?): MoneyBox? {
        if (moneyBoxDto == null) return null
        return if (moneyBoxDto is CashMoneyBoxDto) {
            cashMoneyBoxMapper!!.dtoToDo(moneyBoxDto as CashMoneyBoxDto?)
        } else if (moneyBoxDto is BankMoneyBoxDto) {
            bankMoneyBoxMapper!!.dtoToDo(moneyBoxDto as BankMoneyBoxDto?)
        } else {
            throw IllegalStateException("not supported money box type.")
        }
    }
}