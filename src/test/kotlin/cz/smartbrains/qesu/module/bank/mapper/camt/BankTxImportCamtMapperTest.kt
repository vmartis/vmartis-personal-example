package cz.smartbrains.qesu.module.bank.mapper.camt

import cz.smartbrains.qesu.module.bank.entity.BankAccount
import cz.smartbrains.qesu.module.bank.entity.BankTransaction
import cz.smartbrains.qesu.module.bank.repository.BankAccountRepository
import cz.smartbrains.qesu.module.bank.type.BankTxImportStatus
import cz.smartbrains.qesu.module.bank.type.BankTxImportType
import cz.smartbrains.qesu.module.common.exception.ServiceRuntimeException
import cz.smartbrains.qesu.module.document.entity.Document
import org.assertj.core.api.Assertions
import org.iso.camt053.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDateTime
import java.util.*

@SpringBootTest(classes = [BankTxImportCamtMapperImpl::class])
@ExtendWith(SpringExtension::class)
class BankTxImportCamtMapperTest {
    @MockBean
    private val bankAccountRepository: BankAccountRepository? = null

    @Autowired
    private val mapper: BankTxImportCamtMapperImpl? = null

    @MockBean
    private val transactionCamtMapper: BankTransactionCamtMapper? = null
    @Test
    fun fromCamt_emptyInput_transformedToEmptyList() {
        val document = org.iso.camt053.Document()
        val bankTxImports = mapper!!.fromCamt(document, null)
        Assertions.assertThat(bankTxImports).hasSize(0)
    }

    @Test
    fun fromCamt_oneEmptyStml_transformedToOneEmptyImport() {
        val document = org.iso.camt053.Document()
        val bankToCustomerStm = BankToCustomerStatementV02()
        document.bkToCstmrStmt = bankToCustomerStm
        bankToCustomerStm.stmt.add(AccountStatement2())
        val bankTxImports = mapper!!.fromCamt(document, null)
        Assertions.assertThat(bankTxImports).hasSize(1)
        val bankTxImport = bankTxImports[0]
        Assertions.assertThat(bankTxImport.account).isNull()
        Assertions.assertThat(bankTxImport.dateFrom).isNull()
        Assertions.assertThat(bankTxImport.dateTo).isNull()
        Assertions.assertThat(bankTxImport.failedCount).isEqualTo(0)
        Assertions.assertThat(bankTxImport.successCount).isEqualTo(0)
        Assertions.assertThat(bankTxImport.created).isNotNull
        Assertions.assertThat(bankTxImport.updated).isNull()
        Assertions.assertThat(bankTxImport.id).isNull()
        Assertions.assertThat(bankTxImport.document).isNull()
        Assertions.assertThat(bankTxImport.transactions).isEmpty()
        Assertions.assertThat(bankTxImport.status).isSameAs(BankTxImportStatus.NEW)
        Assertions.assertThat(bankTxImport.type).isSameAs(BankTxImportType.CAMT_053_001_002)
    }

    @Test
    fun fromCamt_invalidBankAccount_isTransformed() {
        val document = org.iso.camt053.Document()
        val bankToCustomerStm = BankToCustomerStatementV02()
        val accountStm = AccountStatement2()
        val account = CashAccount20()
        val accountId = AccountIdentification4Choice()
        val iban = "SK123456789"
        document.bkToCstmrStmt = bankToCustomerStm
        bankToCustomerStm.stmt.add(accountStm)
        accountStm.acct = account
        account.id = accountId
        accountId.iban = iban
        Assertions.assertThatThrownBy {
            mapper!!.fromCamt(document, null)
        }.isInstanceOf(ServiceRuntimeException::class.java)
    }

    @Test
    fun fromCamt_validBankAccount_isTransformed() {
        val document = org.iso.camt053.Document()
        val bankToCustomerStm = BankToCustomerStatementV02()
        val accountStm = AccountStatement2()
        val account = CashAccount20()
        val accountId = AccountIdentification4Choice()
        val iban = "SK123456789"
        document.bkToCstmrStmt = bankToCustomerStm
        bankToCustomerStm.stmt.add(accountStm)
        accountStm.acct = account
        account.id = accountId
        accountId.iban = iban
        val bankAccount = BankAccount()
        Mockito.`when`(bankAccountRepository!!.findByIban(iban)).thenReturn(Optional.of(bankAccount))
        val bankTxImports = mapper!!.fromCamt(document, null)
        Assertions.assertThat(bankTxImports).hasSize(1)
        val bankTxImport = bankTxImports[0]
        Assertions.assertThat(bankTxImport.account).isSameAs(bankAccount)
    }

    @Test
    fun fromCamt_fullInput_transformedToCorrectOutput() {
        val document = org.iso.camt053.Document()
        val bankToCustomerStm = BankToCustomerStatementV02()
        val accountStm = AccountStatement2()
        val dates = DateTimePeriodDetails()
        val inputDocument = Document()
        val entry1 = ReportEntry2()
        val from = LocalDateTime.now().minusDays(10)
        val to = LocalDateTime.now()
        document.bkToCstmrStmt = bankToCustomerStm
        bankToCustomerStm.stmt.add(accountStm)
        accountStm.frToDt = dates
        dates.frDtTm = from
        dates.toDtTm = to
        accountStm.ntry.add(entry1)
        val bankTransaction1 = BankTransaction()
        Mockito.`when`(transactionCamtMapper!!.fromCamt(Mockito.any())).thenReturn(bankTransaction1)
        val bankTxImports = mapper!!.fromCamt(document, inputDocument)
        Assertions.assertThat(bankTxImports).hasSize(1)
        val bankTxImport = bankTxImports[0]
        Assertions.assertThat(bankTxImport.dateFrom).isEqualTo(from.toLocalDate())
        Assertions.assertThat(bankTxImport.dateTo).isEqualTo(to.toLocalDate())
        Assertions.assertThat(bankTxImport.status).isSameAs(BankTxImportStatus.NEW)
        Assertions.assertThat(bankTxImport.type).isSameAs(BankTxImportType.CAMT_053_001_002)
        Assertions.assertThat(bankTxImport.failedCount).isEqualTo(0)
        Assertions.assertThat(bankTxImport.successCount).isEqualTo(0)
        Assertions.assertThat(bankTxImport.document).isSameAs(inputDocument)
        Assertions.assertThat(bankTxImport.transactions).contains(bankTransaction1)
    }
}