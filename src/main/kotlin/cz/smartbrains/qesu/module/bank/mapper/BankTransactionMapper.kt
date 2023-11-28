package cz.smartbrains.qesu.module.bank.mapper

import com.google.common.base.Strings
import cz.smartbrains.qesu.module.common.exception.ServiceRuntimeException
import cz.smartbrains.qesu.module.moneyBox.entity.BankMoneyMovement
import cz.smartbrains.qesu.module.moneyBox.entity.MoneyBox
import cz.smartbrains.qesu.module.moneyBox.repository.BankMoneyBoxRepository
import cz.smartbrains.qesu.module.moneyBox.type.AccountingType
import cz.smartbrains.qesu.module.moneyBox.type.MoneyMovementType
import cz.smartbrains.qesu.module.user.repository.UserRepository
import cz.smartbrains.qesu.module.bank.dto.BankTransactionDto
import cz.smartbrains.qesu.module.bank.entity.BankTransaction
import cz.smartbrains.qesu.security.AlfaUserDetails
import org.mapstruct.Mapper
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream

@Mapper(componentModel = "spring")
abstract class BankTransactionMapper {
    @Autowired
     var moneyBoxRepository: BankMoneyBoxRepository? = null
    @Autowired
    var userRepository: UserRepository? = null

    abstract fun doToDto(bankTransaction: BankTransaction?): BankTransactionDto?
    fun doToMovement(bankTransaction: BankTransaction?, user: AlfaUserDetails): BankMoneyMovement? {
        if (bankTransaction == null) {
            return null
        }
        val moneyBox: MoneyBox = moneyBoxRepository!!.findByBankAccountId(bankTransaction.account!!.id!!).orElseThrow<RuntimeException> { throw ServiceRuntimeException("bank-money-movement.moneyBox.not.exist", bankTransaction.account!!.name) }
        val movement = BankMoneyMovement()
        movement.created = LocalDateTime.now()
        movement.bankTransaction = bankTransaction
        movement.active = true
        movement.date = bankTransaction.date!!.atStartOfDay()
        movement.accountingType = AccountingType.NON_FISCAL
        movement.moneyBox = moneyBox
        movement.note = Strings.emptyToNull(
                Stream.of(bankTransaction.message,
                        bankTransaction.userIdentification,
                        bankTransaction.correspondingAccountName)
                        .filter { obj: String? -> Objects.nonNull(obj) }
                        .collect(Collectors.joining(" | ")))
        movement.totalAmount = bankTransaction.amount
        movement.totalWithoutVat = bankTransaction.amount
        movement.type = MoneyMovementType.of(bankTransaction.amount!!)
        movement.createdBy = userRepository!!.getById(user.id)
        return movement
    }
}