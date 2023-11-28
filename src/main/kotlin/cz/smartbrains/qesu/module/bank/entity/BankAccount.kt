package cz.smartbrains.qesu.module.bank.entity

import cz.smartbrains.qesu.module.common.entity.AbstractEntity
import cz.smartbrains.qesu.module.subject.entity.Subject
import lombok.ToString
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Pattern

@ToString(callSuper = true, exclude = ["transactions", "subject"])
@Entity
@Table(name = "BANK_ACCOUNT", indexes = [Index(name = "BANK_ACCOUNT_NAME_IDX", columnList = "NAME"), Index(name = "BANK_ACCOUNT_ID_IDX", columnList = "ACCOUNT_ID,BANK_ID", unique = true), Index(name = "BANK_ACCOUNT_IBAN_IDX", columnList = "IBAN", unique = true)])
class BankAccount : AbstractEntity() {
    @Column(name = "NAME", length = 100, nullable = false)
    var name: String? = null

    @Column(name = "ACCOUNT_ID", length = 17, nullable = false)
    var accountId: @Pattern(regexp = "^[0-9-]{1,17}$") String? = null

    @Column(name = "BANK_ID", length = 4, nullable = false)
    var bankId: @Pattern(regexp = "^[0-9A-Za-z]{4}$") String? = null

    @Column(name = "CURRENCY", length = 3, nullable = false)
    var currency: @Pattern(regexp = "^[A-Z]{3}$") String? = null

    @Column(name = "IBAN", length = 34, unique = true)
    var iban: @Pattern(regexp = "^[A-Z,0-9]{3,34}$") String? = null

    @Column(name = "BIC", length = 11)
    var bic: @Pattern(regexp = "^[A-Z,0-9]{1,11}$") String? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "SUBJECT_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "BANK_ACCOUNT_SUBJECT_FK"), nullable = false)
    var subject: Subject? = null

    @Column(name = "ACTIVE", columnDefinition = "boolean default true", nullable = false)
    var active = true

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
    @OrderBy("DATE DESC")
    var transactions: List<BankTransaction> = ArrayList()

    fun formatAccountNumber(): String {
        return String.format("%s / %s", this.accountId, this.bankId)
    }
}