package cz.smartbrains.qesu.module.moneyBox.controller

import cz.smartbrains.qesu.module.common.dto.validation.OnCreate
import cz.smartbrains.qesu.module.common.dto.validation.OnUpdate
import cz.smartbrains.qesu.module.moneyBox.dto.CashMoneyBoxDto
import cz.smartbrains.qesu.module.moneyBox.dto.MoneyBoxDto
import cz.smartbrains.qesu.module.moneyBox.service.CashMoneyBoxService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.groups.Default

@RestController
@RequestMapping("/api/cash-money-box")
class CashMoneyBoxController(private val service: CashMoneyBoxService) {
    @PostMapping
    fun create(@Validated(OnCreate::class, Default::class) @RequestBody moneyBox: CashMoneyBoxDto): MoneyBoxDto {
        return service.create(moneyBox)
    }

    @PutMapping
    fun update(@Validated(OnUpdate::class, Default::class) @RequestBody moneyBox: CashMoneyBoxDto): MoneyBoxDto {
        return service.update(moneyBox)
    }
}