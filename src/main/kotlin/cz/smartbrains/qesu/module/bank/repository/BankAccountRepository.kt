package cz.smartbrains.qesu.module.bank.repository

import cz.smartbrains.qesu.module.bank.entity.BankAccount
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface BankAccountRepository : JpaRepository<BankAccount, Long>, BankAccountRepositoryCustom {
    fun findByIban(iban: String): Optional<BankAccount>
    fun findByAccountIdAndBankId(accountId: String?, bankId: String?): Optional<BankAccount>
}