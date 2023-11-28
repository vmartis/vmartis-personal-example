package cz.smartbrains.qesu.module.order.service

import java.math.BigInteger

interface OrderStatementNumberFactory {
    fun nextNumber(year: Int): BigInteger
}