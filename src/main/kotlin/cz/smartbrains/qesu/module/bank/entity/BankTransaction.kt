package cz.smartbrains.qesu.module.bank.entity

import cz.smartbrains.qesu.module.common.entity.AbstractEntity
import cz.smartbrains.qesu.module.moneyBox.entity.BankMoneyMovement
import lombok.ToString
import org.apache.commons.lang3.StringUtils
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Pattern

@ToString(callSuper = true, exclude = ["account", "txImport", "movements"])
@Entity
@Table(name = "BANK_TRANSACTION", indexes = [Index(name = "BANK_TRANSACTION_DATE_IDX", columnList = "DATE"), Index(name = "BANK_TRANSACTION_ACCOUNT_TRANSACTION_ID_IDX", columnList = "TRANSACTION_ID,ACCOUNT_ID", unique = true), Index(name = "BANK_TRANSACTION_CREATED_IDX", columnList = "CREATED")])
class BankTransaction : AbstractEntity() {
    @Column(name = "TRANSACTION_ID", length = 35, nullable = false)
    var transactionId: String? = null

    @Column(name = "DATE", nullable = false)
    var date: LocalDate? = null

    @Column(name = "AMOUNT", precision = 15, scale = 5, nullable = false)
    var amount: BigDecimal? = null

    @Column(name = "CURRENCY", length = 3, nullable = false)
    var currency: @Pattern(regexp = "^[A-Z]{3}$") String? = null

    @Column(name = "TYPE", length = 255, nullable = false)
    var type: String? = null

    @Column(name = "CORRESPONDING_ACCOUNT_NUMBER", length = 255)
    var correspondingAccountNumber: @Pattern(regexp = "^[-0-9A-Z]{0,255}$") String? = null

    @Column(name = "CORRESPONDING_ACCOUNT_NAME", length = 255)
    var correspondingAccountName: String? = null

    @Column(name = "CORRESPONDING_BANK_ID", length = 4)
    var correspondingBankId: @Pattern(regexp = "^[0-9]{4}$") String? = null

    @Column(name = "CORRESPONDING_BANK_NAME", length = 255)
    var correspondingBankName: String? = null

    @Column(name = "CONSTANT_SYMBOL", length = 4)
    var constantSymbol: @Pattern(regexp = "^[0-9]{0,4}$") String? = null

    @Column(name = "VARIABLE_SYMBOL", length = 10)
    var variableSymbol: @Pattern(regexp = "^[0-9]{0,10}$") String? = null

    @Column(name = "SPECIFIC_SYMBOL", length = 10)
    var specificSymbol: @Pattern(regexp = "^[0-9]{0,10}$") String? = null

    @Column(name = "USER_IDENTIFICATION", length = 255)
    var userIdentification: String? = null

    @Column(name = "MESSAGE", length = 140)
    var message: String? = null

    @Column(name = "SUBMITTED_BY", length = 50)
    var submittedBy: String? = null

    @Column(name = "DETAIL", length = 255)
    var detail: String? = null

    @Column(name = "DETAIL2", length = 255)
    var detail2: String? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "ACCOUNT_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "BANK_TRANSACTION_ACCOUNT_FK"), nullable = false)
    var account: BankAccount? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "IMPORT_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "BANK_TRANSACTION_IMPORT_FK"), nullable = false)
    var txImport: BankTxImport? = null

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bankTransaction")
    var movements: List<BankMoneyMovement> = ArrayList()
    fun formatCorrespondingBankAccountName(): String {
        val sb = StringBuilder()
        if (StringUtils.isNotBlank(correspondingBankName)) {
            sb.append(correspondingBankName)
        }
        if (StringUtils.isNotBlank(correspondingAccountName)) {
            if (sb.length > 0) {
                sb.append(" - ")
            }
            sb.append(correspondingAccountName)
        }
        if (sb.length == 0) {
            sb.append(formatCorrespondingBankAccount())
        }
        return sb.toString()
    }

    private fun formatCorrespondingBankAccount(): String? {
        return if (StringUtils.isBlank(correspondingBankId)) {
            correspondingAccountNumber
        } else {
            String.format("%s / %s", correspondingAccountNumber, correspondingBankId)
        }
    }
}