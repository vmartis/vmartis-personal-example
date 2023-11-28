package cz.smartbrains.qesu.module.moneyBox.controller

import cz.smartbrains.qesu.module.moneyBox.dto.MoneyMovementDto
import cz.smartbrains.qesu.module.moneyBox.dto.MoneyMovementFilter
import cz.smartbrains.qesu.module.moneyBox.service.MoneyMovementService
import cz.smartbrains.qesu.module.moneyBox.type.AccountingType
import cz.smartbrains.qesu.module.moneyBox.type.MoneyMovementType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/money-movement")
class MoneyMovementController(private val service: MoneyMovementService) {
    @GetMapping
    fun list(@RequestParam(required = false) currency: String?,
             @RequestParam(required = false) moneyBoxId: Long?,
             @RequestParam(required = false) subjectId: Long?,
             @RequestParam(required = false) dateFrom: LocalDate?,
             @RequestParam(required = false) taxableDateFrom: LocalDate?,
             @RequestParam(required = false) active: Boolean?,
             @RequestParam(required = false) dateTo: LocalDate?,
             @RequestParam(required = false) taxableDateTo: LocalDate?,
             @RequestParam(required = false, name = "types[]") types: Set<MoneyMovementType>?,
             @RequestParam(required = false, name = "categories[]") categories: Set<Long>?,
             @RequestParam(required = false, name = "accountingTypes[]") accountingTypes: Set<AccountingType>?): List<MoneyMovementDto> {
        return service.findByFilter(MoneyMovementFilter(currency,
                active,
                moneyBoxId,
                subjectId,
                dateFrom,
                dateTo,
                taxableDateFrom,
                taxableDateTo,
                types,
                accountingTypes,
                categories))
    }
}