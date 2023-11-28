package cz.smartbrains.qesu.module.bank.mapper.camt

import cz.smartbrains.qesu.module.bank.entity.BankTransaction
import org.apache.commons.lang3.StringUtils
import org.iso.camt053.*
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import java.math.BigDecimal
import java.util.*
import java.util.function.Consumer
import java.util.regex.Pattern
import java.util.stream.Collectors
import java.util.stream.Stream

@Mapper(componentModel = "spring")
abstract class BankTransactionCamtMapper {
    @Mappings(Mapping(target = "transactionId", expression = "java(mapTransactionId(reportEntry))"),
            Mapping(target = "date", source = "bookgDt.dt"),
            Mapping(target = "amount", expression = "java(convertAmount(reportEntry))"),
            Mapping(target = "currency", source = "amt.ccy"),
            Mapping(target = "type", source = "cdtDbtInd"),
            Mapping(target = "correspondingAccountName", expression = "java(mapCorrespondingAccountName(reportEntry))"),
            Mapping(target = "correspondingAccountNumber", expression = "java(mapCorrespondingAccountNumber(reportEntry))"),
            Mapping(target = "correspondingBankId", expression = "java(mapBankId(reportEntry))"),
            Mapping(target = "correspondingBankName", expression = "java(mapBankName(reportEntry))"),
            Mapping(target = "constantSymbol", expression = "java(parseConstantSymbol(reportEntry))"),
            Mapping(target = "variableSymbol", expression = "java(parseVariableSymbol(reportEntry))"),
            Mapping(target = "specificSymbol", expression = "java(parseSpecificSymbol(reportEntry))"),
            Mapping(target = "message", expression = "java(mapMessage(reportEntry))"),
            Mapping(target = "userIdentification", expression = "java(mapUserInfo(reportEntry))"),
            Mapping(target = "id", ignore = true),
            Mapping(target = "submittedBy", ignore = true),
            Mapping(target = "detail", ignore = true),
            Mapping(target = "detail2", ignore = true),
            Mapping(target = "account", ignore = true),
            Mapping(target = "txImport", ignore = true),
            Mapping(target = "movements", ignore = true),
            Mapping(target = "updated", ignore = true),
            Mapping(target = "created", ignore = true))
    abstract fun fromCamt(reportEntry: ReportEntry2?): BankTransaction
    protected fun mapTransactionId(reportEntry: ReportEntry2?): String? {
        if (reportEntry == null) {
            return null
        }
        //set Ntry/NtryRef if exists as transactionId
        if (StringUtils.isNotBlank(reportEntry.ntryRef)) {
            return reportEntry.ntryRef
        } else if (!reportEntry.ntryDtls.isEmpty()) {
            val detail = reportEntry.ntryDtls[0]
            if (!detail.txDtls.isEmpty()) {
                val entryTransaction = detail.txDtls[0]
                if (entryTransaction.refs != null && StringUtils.isNotBlank(entryTransaction.refs.acctSvcrRef)) {
                    return entryTransaction.refs.acctSvcrRef
                }
            }
        }
        return null
    }

    protected fun convertAmount(reportEntry: ReportEntry2?): BigDecimal? {
        return if (reportEntry != null && reportEntry.amt != null) {
            if (reportEntry.cdtDbtInd == CreditDebitCode.CRDT) reportEntry.amt.value else reportEntry.amt.value.multiply(BigDecimal.valueOf(-1))
        } else {
            null
        }
    }

    protected fun mapCorrespondingAccountNumber(reportEntry: ReportEntry2?): String? {
        return mapAccountNumber(parseAccount(reportEntry))
    }

    protected fun mapCorrespondingAccountName(reportEntry: ReportEntry2?): String? {
        val account = parseAccount(reportEntry)
        return if (account != null && NOTPROVIDED_ACCOUNT_NAME != account.nm) account.nm else null
    }

    private fun parseAccount(reportEntry: ReportEntry2?): CashAccount16? {
        if (reportEntry == null || reportEntry.ntryDtls.isEmpty()) {
            return null
        }
        val detail = reportEntry.ntryDtls[0]
        if (!detail.txDtls.isEmpty()) {
            val trx = detail.txDtls[0]
            val part = trx.rltdPties
            if (part != null) {
                if (reportEntry.cdtDbtInd == CreditDebitCode.CRDT) {
                    return part.dbtrAcct
                } else if (reportEntry.cdtDbtInd == CreditDebitCode.DBIT) {
                    return part.cdtrAcct
                }
            }
        }
        return null
    }

    protected fun mapBankId(reportEntry: ReportEntry2?): String? {
        val bankIdentification = parseBankIdentification(reportEntry)
        return if (bankIdentification != null && bankIdentification.finInstnId != null && bankIdentification.finInstnId.othr != null) {
            bankIdentification.finInstnId.othr.id
        } else null
    }

    protected fun mapBankName(reportEntry: ReportEntry2?): String? {
        val bankIdentification = parseBankIdentification(reportEntry)
        return if (bankIdentification != null && bankIdentification.finInstnId != null) {
            bankIdentification.finInstnId.nm
        } else null
    }

    protected fun parseConstantSymbol(reportEntry: ReportEntry2?): String? {
        return parseSymbol(reportEntry, KS_PREFIX, KS_MAX_LENGTH)
    }

    protected fun parseVariableSymbol(reportEntry: ReportEntry2?): String? {
        return parseSymbol(reportEntry, VS_PREFIX, VS_MAX_LENGTH)
    }

    protected fun parseSpecificSymbol(reportEntry: ReportEntry2?): String? {
        return parseSymbol(reportEntry, SS_PREFIX, SS_MAX_LENGTH)
    }

    protected fun mapMessage(reportEntry: ReportEntry2?): String? {
        if (reportEntry != null && !reportEntry.ntryDtls.isEmpty()
                && !reportEntry.ntryDtls[0].txDtls.isEmpty()) {
            val trx = reportEntry.ntryDtls[0].txDtls[0]
            val builder = Stream.builder<String?>()
            // payment identification - will be on first part, always present in result
            if (trx.rmtInf != null && !trx.rmtInf.ustrd.isEmpty()) {
                builder.add(trx.rmtInf.ustrd[0])
            }
            // debit payment with card
            if (trx.rltdAgts != null && !trx.rltdAgts.prtry.isEmpty()) {
                builder.add(trx.rltdAgts.prtry
                        .stream()
                        .filter { proprietaryAgent2: ProprietaryAgent2 -> CARD_ACQUIER.equals(proprietaryAgent2.tp, ignoreCase = true) }
                        .findFirst()
                        .map { agent2: ProprietaryAgent2 -> agentToMessage(agent2) }
                        .orElse(null))
            } else if (trx.rltdPties != null && trx.rltdPties.cdtr != null) {
                builder.add(identificationToMessage(trx.rltdPties.cdtr))
            } else if (trx.rltdPties != null
                    && trx.rltdPties.dbtr != null) {
                builder.add(identificationToMessage(trx.rltdPties.dbtr))
            }
            return builder.build()
                    .filter { obj: String? -> Objects.nonNull(obj) }
                    .collect(Collectors.joining(MESSAGE_DELIMITER))
        }
        return null
    }

    private fun agentToMessage(agent2: ProprietaryAgent2): String? {
        if (agent2.agt != null && agent2.agt.brnchId != null) {
            val brnchId = agent2.agt.brnchId
            val builder = Stream.builder<String>()
            if (StringUtils.isNotBlank(brnchId.nm)) {
                builder.add(brnchId.nm)
            }
            if (brnchId.pstlAdr != null) {
                brnchId.pstlAdr.adrLine.forEach(Consumer { t: String -> builder.add(t) })
            }
            return builder.build()
                    .filter { obj: String? -> Objects.nonNull(obj) }
                    .collect(Collectors.joining(MESSAGE_DELIMITER))
        }
        return null
    }

    private fun identificationToMessage(identification: PartyIdentification32): String {
        val builder = Stream.builder<String>()
        if (StringUtils.isNotBlank(identification.nm)) {
            builder.add(identification.nm)
        }
        if (identification.pstlAdr != null) {
            identification.pstlAdr.adrLine.forEach(Consumer { t: String -> builder.add(t) })
        }
        return builder.build()
                .filter { obj: String? -> Objects.nonNull(obj) }
                .collect(Collectors.joining(MESSAGE_DELIMITER))
    }

    protected fun mapUserInfo(reportEntry: ReportEntry2?): String? {
        return if (reportEntry == null || reportEntry.ntryDtls.isEmpty()
                || reportEntry.ntryDtls[0].txDtls.isEmpty()) {
            null
        } else {
            reportEntry.ntryDtls[0].txDtls[0].addtlTxInf
        }
    }

    private fun parseSymbol(reportEntry: ReportEntry2?, symbolPrefix: String, maxLength: Int): String? {
        if (reportEntry == null || reportEntry.ntryDtls.isEmpty()
                || reportEntry.ntryDtls[0].txDtls.isEmpty()
                || reportEntry.ntryDtls[0].txDtls[0].refs == null || reportEntry.ntryDtls[0].txDtls[0].refs.endToEndId == null) {
            return null
        }
        val endToEndId = reportEntry.ntryDtls[0].txDtls[0].refs.endToEndId
        val pattern = String.format("(.*%s)([0-9]{1,%d})(.*)", symbolPrefix, maxLength)

        // Create a Pattern object
        val r = Pattern.compile(pattern)
        // Now create matcher object.
        val m = r.matcher(endToEndId)
        return if (m.find()) m.group(2) else null
    }

    private fun parseBankIdentification(reportEntry: ReportEntry2?): BranchAndFinancialInstitutionIdentification4? {
        if (reportEntry == null || reportEntry.ntryDtls.isEmpty()) {
            return null
        }
        val detail = reportEntry.ntryDtls[0]
        if (!detail.txDtls.isEmpty()) {
            val trx = detail.txDtls[0]
            val agents2 = trx.rltdAgts
            if (agents2 != null) {
                if (reportEntry.cdtDbtInd == CreditDebitCode.CRDT) {
                    return agents2.dbtrAgt
                } else if (reportEntry.cdtDbtInd == CreditDebitCode.DBIT) {
                    return agents2.cdtrAgt
                }
            }
        }
        return null
    }

    private fun mapAccountNumber(account: CashAccount16?): String? {
        if (account == null || account.id == null) {
            return null
        }
        val accountId = account.id
        if (StringUtils.isNotBlank(accountId.iban)) {
            return accountId.iban
        } else if (accountId.othr != null && StringUtils.isNotBlank(accountId.othr.id)) {
            return accountId.othr.id
        }
        return null
    }

    companion object {
        private const val NOTPROVIDED_ACCOUNT_NAME = "NOTPROVIDED"
        private const val VS_PREFIX = "[/]VS"
        private const val SS_PREFIX = "[/]SS"
        private const val KS_PREFIX = "[/]KS"
        private const val VS_MAX_LENGTH = 10
        private const val SS_MAX_LENGTH = 10
        private const val KS_MAX_LENGTH = 4
        const val CARD_ACQUIER = "CardAcquier"
        const val MESSAGE_DELIMITER = ", "
    }
}