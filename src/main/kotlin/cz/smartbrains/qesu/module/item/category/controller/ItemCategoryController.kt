package cz.smartbrains.qesu.module.item.category.controller

import cz.smartbrains.qesu.module.common.controller.CodeListController
import cz.smartbrains.qesu.module.common.dto.ColorableCodeListDto
import cz.smartbrains.qesu.module.common.service.CodeListService
import cz.smartbrains.qesu.module.item.category.entity.ItemCategory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/item-category")
class ItemCategoryController(@Qualifier("itemCategoryService") service: CodeListService<ItemCategory, ColorableCodeListDto>) : CodeListController<ColorableCodeListDto>(service)