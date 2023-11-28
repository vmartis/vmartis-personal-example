package cz.smartbrains.qesu.module.moneyBox.controller

import cz.smartbrains.qesu.module.common.dto.validation.OnCreate
import cz.smartbrains.qesu.module.common.dto.validation.OnUpdate
import cz.smartbrains.qesu.module.moneyBox.dto.BankMoneyBoxDto
import cz.smartbrains.qesu.module.moneyBox.dto.MoneyBoxDto
import cz.smartbrains.qesu.module.moneyBox.service.BankMoneyBoxService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.groups.Default

@RestController
@RequestMapping("/api/bank-money-box")
class BankMoneyBoxController(private val service: BankMoneyBoxService) {
    @PostMapping
    fun create(@Validated(OnCreate::class, Default::class) @RequestBody moneyBox: BankMoneyBoxDto): MoneyBoxDto {
        return service.create(moneyBox)
    }

    @PutMapping
    fun update(@Validated(OnUpdate::class, Default::class) @RequestBody moneyBox: BankMoneyBoxDto): MoneyBoxDto {
        return service.update(moneyBox)
    }
}