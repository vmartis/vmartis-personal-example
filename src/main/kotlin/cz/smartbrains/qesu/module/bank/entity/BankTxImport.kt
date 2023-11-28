package cz.smartbrains.qesu.module.bank.entity

import cz.smartbrains.qesu.module.common.entity.AbstractEntity
import cz.smartbrains.qesu.module.document.entity.Document
import cz.smartbrains.qesu.module.bank.type.BankTxImportStatus
import cz.smartbrains.qesu.module.bank.type.BankTxImportType
import lombok.ToString
import java.time.LocalDate
import java.util.*
import javax.persistence.*

@ToString(callSuper = true, exclude = ["account", "transactions", "document"])
@Entity
@Table(name = "BANK_TX_IMPORT", indexes = [Index(name = "BANK_TX_IMPORT_DATE_FROM_IDX", columnList = "DATE_FROM")])
class BankTxImport : AbstractEntity() {
    @Column(name = "DATE_FROM", nullable = false)
    var dateFrom: LocalDate? = null

    @Column(name = "DATE_TO", nullable = false)
    var dateTo: LocalDate? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    var status: BankTxImportStatus? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false, length = 20)
    var type: BankTxImportType? = null

    @Column(name = "SUCCESS_COUNT", columnDefinition = "integer", nullable = false)
    var successCount = 0

    @Column(name = "FAILED_COUNT", columnDefinition = "integer", nullable = false)
    var failedCount = 0

    @ManyToOne(optional = false)
    @JoinColumn(name = "ACCOUNT_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "BANK_TX_IMPORT_ACCOUNT_FK"), nullable = false)
    var account: BankAccount? = null

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
    @OrderBy("DATE DESC")
    var transactions: List<BankTransaction> = ArrayList()

    @OneToOne(optional = false, orphanRemoval = true)
    @JoinColumn(name = "DOCUMENT_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "BANK_TX_IMPORT_DOCUMENT_FK"), nullable = false)
    var document: Document? = null
}