package cz.smartbrains.qesu.module.bank.mapper

import cz.smartbrains.qesu.module.document.mapper.DocumentBaseMapper
import cz.smartbrains.qesu.module.bank.dto.BankTxImportDto
import cz.smartbrains.qesu.module.bank.entity.BankTxImport
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(uses = [DocumentBaseMapper::class, BankAccountBaseMapper::class], componentModel = "spring")
interface BankTxImportMapper {
    fun doToDto(txImport: BankTxImport?): BankTxImportDto?

    @Mappings(Mapping(target = "transactions", ignore = true), Mapping(target = "created", ignore = true), Mapping(target = "updated", ignore = true))
    fun dtoToDo(bankAccount: BankTxImportDto?): BankTxImport?
}