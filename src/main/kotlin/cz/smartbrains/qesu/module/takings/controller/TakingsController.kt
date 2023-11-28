package cz.smartbrains.qesu.module.takings.controller

import cz.smartbrains.qesu.module.common.dto.validation.OnCreate
import cz.smartbrains.qesu.module.common.dto.validation.OnUpdate
import cz.smartbrains.qesu.module.takings.dto.TakingsDto
import cz.smartbrains.qesu.module.takings.dto.TakingsFilter
import cz.smartbrains.qesu.module.takings.dto.TakingsTransferDto
import cz.smartbrains.qesu.module.takings.service.TakingsService
import cz.smartbrains.qesu.security.AlfaUserDetails
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import javax.validation.Valid
import javax.validation.groups.Default

@RestController
@RequestMapping("/api/takings")
class TakingsController(private val service: TakingsService) {
    @GetMapping
    fun find(@RequestParam(value = "company-branch-id", required = false) companyBranchId: Long?,
             @RequestParam(value = "company-branch-ids[]", required = false) companyBranchIds: List<Long>?,
             @RequestParam(required = false) date: LocalDate?,
             @RequestParam(required = false, name = "date-from") dateFrom: LocalDate?,
             @RequestParam(required = false, name = "date-to") dateTo: LocalDate?,
             @RequestParam(required = false) currency: String?): List<TakingsDto> {
        return service.findBy(TakingsFilter(companyBranchId, companyBranchIds, date, dateFrom, dateTo, currency))
    }

    @PostMapping
    fun create(@Validated(OnCreate::class, Default::class) @RequestBody takings: TakingsDto,
               @AuthenticationPrincipal userDetails: AlfaUserDetails): TakingsDto {
        return service.create(takings, userDetails)
    }

    @PutMapping
    fun update(@Validated(OnUpdate::class, Default::class) @RequestBody takings: TakingsDto,
               @AuthenticationPrincipal userDetails: AlfaUserDetails): TakingsDto {
        return service.update(takings, userDetails)
    }

    @PutMapping("/transfer")
    fun transfer(@Validated(OnUpdate::class, Default::class) @RequestBody transfer: TakingsTransferDto,
                 @AuthenticationPrincipal userDetails: AlfaUserDetails) {
        service.transfer(transfer, userDetails)
    }

    @DeleteMapping(path = ["/{id}"])
    fun delete(@PathVariable id: @Valid Long) {
        service.delete(id)
    }
}