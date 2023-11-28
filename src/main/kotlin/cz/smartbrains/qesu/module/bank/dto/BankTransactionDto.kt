package cz.smartbrains.qesu.module.bank.dto

import cz.smartbrains.qesu.module.common.dto.AbstractDto
import java.math.BigDecimal
import java.time.LocalDate

class BankTransactionDto : AbstractDto() {
    var transactionId: String? = null
    var date: LocalDate? = null
    var amount: BigDecimal? = null
    var currency: String? = null
    var type: String? = null
    var correspondingAccountNumber: String? = null
    var correspondingAccountName: String? = null
    var correspondingBankId: String? = null
    var correspondingBankName: String? = null
    var constantSymbol: String? = null
    var variableSymbol: String? = null
    var specificSymbol: String? = null
    var userIdentification: String? = null
    var message: String? = null
    var submittedBy: String? = null
    var detail: String? = null
    var detail2: String? = null
}