package cz.smartbrains.qesu.module.bank.service

import cz.smartbrains.qesu.module.bank.dto.BankTxImportDto
import cz.smartbrains.qesu.security.AlfaUserDetails
import org.springframework.security.access.prepost.PreAuthorize

interface BankTxImportService {
    @PreAuthorize("hasAuthority('FINANCE_MOVEMENT_IMPORT')")
    fun findAll(): List<BankTxImportDto>

    @PreAuthorize("hasAuthority('FINANCE_MOVEMENT_IMPORT')")
    fun create(bankTxImport: BankTxImportDto, user: AlfaUserDetails): BankTxImportDto
}