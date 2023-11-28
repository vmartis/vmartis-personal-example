package cz.smartbrains.qesu.module.moneyBox.controller

import cz.smartbrains.qesu.module.common.dto.validation.OnCreate
import cz.smartbrains.qesu.module.common.dto.validation.OnUpdate
import cz.smartbrains.qesu.module.moneyBox.dto.BankMoneyMovementDto
import cz.smartbrains.qesu.module.moneyBox.dto.BankMoneyMovementSplitDto
import cz.smartbrains.qesu.module.moneyBox.service.BankMoneyMovementService
import cz.smartbrains.qesu.security.AlfaUserDetails
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.groups.Default

@RestController
@RequestMapping("/api/bank-money-movement")
class BankMoneyMovementController(private val bankMoneyMovementService: BankMoneyMovementService) {
    @PutMapping
    fun update(@Validated(OnUpdate::class, Default::class) @RequestBody moneyMovement: BankMoneyMovementDto, @AuthenticationPrincipal user: AlfaUserDetails): BankMoneyMovementDto {
        return bankMoneyMovementService.update(moneyMovement, user)
    }

    @PostMapping(path = ["/split"])
    fun split(@Validated(OnCreate::class, Default::class) @RequestBody moneyMovementSplit: BankMoneyMovementSplitDto, @AuthenticationPrincipal user: AlfaUserDetails): BankMoneyMovementDto {
        return bankMoneyMovementService.split(moneyMovementSplit, user)
    }
}