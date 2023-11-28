package cz.smartbrains.qesu.module.moneyBox.repository

import cz.smartbrains.qesu.module.moneyBox.entity.CashMoneyMovement
import org.springframework.data.jpa.repository.JpaRepository

interface CashMoneyMovementRepository : JpaRepository<CashMoneyMovement, Long>