package cz.smartbrains.qesu.module.bank.service

import cz.smartbrains.qesu.module.bank.dto.BankTxImportDto
import cz.smartbrains.qesu.security.AlfaUserDetails

interface CamtImportService {
    fun importTransactions(documentId: Long, user: AlfaUserDetails): List<BankTxImportDto>
}