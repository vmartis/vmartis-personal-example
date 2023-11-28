package cz.smartbrains.qesu.module.moneyBox.mapper

import cz.smartbrains.qesu.module.company.mapper.CompanyBranchMapper
import cz.smartbrains.qesu.module.moneyBox.dto.CashMoneyBoxDto
import cz.smartbrains.qesu.module.moneyBox.entity.CashMoneyBox
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(uses = [CompanyBranchMapper::class], componentModel = "spring")
interface CashMoneyBoxMapper {
    fun doToDto(moneyBox: CashMoneyBox?): CashMoneyBoxDto?

    @Mappings(Mapping(target = "created", ignore = true), Mapping(target = "updated", ignore = true))
    fun dtoToDo(moneyBoxDto: CashMoneyBoxDto?): CashMoneyBox?
}