package cz.smartbrains.qesu.module.invoice.controller

import cz.smartbrains.qesu.module.common.controller.CodeListController
import cz.smartbrains.qesu.module.common.service.CodeListService
import cz.smartbrains.qesu.module.invoice.dto.VatRateDto
import cz.smartbrains.qesu.module.invoice.entity.VatRate
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/vat-rate")
class VatRateController(@Qualifier("vatRateService") service: CodeListService<VatRate, VatRateDto>) : CodeListController<VatRateDto>(service)