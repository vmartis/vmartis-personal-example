package cz.smartbrains.qesu.module.moneyBox.config

import cz.smartbrains.qesu.module.common.service.CodeListService
import cz.smartbrains.qesu.module.common.service.CodeListServiceImpl
import cz.smartbrains.qesu.module.common.service.OrderableEntityService
import cz.smartbrains.qesu.module.user.type.PermissionType
import cz.smartbrains.qesu.module.moneyBox.dto.MoneyMovementCategoryDto
import cz.smartbrains.qesu.module.moneyBox.entity.MoneyMovementCategory
import cz.smartbrains.qesu.module.moneyBox.mapper.MoneyMovementCategoryMapper
import cz.smartbrains.qesu.module.moneyBox.repository.MoneyMovementCategoryRepository
import cz.smartbrains.qesu.security.PermissionChecker
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MoneyMovementCategoryConfig {
    @Bean
    fun moneyMovementCategoryService(permissionChecker: PermissionChecker,
                                     orderableEntityService: OrderableEntityService,
                                     repository: MoneyMovementCategoryRepository,
                                     mapper: MoneyMovementCategoryMapper): CodeListService<MoneyMovementCategory, MoneyMovementCategoryDto> {
        return CodeListServiceImpl(permissionChecker, orderableEntityService, repository, mapper, PermissionType.SETTING_FINANCE)
    }
}