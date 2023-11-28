package cz.smartbrains.qesu.module.moneyBox.service

import cz.smartbrains.qesu.module.moneyBox.dto.BankMoneyMovementDto
import cz.smartbrains.qesu.module.moneyBox.dto.CashMoneyMovementDto
import cz.smartbrains.qesu.module.moneyBox.entity.BankMoneyMovement
import cz.smartbrains.qesu.module.moneyBox.entity.CashMoneyMovement

interface MoneyMovementInvoicePairService {
    fun payInvoice(newMoneyMovement: CashMoneyMovementDto)
    fun payInvoice(newMoneyMovement: CashMoneyMovementDto?, originalMovement: CashMoneyMovement)
    fun payInvoice(newMoneyMovement: BankMoneyMovementDto, originalMovement: BankMoneyMovement)
}