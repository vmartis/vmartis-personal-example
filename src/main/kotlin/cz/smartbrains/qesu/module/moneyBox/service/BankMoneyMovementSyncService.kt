package cz.smartbrains.qesu.module.moneyBox.service

import cz.smartbrains.qesu.security.AlfaUserDetails

interface BankMoneyMovementSyncService {
    fun synchronize(bankAccountId: Long, user: AlfaUserDetails?)
}