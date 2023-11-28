package cz.smartbrains.qesu.module.moneyBox.mapper

import cz.smartbrains.qesu.module.common.exception.ServiceRuntimeException
import cz.smartbrains.qesu.module.invoice.mapper.InvoiceMapper
import cz.smartbrains.qesu.module.user.dto.UserDto
import cz.smartbrains.qesu.module.user.entity.User
import cz.smartbrains.qesu.module.bank.mapper.BankTransactionMapper
import cz.smartbrains.qesu.module.moneyBox.dto.CashMoneyMovementDto
import cz.smartbrains.qesu.module.moneyBox.entity.CashMoneyMovement
import cz.smartbrains.qesu.module.moneyBox.type.MoneyMovementType
import cz.smartbrains.qesu.module.subject.mapper.SubjectBaseMapper
import org.mapstruct.Mapper
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal

@Mapper(uses = [MoneyBoxMapper::class, InvoiceMapper::class, SubjectBaseMapper::class, BankTransactionMapper::class, MoneyMovementCategoryMapper::class], componentModel = "spring")
abstract class CashMoneyMovementMapper {
    @Autowired
    protected var moneyBoxMapper: MoneyBoxMapper? = null

    @Autowired
    protected var invoiceMapper: InvoiceMapper? = null

    @Autowired
    protected var subjectBaseMapper: SubjectBaseMapper? = null

    @Autowired
    protected var moneyMovementCategoryMapper: MoneyMovementCategoryMapper? = null
    abstract fun doToDto(moneyMovement: CashMoneyMovement?): CashMoneyMovementDto?
    fun dtoToDo(moneyMovementDto: CashMoneyMovementDto?): CashMoneyMovement? {
        if (moneyMovementDto == null) {
            return null
        }
        val moneyMovement = CashMoneyMovement()
        moneyMovement.id = moneyMovementDto.id
        moneyMovement.moneyBox = moneyBoxMapper!!.dtoToDo(moneyMovementDto.moneyBox)
        moneyMovement.category = moneyMovementCategoryMapper!!.dtoToDo(moneyMovementDto.category)
        moneyMovement.invoice = invoiceMapper!!.dtoToDo(moneyMovementDto.invoice)
        moneyMovement.subject = subjectBaseMapper!!.dtoToDo(moneyMovementDto.subject)
        moneyMovement.type = moneyMovementDto.type
        moneyMovement.date = moneyMovementDto.date
        moneyMovement.taxableDate = moneyMovementDto.taxableDate
        moneyMovement.accountingType = moneyMovementDto.accountingType
        moneyMovement.totalVat = moneyMovementDto.totalVat
        moneyMovement.amountCash = if (moneyMovementDto.amountCash == null) BigDecimal.ZERO else moneyMovementDto.amountCash
        moneyMovement.amountCheque = if (moneyMovementDto.amountCheque == null) BigDecimal.ZERO else moneyMovementDto.amountCheque
        moneyMovement.note = moneyMovementDto.note
        moneyMovement.active = moneyMovementDto.active
        moneyMovement.totalAmount = calcTotalAmount(moneyMovement.amountCash, moneyMovement.amountCheque)
        moneyMovement.totalWithoutVat = calcTotalWithoutVat(moneyMovement.totalAmount, moneyMovement.totalVat)

        //ensure negative value for outcome movements
        if (moneyMovement.type === MoneyMovementType.OUTCOME) {
            moneyMovement.totalAmount = moneyMovement.totalAmount!!.abs().multiply(BigDecimal.valueOf(-1))
            moneyMovement.amountCash = moneyMovement.amountCash!!.abs().multiply(BigDecimal.valueOf(-1))
            moneyMovement.amountCheque = moneyMovement.amountCheque!!.abs().multiply(BigDecimal.valueOf(-1))
            moneyMovement.totalWithoutVat = moneyMovement.totalWithoutVat!!.abs().multiply(BigDecimal.valueOf(-1))
            if (moneyMovement.totalVat != null) {
                moneyMovement.totalVat = moneyMovement.totalVat!!.abs().multiply(BigDecimal.valueOf(-1))
            }
        }
        return moneyMovement
    }

    fun calcTotalAmount(amountCash: BigDecimal?, amountCheque: BigDecimal?): BigDecimal {
        if (amountCash == null && amountCheque == null) {
            throw ServiceRuntimeException("moneyMovement.cash.amount.empty")
        }
        var totalAmount = BigDecimal.ZERO
        if (amountCash != null) {
            totalAmount = totalAmount.add(amountCash)
        }
        if (amountCheque != null) {
            totalAmount = totalAmount.add(amountCheque)
        }
        return totalAmount
    }

    private fun calcTotalWithoutVat(totalAmount: BigDecimal?, totalVat: BigDecimal?): BigDecimal {
        return if (totalVat == null) totalAmount!!.abs() else totalAmount!!.abs().subtract(totalVat.abs())
    }

    protected fun userDoToDto(user: User?): UserDto? {
        if (user == null) {
            return null
        }
        val userDto = UserDto()
        userDto.firstname = user.firstname
        userDto.surname = user.surname
        return userDto
    }
}