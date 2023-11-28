package cz.smartbrains.qesu.module.invoice.mapper

import cz.smartbrains.qesu.module.bank.mapper.BankAccountMapper
import cz.smartbrains.qesu.module.common.service.Messages
import cz.smartbrains.qesu.module.company.mapper.CompanyMapper
import cz.smartbrains.qesu.module.invoice.dto.InvoiceDto
import cz.smartbrains.qesu.module.invoice.dto.InvoiceItemDto
import cz.smartbrains.qesu.module.invoice.entity.Invoice
import cz.smartbrains.qesu.module.invoice.entity.InvoiceItem
import cz.smartbrains.qesu.module.moneyBox.dto.BankMoneyMovementDto
import cz.smartbrains.qesu.module.moneyBox.dto.CashMoneyMovementDto
import cz.smartbrains.qesu.module.moneyBox.dto.MoneyMovementDto
import cz.smartbrains.qesu.module.moneyBox.entity.BankMoneyMovement
import cz.smartbrains.qesu.module.moneyBox.entity.CashMoneyMovement
import cz.smartbrains.qesu.module.moneyBox.entity.MoneyMovement
import cz.smartbrains.qesu.module.moneyBox.mapper.MoneyBoxMapper
import cz.smartbrains.qesu.module.moneyBox.mapper.MoneyMovementCategoryMapper
import org.hibernate.Hibernate
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.util.stream.Collectors

@Mapper(uses = [InvoiceItemMapper::class, CompanyMapper::class, BankAccountMapper::class], componentModel = "spring")
abstract class InvoiceMapper {
    @JvmField
    @Autowired
    protected var messages: Messages? = null

    @Autowired
    protected var moneyMovementCategoryMapper: MoneyMovementCategoryMapper? = null

    @Autowired
    protected var moneyBoxMapper: MoneyBoxMapper? = null

    @Autowired
    protected var invoiceItemMapper: InvoiceItemMapper? = null
    @Mappings(Mapping(target = "invoiceSeries", ignore = true), Mapping(target = "orderStatement", ignore = true), Mapping(target = "movements", expression = "java(movementDosToDtos(invoice.getMovements()))"))
    abstract fun doToDto(invoice: Invoice?): InvoiceDto?
    @Mappings(Mapping(target = "created", ignore = true), Mapping(target = "updated", ignore = true), Mapping(target = "items", expression = "java(itemsDoToDto(invoiceDto))"), Mapping(target = "movements", ignore = true))
    abstract fun dtoToDo(invoiceDto: InvoiceDto?): Invoice?
    protected fun movementDosToDtos(movements: List<MoneyMovement>?): List<MoneyMovementDto> {
        return if (movements == null || movements.isEmpty()) {
            emptyList()
        } else movements.stream()
                .map { movement: MoneyMovement -> movementDoToDto(movement)!! }
                .collect(Collectors.toList())
    }

    protected fun movementDoToDto(movement: MoneyMovement?): MoneyMovementDto? {
        if (movement == null) {
            return null
        }
        return if (Hibernate.getClass(movement) == BankMoneyMovement::class.java) {
            val dto = BankMoneyMovementDto()
            dto.date = movement.date
            dto.note = movement.note
            dto.totalAmount = movement.totalAmount
            dto.totalWithoutVat = movement.totalWithoutVat
            dto.totalVat = movement.totalVat
            dto.moneyBox = moneyBoxMapper!!.doToDto(movement.moneyBox)
            dto.category = moneyMovementCategoryMapper!!.doToDto(movement.category)
            dto
        } else if (Hibernate.getClass(movement) == CashMoneyMovement::class.java) {
            val dto = CashMoneyMovementDto()
            dto.date = movement.date
            dto.note = movement.note
            dto.totalAmount = movement.totalAmount
            dto.totalWithoutVat = movement.totalWithoutVat
            dto.totalVat = movement.totalVat
            dto.moneyBox = moneyBoxMapper!!.doToDto(movement.moneyBox)
            dto.category = moneyMovementCategoryMapper!!.doToDto(movement.category)
            dto
        } else {
            throw IllegalStateException("Not supported money movement type.")
        }
    }

    protected fun itemsDoToDto(invoiceDto: InvoiceDto?): List<InvoiceItem> {
        return if (invoiceDto == null || invoiceDto.items == null) {
            emptyList()
        } else invoiceDto.items!!.stream()
                .map { invoiceItemDto: InvoiceItemDto -> invoiceItemMapper!!.dtoToDo(invoiceItemDto)!! }
                .peek { invoiceItem: InvoiceItem? ->
                    if (invoiceDto.transferredVat) {
                        invoiceItem!!.vatRate = BigDecimal.ZERO
                    }
                }.collect(Collectors.toList())
    }
}