package cz.smartbrains.qesu.module.moneyBox.mapper

import cz.smartbrains.qesu.module.company.mapper.CompanyBranchMapper
import cz.smartbrains.qesu.module.bank.mapper.BankAccountMapper
import cz.smartbrains.qesu.module.moneyBox.dto.BankMoneyBoxDto
import cz.smartbrains.qesu.module.moneyBox.entity.BankMoneyBox
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(uses = [BankAccountMapper::class, CompanyBranchMapper::class], componentModel = "spring")
interface BankMoneyBoxMapper {
    fun doToDto(moneyBox: BankMoneyBox?): BankMoneyBoxDto?

    @Mappings(Mapping(target = "created", ignore = true), Mapping(target = "updated", ignore = true))
    fun dtoToDo(moneyBoxDto: BankMoneyBoxDto?): BankMoneyBox?
}