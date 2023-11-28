package cz.smartbrains.qesu.module.moneyBox.service

interface MoneyMovementSubjectPairService {
    fun pairSubject(movementId: Long, subjectId: Long?)
}