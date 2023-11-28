package cz.smartbrains.qesu.module.bank.mapper.camt

import org.assertj.core.api.Assertions
import org.iso.camt053.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate

class BankTransactionCamtMapperTest {
    private val mapper: BankTransactionCamtMapper = BankTransactionCamtMapperImpl()
    @Test
    fun fromCamt_nullInput_transformedToNull() {
        Assertions.assertThat(mapper.fromCamt(null)).isNull()
    }

    @Test
    fun fromCamt_emptyInput_transformedToEmptyOutput() {
        val transaction = mapper.fromCamt(ReportEntry2())
        Assertions.assertThat(transaction).isNotNull
        Assertions.assertThat(transaction.id).isNull()
        Assertions.assertThat(transaction.transactionId).isNull()
        Assertions.assertThat(transaction.account).isNull()
        Assertions.assertThat(transaction.amount).isNull()
        Assertions.assertThat(transaction.currency).isNull()
        Assertions.assertThat(transaction.type).isNull()
        Assertions.assertThat(transaction.constantSymbol).isNull()
        Assertions.assertThat(transaction.correspondingAccountName).isNull()
        Assertions.assertThat(transaction.correspondingAccountNumber).isNull()
        Assertions.assertThat(transaction.correspondingBankId).isNull()
        Assertions.assertThat(transaction.correspondingBankName).isNull()
        Assertions.assertThat(transaction.date).isNull()
        Assertions.assertThat(transaction.detail).isNull()
        Assertions.assertThat(transaction.detail2).isNull()
        Assertions.assertThat(transaction.message).isNull()
        Assertions.assertThat(transaction.specificSymbol).isNull()
        Assertions.assertThat(transaction.submittedBy).isNull()
        Assertions.assertThat(transaction.userIdentification).isNull()
        Assertions.assertThat(transaction.variableSymbol).isNull()
        Assertions.assertThat(transaction.updated).isNull()
        Assertions.assertThat(transaction.created).isNotNull
    }

    @Test
    fun fromCamt_debitTx_amoutTransformedToPositiveValue() {
        val input = ReportEntry2()
        val amount = ActiveOrHistoricCurrencyAndAmount()
        amount.value = BigDecimal.valueOf(12345)
        input.amt = amount
        input.cdtDbtInd = CreditDebitCode.DBIT
        val bankTransaction = mapper.fromCamt(input)
        Assertions.assertThat(bankTransaction.amount).isEqualTo(BigDecimal.valueOf(-12345))
    }

    @Test
    fun fromCamt_creditTx_amoutTransformedToPositiveValue() {
        val input = ReportEntry2()
        val amount = ActiveOrHistoricCurrencyAndAmount()
        amount.value = BigDecimal.valueOf(998)
        input.amt = amount
        input.cdtDbtInd = CreditDebitCode.CRDT
        val bankTransaction = mapper.fromCamt(input)
        Assertions.assertThat(bankTransaction.amount).isEqualTo(BigDecimal.valueOf(998))
    }

    @Test
    fun fromCamt_creditTxWithIbanCorrespondingAccount_correspondingBankAccountTransformed() {
        val input = ReportEntry2()
        input.cdtDbtInd = CreditDebitCode.CRDT
        val detail = EntryDetails1()
        val trx = EntryTransaction2()
        val party = TransactionParty2()
        val account = CashAccount16()
        val accountId = AccountIdentification4Choice()
        detail.txDtls.add(trx)
        input.ntryDtls.add(detail)
        trx.rltdPties = party
        party.dbtrAcct = account
        account.id = accountId
        accountId.iban = "SK12345678912345"
        val bankTransaction = mapper.fromCamt(input)
        Assertions.assertThat(bankTransaction.correspondingAccountNumber).isEqualTo("SK12345678912345")
    }

    @Test
    fun fromCamt_debitTxWithIbanCorrespondingAccount_correspondingBankAccountTransformed() {
        val input = ReportEntry2()
        input.cdtDbtInd = CreditDebitCode.DBIT
        val detail = EntryDetails1()
        val trx = EntryTransaction2()
        val party = TransactionParty2()
        val account = CashAccount16()
        val accountId = AccountIdentification4Choice()
        detail.txDtls.add(trx)
        input.ntryDtls.add(detail)
        trx.rltdPties = party
        party.cdtrAcct = account
        account.id = accountId
        accountId.iban = "SK12345678912345"
        val bankTransaction = mapper.fromCamt(input)
        Assertions.assertThat(bankTransaction.correspondingAccountNumber).isEqualTo("SK12345678912345")
    }

    @Test
    fun fromCamt_creditTxWithOtherIdCorrespondingAccount_correspondingBankAccountTransformed() {
        val input = ReportEntry2()
        input.cdtDbtInd = CreditDebitCode.CRDT
        val detail = EntryDetails1()
        val trx = EntryTransaction2()
        val party = TransactionParty2()
        val account = CashAccount16()
        val accountId = AccountIdentification4Choice()
        val otherAccountId = GenericAccountIdentification1()
        detail.txDtls.add(trx)
        input.ntryDtls.add(detail)
        trx.rltdPties = party
        party.dbtrAcct = account
        account.id = accountId
        accountId.othr = otherAccountId
        otherAccountId.id = "123456789"
        val bankTransaction = mapper.fromCamt(input)
        Assertions.assertThat(bankTransaction.correspondingAccountNumber).isEqualTo("123456789")
    }

    @Test
    fun fromCamt_debitTxWithOtherIdCorrespondingAccount_correspondingBankAccountTransformed() {
        val input = ReportEntry2()
        input.cdtDbtInd = CreditDebitCode.DBIT
        val detail = EntryDetails1()
        val trx = EntryTransaction2()
        val party = TransactionParty2()
        val account = CashAccount16()
        val accountId = AccountIdentification4Choice()
        val otherAccountId = GenericAccountIdentification1()
        detail.txDtls.add(trx)
        input.ntryDtls.add(detail)
        trx.rltdPties = party
        party.cdtrAcct = account
        account.id = accountId
        accountId.othr = otherAccountId
        otherAccountId.id = "123456789"
        val bankTransaction = mapper.fromCamt(input)
        Assertions.assertThat(bankTransaction.correspondingAccountNumber).isEqualTo("123456789")
    }

    @Test
    fun fromCamt_creditTxWithCorrespondingAccountName_accountNameIsMapped() {
        val input = ReportEntry2()
        input.cdtDbtInd = CreditDebitCode.CRDT
        val detail = EntryDetails1()
        val trx = EntryTransaction2()
        val party = TransactionParty2()
        val account = CashAccount16()
        detail.txDtls.add(trx)
        input.ntryDtls.add(detail)
        trx.rltdPties = party
        party.dbtrAcct = account
        account.nm = "Account XYZ"
        val bankTransaction = mapper.fromCamt(input)
        Assertions.assertThat(bankTransaction.correspondingAccountName).isEqualTo("Account XYZ")
    }

    @Test
    fun fromCamt_debitTxWithCorrespondingAccountName_accountNameIsMapped() {
        val input = ReportEntry2()
        input.cdtDbtInd = CreditDebitCode.DBIT
        val detail = EntryDetails1()
        val trx = EntryTransaction2()
        val party = TransactionParty2()
        val account = CashAccount16()
        detail.txDtls.add(trx)
        input.ntryDtls.add(detail)
        trx.rltdPties = party
        party.cdtrAcct = account
        account.nm = "Account XYZ"
        val bankTransaction = mapper.fromCamt(input)
        Assertions.assertThat(bankTransaction.correspondingAccountName).isEqualTo("Account XYZ")
    }

    @Test
    fun fromCamt_creditTxWithCorrespondingBankId_bankIdIsMapped() {
        val input = ReportEntry2()
        input.cdtDbtInd = CreditDebitCode.CRDT
        val detail = EntryDetails1()
        val trx = EntryTransaction2()
        val agents = TransactionAgents2()
        val bankId = BranchAndFinancialInstitutionIdentification4()
        val finInstnId = FinancialInstitutionIdentification7()
        val otherId = GenericFinancialIdentification1()
        detail.txDtls.add(trx)
        input.ntryDtls.add(detail)
        trx.rltdAgts = agents
        agents.dbtrAgt = bankId
        bankId.finInstnId = finInstnId
        finInstnId.othr = otherId
        otherId.id = "0300"
        val bankTransaction = mapper.fromCamt(input)
        Assertions.assertThat(bankTransaction.correspondingBankId).isEqualTo("0300")
    }

    @Test
    fun fromCamt_debitTxWithCorrespondingBankId_bankIdIsMapped() {
        val input = ReportEntry2()
        input.cdtDbtInd = CreditDebitCode.DBIT
        val detail = EntryDetails1()
        val trx = EntryTransaction2()
        val agents = TransactionAgents2()
        val bankId = BranchAndFinancialInstitutionIdentification4()
        val finInstnId = FinancialInstitutionIdentification7()
        val otherId = GenericFinancialIdentification1()
        detail.txDtls.add(trx)
        input.ntryDtls.add(detail)
        trx.rltdAgts = agents
        agents.cdtrAgt = bankId
        bankId.finInstnId = finInstnId
        finInstnId.othr = otherId
        otherId.id = "0300"
        val bankTransaction = mapper.fromCamt(input)
        Assertions.assertThat(bankTransaction.correspondingBankId).isEqualTo("0300")
    }

    @Test
    fun fromCamt_creditTxWithCorrespondingBankName_bankNameIsMapped() {
        val input = ReportEntry2()
        input.cdtDbtInd = CreditDebitCode.CRDT
        val detail = EntryDetails1()
        val trx = EntryTransaction2()
        val agents = TransactionAgents2()
        val bankId = BranchAndFinancialInstitutionIdentification4()
        val finInstnId = FinancialInstitutionIdentification7()
        detail.txDtls.add(trx)
        input.ntryDtls.add(detail)
        trx.rltdAgts = agents
        agents.dbtrAgt = bankId
        bankId.finInstnId = finInstnId
        finInstnId.nm = "Bank XYZ"
        val bankTransaction = mapper.fromCamt(input)
        Assertions.assertThat(bankTransaction.correspondingBankName).isEqualTo("Bank XYZ")
    }

    @Test
    fun fromCamt_debitTxWithCorrespondingBankName_bankNameIsMapped() {
        val input = ReportEntry2()
        input.cdtDbtInd = CreditDebitCode.DBIT
        val detail = EntryDetails1()
        val trx = EntryTransaction2()
        val agents = TransactionAgents2()
        val bankId = BranchAndFinancialInstitutionIdentification4()
        val finInstnId = FinancialInstitutionIdentification7()
        detail.txDtls.add(trx)
        input.ntryDtls.add(detail)
        trx.rltdAgts = agents
        agents.cdtrAgt = bankId
        bankId.finInstnId = finInstnId
        finInstnId.nm = "Bank XYZ"
        val bankTransaction = mapper.fromCamt(input)
        Assertions.assertThat(bankTransaction.correspondingBankName).isEqualTo("Bank XYZ")
    }

    @Test
    fun fromCamt_everySymbol_isMapped() {
        val input = ReportEntry2()
        val detail = EntryDetails1()
        val trx = EntryTransaction2()
        val refs = TransactionReferences2()
        detail.txDtls.add(trx)
        input.ntryDtls.add(detail)
        trx.refs = refs
        refs.endToEndId = "/VS20190009/SS000111222/KS0308"
        val bankTransaction = mapper.fromCamt(input)
        Assertions.assertThat(bankTransaction.variableSymbol).isEqualTo("20190009")
        Assertions.assertThat(bankTransaction.specificSymbol).isEqualTo("000111222")
        Assertions.assertThat(bankTransaction.constantSymbol).isEqualTo("0308")
    }

    @Test
    fun fromCamt_onlySpecificSymbol_isMapped() {
        val input = ReportEntry2()
        val detail = EntryDetails1()
        val trx = EntryTransaction2()
        val refs = TransactionReferences2()
        detail.txDtls.add(trx)
        input.ntryDtls.add(detail)
        trx.refs = refs
        refs.endToEndId = "/VS/SS000111222"
        val bankTransaction = mapper.fromCamt(input)
        Assertions.assertThat(bankTransaction.variableSymbol).isNull()
        Assertions.assertThat(bankTransaction.specificSymbol).isEqualTo("000111222")
        Assertions.assertThat(bankTransaction.constantSymbol).isNull()
    }

    @Test
    fun fromCamt_specificSymbolAndVariableSymbol_isMapped() {
        val input = ReportEntry2()
        val detail = EntryDetails1()
        val trx = EntryTransaction2()
        val refs = TransactionReferences2()
        detail.txDtls.add(trx)
        input.ntryDtls.add(detail)
        trx.refs = refs
        refs.endToEndId = "/VS00/SS000111222/KS"
        val bankTransaction = mapper.fromCamt(input)
        Assertions.assertThat(bankTransaction.constantSymbol).isNull()
        Assertions.assertThat(bankTransaction.specificSymbol).isEqualTo("000111222")
        Assertions.assertThat(bankTransaction.variableSymbol).isEqualTo("00")
    }

    @Test
    fun fromCamt_message_isMapped() {
        val input = ReportEntry2()
        val detail = EntryDetails1()
        val trx = EntryTransaction2()
        val info = RemittanceInformation5()
        detail.txDtls.add(trx)
        input.ntryDtls.add(detail)
        trx.rmtInf = info
        info.ustrd.add("Some nice payment info")
        val bankTransaction = mapper.fromCamt(input)
        Assertions.assertThat(bankTransaction.message).isEqualTo("Some nice payment info")
    }

    @Test
    fun fromCamt_userIdentificaiton_isMapped() {
        val input = ReportEntry2()
        val detail = EntryDetails1()
        val trx = EntryTransaction2()
        detail.txDtls.add(trx)
        input.ntryDtls.add(detail)
        trx.addtlTxInf = "Some other user identification."
        val bankTransaction = mapper.fromCamt(input)
        Assertions.assertThat(bankTransaction.userIdentification).isEqualTo("Some other user identification.")
    }

    @Test
    fun fromCamt_txWithNtryRef_ntryRefIsMappedAsTransactionId() {
        val input = ReportEntry2()
        input.ntryRef = "ABC123"
        val bankTransaction = mapper.fromCamt(input)
        Assertions.assertThat(bankTransaction.transactionId).isEqualTo("ABC123")
    }

    @Test
    fun fromCamt_txWithAcctSvcrRef_acctSvcrRefIsMappedAsTransactionId() {
        val input = ReportEntry2()
        val entryDetail = EntryDetails1()
        input.ntryDtls.add(entryDetail)
        val entryTransaction = EntryTransaction2()
        entryDetail.txDtls.add(entryTransaction)
        val txRefs = TransactionReferences2()
        entryTransaction.refs = txRefs
        txRefs.acctSvcrRef = "NJU0"
        val bankTransaction = mapper.fromCamt(input)
        Assertions.assertThat(bankTransaction.transactionId).isEqualTo("NJU0")
    }

    @Test
    fun fromCamt_fullInput_transformedToCorrectOutput() {
        val now = LocalDate.now()
        val input = ReportEntry2()
        input.ntryRef = "21996341517"
        val bookDt = DateAndDateTimeChoice()
        bookDt.dt = now
        input.bookgDt = bookDt
        val amount = ActiveOrHistoricCurrencyAndAmount()
        amount.value = BigDecimal.TEN
        amount.ccy = "EUR"
        input.amt = amount
        input.cdtDbtInd = CreditDebitCode.CRDT
        val transaction = mapper.fromCamt(input)
        Assertions.assertThat(transaction).isNotNull
        Assertions.assertThat(transaction.id).isNull()
        Assertions.assertThat(transaction.account).isNull()
        Assertions.assertThat(transaction.transactionId).isEqualTo("21996341517")
        Assertions.assertThat(transaction.date).isEqualTo(now)
        Assertions.assertThat(transaction.amount).isEqualTo(BigDecimal.TEN)
        Assertions.assertThat(transaction.currency).isEqualTo("EUR")
        Assertions.assertThat(transaction.type).isEqualTo("CRDT")
        Assertions.assertThat(transaction.constantSymbol).isNull()
        Assertions.assertThat(transaction.correspondingAccountName).isNull()
        Assertions.assertThat(transaction.correspondingAccountNumber).isNull()
        Assertions.assertThat(transaction.correspondingBankId).isNull()
        Assertions.assertThat(transaction.correspondingBankName).isNull()
        Assertions.assertThat(transaction.detail).isNull()
        Assertions.assertThat(transaction.detail2).isNull()
        Assertions.assertThat(transaction.message).isNull()
        Assertions.assertThat(transaction.specificSymbol).isNull()
        Assertions.assertThat(transaction.submittedBy).isNull()
        Assertions.assertThat(transaction.userIdentification).isNull()
        Assertions.assertThat(transaction.variableSymbol).isNull()
        Assertions.assertThat(transaction.updated).isNull()
        Assertions.assertThat(transaction.created).isNotNull
    }
}