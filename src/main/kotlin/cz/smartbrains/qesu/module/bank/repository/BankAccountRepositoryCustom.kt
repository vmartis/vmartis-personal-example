package cz.smartbrains.qesu.module.bank.repository

import cz.smartbrains.qesu.module.bank.dto.BankAccountFilter
import cz.smartbrains.qesu.module.bank.entity.BankAccount

interface BankAccountRepositoryCustom {
    fun findByFilter(filter: BankAccountFilter): List<BankAccount>
}