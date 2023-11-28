package cz.smartbrains.qesu.module.moneyBox.controller

import cz.smartbrains.qesu.module.common.controller.CodeListController
import cz.smartbrains.qesu.module.common.service.CodeListService
import cz.smartbrains.qesu.module.moneyBox.dto.MoneyMovementCategoryDto
import cz.smartbrains.qesu.module.moneyBox.entity.MoneyMovementCategory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/money-movement-category")
class MoneyMovementCategoryController(@Qualifier("moneyMovementCategoryService") service: CodeListService<MoneyMovementCategory, MoneyMovementCategoryDto>) : CodeListController<MoneyMovementCategoryDto>(service)