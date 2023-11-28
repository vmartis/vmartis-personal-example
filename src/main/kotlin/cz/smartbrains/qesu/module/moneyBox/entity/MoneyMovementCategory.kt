package cz.smartbrains.qesu.module.moneyBox.entity

import cz.smartbrains.qesu.module.common.entity.ColorableCodeList
import cz.smartbrains.qesu.module.moneyBox.type.MoneyMovementType
import lombok.EqualsAndHashCode
import javax.persistence.*

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "MONEY_MOVEMENT_CATEGORY", indexes = [Index(name = "IDX_MONEY_MOVEMENT_CATEGORY_LABEL", columnList = "LABEL")], uniqueConstraints = [UniqueConstraint(name = "MONEY_MOVEMENT_CATEGORY_ITEM_ORDER_UC", columnNames = ["ITEM_ORDER"])])
class MoneyMovementCategory : ColorableCodeList() {
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false, length = 20)
    var type: MoneyMovementType? = null
}