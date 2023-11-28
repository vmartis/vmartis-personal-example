package cz.smartbrains.qesu.module.item.category.config

import cz.smartbrains.qesu.module.user.type.PermissionType
import cz.smartbrains.qesu.module.common.dto.ColorableCodeListDto
import cz.smartbrains.qesu.module.common.service.CodeListService
import cz.smartbrains.qesu.module.common.service.CodeListServiceImpl
import cz.smartbrains.qesu.module.common.service.OrderableEntityService
import cz.smartbrains.qesu.module.item.category.entity.ItemCategory
import cz.smartbrains.qesu.module.item.category.mapper.ItemCategoryMapper
import cz.smartbrains.qesu.module.item.category.repository.ItemCategoryRepository
import cz.smartbrains.qesu.security.PermissionChecker
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ItemCategoryConfig {
    @Bean
    fun itemCategoryService(permissionChecker: PermissionChecker,
                            orderableEntityService: OrderableEntityService,
                            repository: ItemCategoryRepository,
                            mapper: ItemCategoryMapper): CodeListService<ItemCategory, ColorableCodeListDto> {
        return CodeListServiceImpl(permissionChecker, orderableEntityService, repository, mapper, PermissionType.SETTING_STOCK)
    }
}