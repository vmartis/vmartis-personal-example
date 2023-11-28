package cz.smartbrains.qesu.module.moneyBox.mapper

import cz.smartbrains.qesu.module.moneyBox.dto.MoneyBoxBalanceDto
import cz.smartbrains.qesu.module.moneyBox.entity.MoneyBoxBalance
import org.mapstruct.Mapper
import org.springframework.beans.factory.annotation.Autowired

@Mapper(uses = [MoneyBoxMapper::class], componentModel = "spring")
abstract class MoneyBoxBalanceMapper {
    @Autowired
    private val moneyBoxMapper: MoneyBoxMapper? = null

    fun doToDto(moneyBoxBalance: MoneyBoxBalance?): MoneyBoxBalanceDto? {
        if (moneyBoxBalance == null) {
            return null
        }
        val moneyBoxBalanceDto = MoneyBoxBalanceDto()
        moneyBoxBalanceDto.totalAmount = moneyBoxBalance.totalAmount.add(moneyBoxBalance.moneyBox!!.getInitialAmount(moneyBoxBalance.date!!))
        moneyBoxBalanceDto.date = moneyBoxBalance.date
        moneyBoxBalanceDto.moneyBox = moneyBoxMapper!!.doToDto(moneyBoxBalance.moneyBox)
        return moneyBoxBalanceDto
    }
}