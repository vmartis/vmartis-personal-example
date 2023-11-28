package cz.smartbrains.qesu.module.order.service

import java.math.BigInteger

interface OrderNumberFactory {
    fun nextNumber(year: Int): BigInteger
}