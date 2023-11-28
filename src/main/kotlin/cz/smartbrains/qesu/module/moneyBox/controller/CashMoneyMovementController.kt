package cz.smartbrains.qesu.module.moneyBox.controller

import cz.smartbrains.qesu.module.common.dto.validation.OnCreate
import cz.smartbrains.qesu.module.common.dto.validation.OnUpdate
import cz.smartbrains.qesu.module.moneyBox.dto.CashMoneyMovementDto
import cz.smartbrains.qesu.module.moneyBox.service.CashMoneyMovementService
import cz.smartbrains.qesu.security.AlfaUserDetails
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.groups.Default

@RestController
@RequestMapping("/api/cash-money-movement")
class CashMoneyMovementController(private val cashMoneyMovementService: CashMoneyMovementService) {
    @PostMapping
    fun create(@Validated(OnCreate::class, Default::class) @RequestBody moneyMovement: CashMoneyMovementDto, @AuthenticationPrincipal user: AlfaUserDetails): CashMoneyMovementDto {
        return cashMoneyMovementService.create(moneyMovement, user)
    }

    @PutMapping
    fun update(@Validated(OnUpdate::class, Default::class) @RequestBody moneyMovement: CashMoneyMovementDto, @AuthenticationPrincipal user: AlfaUserDetails): CashMoneyMovementDto {
        return cashMoneyMovementService.update(moneyMovement, user)
    }

    @DeleteMapping(path = ["/{id}"])
    fun delete(@PathVariable id: @Valid Long) {
        cashMoneyMovementService.delete(id)
    }
}