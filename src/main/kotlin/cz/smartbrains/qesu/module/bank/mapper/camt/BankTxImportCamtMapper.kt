package cz.smartbrains.qesu.module.bank.mapper.camt

import cz.smartbrains.qesu.module.common.exception.ServiceRuntimeException
import cz.smartbrains.qesu.module.bank.entity.BankAccount
import cz.smartbrains.qesu.module.bank.entity.BankTransaction
import cz.smartbrains.qesu.module.bank.entity.BankTxImport
import cz.smartbrains.qesu.module.bank.repository.BankAccountRepository
import cz.smartbrains.qesu.module.bank.type.BankTxImportStatus
import cz.smartbrains.qesu.module.bank.type.BankTxImportType
import org.apache.commons.lang3.StringUtils
import org.iso.camt053.Document
import org.iso.camt053.ReportEntry2
import org.mapstruct.Mapper
import org.springframework.beans.factory.annotation.Autowired
import java.util.*
import java.util.function.Supplier
import java.util.stream.Collectors

@Mapper(componentModel = "spring")
open class BankTxImportCamtMapper {
    @Autowired
    private val bankAccountRepository: BankAccountRepository? = null

    @Autowired
    private val transactionCamtMapper: BankTransactionCamtMapper? = null

    fun fromCamt(document: Document?, inputDocument: cz.smartbrains.qesu.module.document.entity.Document?): List<BankTxImport> {
        if (document == null || document.bkToCstmrStmt == null) {
            return emptyList()
        }
        val imports: MutableList<BankTxImport> = ArrayList()
        for (statement in document.bkToCstmrStmt.stmt) {
            val bankTxImport = BankTxImport()
            var bankAccount: BankAccount?
            val cashAccount = statement.acct
            if (cashAccount != null && cashAccount.id != null) {
                val ibanFromImport = cashAccount.id.iban
                bankAccount = if (StringUtils.isNotBlank(ibanFromImport)) {
                    bankAccountRepository
                            ?.findByIban(ibanFromImport)
                            ?.orElseThrow(Supplier { ServiceRuntimeException("bank-tx-import.account.not.found.by.iban", ibanFromImport) })
                } else {
                    throw ServiceRuntimeException("bank-tx-import.account.id.missing")
                }
                bankTxImport.account = bankAccount
            }
            if (statement.frToDt != null && statement.frToDt.frDtTm != null) {
                bankTxImport.dateFrom = statement.frToDt.frDtTm.toLocalDate()
            }
            if (statement.frToDt != null && statement.frToDt.toDtTm != null) {
                bankTxImport.dateTo = statement.frToDt.toDtTm.toLocalDate()
            }
            bankTxImport.status = BankTxImportStatus.NEW
            bankTxImport.type = BankTxImportType.CAMT_053_001_002
            val transactions: List<BankTransaction> = statement.ntry.stream().map { reportEntry: ReportEntry2 -> transactionCamtMapper!!.fromCamt(reportEntry) }.collect(Collectors.toList())
            for (transaction in transactions) {
                transaction.account = bankTxImport.account
                transaction.txImport = bankTxImport
            }
            bankTxImport.transactions = transactions
            bankTxImport.document = inputDocument
            imports.add(bankTxImport)
        }
        return imports
    }
}