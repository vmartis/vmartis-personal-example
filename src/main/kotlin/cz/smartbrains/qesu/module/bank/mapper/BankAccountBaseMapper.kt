package cz.smartbrains.qesu.module.bank.mapper

import cz.smartbrains.qesu.module.bank.dto.BankAccountDto
import cz.smartbrains.qesu.module.bank.entity.BankAccount
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(componentModel = "spring")
interface BankAccountBaseMapper {
    @Mappings(Mapping(target = "subject", ignore = true))
    fun doToDto(bankAccount: BankAccount?): BankAccountDto?

    @Mappings(Mapping(target = "subject", ignore = true), Mapping(target = "created", ignore = true), Mapping(target = "updated", ignore = true), Mapping(target = "transactions", ignore = true))
    fun doToDto(bankAccount: BankAccountDto?): BankAccount?
}