package cz.smartbrains.qesu.module.bank.dto

import cz.smartbrains.qesu.module.common.dto.AbstractDto
import cz.smartbrains.qesu.module.document.dto.DocumentDto
import cz.smartbrains.qesu.module.bank.type.BankTxImportStatus
import cz.smartbrains.qesu.module.bank.type.BankTxImportType
import lombok.ToString
import java.time.LocalDate
import javax.validation.constraints.NotNull

@ToString(callSuper = true, exclude = ["account", "document"])
class BankTxImportDto : AbstractDto() {
    var dateFrom: LocalDate? = null
    var dateTo: LocalDate? = null
    var status: BankTxImportStatus? = null
    var type: BankTxImportType? = null
    var successCount = 0
    var failedCount = 0
    var account: BankAccountDto? = null
    var document: @NotNull DocumentDto? = null
}