package cz.smartbrains.qesu.module.bank.mapper

import cz.smartbrains.qesu.module.subject.mapper.SubjectBaseMapper
import cz.smartbrains.qesu.module.bank.dto.BankAccountDto
import cz.smartbrains.qesu.module.bank.dto.BankAccountRdto
import cz.smartbrains.qesu.module.bank.entity.BankAccount
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(uses = [SubjectBaseMapper::class], componentModel = "spring")
abstract class BankAccountMapper {
    abstract fun doToDto(bankAccount: BankAccount?): BankAccountDto?

    @Mappings(Mapping(target = "transactions", ignore = true),
            Mapping(target = "created", ignore = true),
            Mapping(target = "updated", ignore = true))
    abstract fun dtoToDo(bankAccount: BankAccountDto?): BankAccount?

    fun doToRdto(bankAccount: BankAccount?): BankAccountRdto? {
        return if (bankAccount == null) {
            null
        } else BankAccountRdto(String.format("%s / %s", bankAccount.accountId, bankAccount.bankId), bankAccount.iban, bankAccount.bic)
    }
}