package cz.smartbrains.qesu.module.item.category.entity

import cz.smartbrains.qesu.module.common.entity.ColorableCodeList
import javax.persistence.Entity
import javax.persistence.Index
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(name = "ITEM_CATEGORY", indexes = [Index(name = "IDX_ITEM_CATEGORY_LABEL", columnList = "LABEL")], uniqueConstraints = [UniqueConstraint(name = "ITEM_CATEGORY_ITEM_ORDER_UC", columnNames = ["ITEM_ORDER"])])
class ItemCategory : ColorableCodeList()