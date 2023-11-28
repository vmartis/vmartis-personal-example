package cz.smartbrains.qesu.module.bank.controller

import cz.smartbrains.qesu.module.bank.dto.BankAccountDto
import cz.smartbrains.qesu.module.bank.dto.BankAccountFilter
import cz.smartbrains.qesu.module.bank.service.BankAccountService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/bank-account")
class BankAccountController(private val bankAccountService: BankAccountService) {

    @GetMapping
    fun list(@RequestParam(required = false) subjectId: Long?,
             @RequestParam(required = false) ownershipped: Boolean): List<BankAccountDto> {
        return bankAccountService.findByFilter(BankAccountFilter(subjectId, ownershipped))
    }

    @PostMapping
    fun create(@RequestBody bankAccount: @Valid BankAccountDto): BankAccountDto {
        return bankAccountService.create(bankAccount)
    }

    @PutMapping
    fun update(@RequestBody bankAccount: @Valid BankAccountDto): BankAccountDto {
        return bankAccountService.update(bankAccount)
    }

    @DeleteMapping(path = ["/{id}"])
    fun delete(@PathVariable id: @Valid Long?) {
        bankAccountService.delete(id)
    }

}