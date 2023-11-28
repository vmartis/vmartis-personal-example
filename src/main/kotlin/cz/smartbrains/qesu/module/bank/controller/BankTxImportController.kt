package cz.smartbrains.qesu.module.bank.controller

import cz.smartbrains.qesu.module.common.dto.validation.OnCreate
import cz.smartbrains.qesu.module.bank.dto.BankTxImportDto
import cz.smartbrains.qesu.module.bank.service.BankTxImportService
import cz.smartbrains.qesu.security.AlfaUserDetails
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.groups.Default

@RestController
@RequestMapping("/api/bank-tx-import")
class BankTxImportController(private val bankTxImportService: BankTxImportService) {

    @GetMapping
    fun list(): List<BankTxImportDto> {
        return bankTxImportService.findAll()
    }

    @PostMapping
    fun create(@Validated(OnCreate::class, Default::class) @RequestBody bankTxImport: BankTxImportDto, @AuthenticationPrincipal user: AlfaUserDetails): BankTxImportDto {
        return bankTxImportService.create(bankTxImport, user)
    }

}