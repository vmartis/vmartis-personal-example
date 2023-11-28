package cz.smartbrains.qesu.module.invoice.config

import cz.smartbrains.qesu.module.user.type.PermissionType
import cz.smartbrains.qesu.module.common.service.CodeListService
import cz.smartbrains.qesu.module.common.service.CodeListServiceImpl
import cz.smartbrains.qesu.module.common.service.OrderableEntityService
import cz.smartbrains.qesu.module.invoice.dto.VatRateDto
import cz.smartbrains.qesu.module.invoice.entity.VatRate
import cz.smartbrains.qesu.module.invoice.mapper.VatRateMapper
import cz.smartbrains.qesu.module.invoice.repository.VatRateRepository
import cz.smartbrains.qesu.security.PermissionChecker
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class VatRateConfig {
    @Bean
    fun vatRateService(permissionChecker: PermissionChecker,
                       orderableEntityService: OrderableEntityService,
                       repository: VatRateRepository,
                       mapper: VatRateMapper): CodeListService<VatRate, VatRateDto> {
        return CodeListServiceImpl(permissionChecker, orderableEntityService, repository, mapper, PermissionType.SETTING_FINANCE)
    }
}