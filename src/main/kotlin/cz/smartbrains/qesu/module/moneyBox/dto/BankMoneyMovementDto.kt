package cz.smartbrains.qesu.module.moneyBox.dto

import cz.smartbrains.qesu.module.subject.dto.SubjectDto
import lombok.EqualsAndHashCode
import lombok.ToString

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
class BankMoneyMovementDto : MoneyMovementDto() {
    override var subject: SubjectDto? = null
    var mainMovementId: Long? = null
}