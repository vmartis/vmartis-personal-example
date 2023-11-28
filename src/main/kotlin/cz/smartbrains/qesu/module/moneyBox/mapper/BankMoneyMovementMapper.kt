package cz.smartbrains.qesu.module.moneyBox.mapper

import cz.smartbrains.qesu.module.invoice.mapper.InvoiceMapper
import cz.smartbrains.qesu.module.user.dto.UserDto
import cz.smartbrains.qesu.module.user.entity.User
import cz.smartbrains.qesu.module.moneyBox.dto.BankMoneyMovementDto
import cz.smartbrains.qesu.module.moneyBox.entity.BankMoneyMovement
import cz.smartbrains.qesu.module.moneyBox.type.MoneyMovementType
import cz.smartbrains.qesu.module.subject.mapper.SubjectBaseMapper
import org.mapstruct.Mapper
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal

@Mapper(componentModel = "spring")
abstract class BankMoneyMovementMapper {
    @Autowired
    var moneyBoxMapper: MoneyBoxMapper? = null

    @Autowired
    var invoiceMapper: InvoiceMapper? = null

    @Autowired
    var subjectBaseMapper: SubjectBaseMapper? = null

    @Autowired
    var moneyMovementCategoryMapper: MoneyMovementCategoryMapper? = null

    fun doToDto(moneyMovement: BankMoneyMovement?): BankMoneyMovementDto? {
        if (moneyMovement == null) {
            return null
        }
        val bankMoneyMovementDto = BankMoneyMovementDto()
        bankMoneyMovementDto.subject = subjectBaseMapper!!.doToDto(moneyMovement.subject)
        bankMoneyMovementDto.id = moneyMovement.id
        bankMoneyMovementDto.moneyBox = moneyBoxMapper!!.doToDto(moneyMovement.moneyBox)
        bankMoneyMovementDto.category = moneyMovementCategoryMapper!!.doToDto(moneyMovement.category)
        bankMoneyMovementDto.invoice = invoiceMapper!!.doToDto(moneyMovement.invoice)
        bankMoneyMovementDto.type = moneyMovement.type
        bankMoneyMovementDto.date = moneyMovement.date
        bankMoneyMovementDto.taxableDate = moneyMovement.taxableDate
        bankMoneyMovementDto.totalAmount = moneyMovement.totalAmount
        bankMoneyMovementDto.accountingType = moneyMovement.accountingType
        bankMoneyMovementDto.totalVat = moneyMovement.totalVat
        bankMoneyMovementDto.totalWithoutVat = moneyMovement.totalWithoutVat
        bankMoneyMovementDto.note = moneyMovement.note
        bankMoneyMovementDto.active = moneyMovement.active
        bankMoneyMovementDto.created = moneyMovement.created
        bankMoneyMovementDto.createdBy = userDoToDto(moneyMovement.createdBy)
        bankMoneyMovementDto.updated = moneyMovement.updated
        bankMoneyMovementDto.updatedBy = userDoToDto(moneyMovement.updatedBy)
        return bankMoneyMovementDto
    }

    fun dtoToDo(moneyMovementDto: BankMoneyMovementDto?): BankMoneyMovement? {
        if (moneyMovementDto == null) {
            return null
        }
        val moneyMovement = BankMoneyMovement()
        moneyMovement.id = moneyMovementDto.id
        moneyMovement.moneyBox = moneyBoxMapper!!.dtoToDo(moneyMovementDto.moneyBox)
        moneyMovement.category = moneyMovementCategoryMapper!!.dtoToDo(moneyMovementDto.category)
        moneyMovement.invoice = invoiceMapper!!.dtoToDo(moneyMovementDto.invoice)
        moneyMovement.subject = subjectBaseMapper!!.dtoToDo(moneyMovementDto.subject)
        moneyMovement.type = moneyMovementDto.type
        moneyMovement.date = moneyMovementDto.date
        moneyMovement.taxableDate = moneyMovementDto.taxableDate
        moneyMovement.totalAmount = moneyMovementDto.totalAmount
        moneyMovement.accountingType = moneyMovementDto.accountingType
        moneyMovement.totalVat = moneyMovementDto.totalVat
        moneyMovement.note = moneyMovementDto.note
        moneyMovement.active = moneyMovementDto.active
        moneyMovement.totalWithoutVat = calcTotalWithoutVat(moneyMovement.totalAmount, moneyMovement.totalVat)

        //ensure negative value for outcome movements
        if (moneyMovement.type === MoneyMovementType.OUTCOME) {
            moneyMovement.totalAmount = moneyMovement.totalAmount!!.abs().multiply(BigDecimal.valueOf(-1))
            moneyMovement.totalWithoutVat = moneyMovement.totalWithoutVat!!.abs().multiply(BigDecimal.valueOf(-1))
            if (moneyMovement.totalVat != null) {
                moneyMovement.totalVat = moneyMovement.totalVat!!.abs().multiply(BigDecimal.valueOf(-1))
            }
        }
        return moneyMovement
    }

    private fun calcTotalWithoutVat(totalAmount: BigDecimal?, totalVat: BigDecimal?): BigDecimal? {
        return if (totalVat == null) totalAmount else totalAmount!!.subtract(totalVat)
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