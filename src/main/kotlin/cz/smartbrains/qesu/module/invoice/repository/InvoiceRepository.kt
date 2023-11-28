package cz.smartbrains.qesu.module.invoice.repository

import cz.smartbrains.qesu.module.invoice.entity.Invoice
import cz.smartbrains.qesu.module.invoice.type.InvoiceType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

interface InvoiceRepository : JpaRepository<Invoice, Long>, InvoiceRepositoryCustom {
    @Query("SELECT SUM(i.totalVat) FROM Invoice i where i.taxableDate BETWEEN ?1 AND ?2 AND i.currency = ?3 AND i.type = ?4")
    fun sumVat(dateFrom: LocalDate?, to: LocalDate, currency: String, type: InvoiceType): Optional<BigDecimal>

    @Query("SELECT SUM(i.totalAmount - i.totalVat) FROM Invoice i where i.taxableDate BETWEEN ?1 AND ?2 AND i.currency = ?3")
    fun sumAmount(dateFrom: LocalDate, to: LocalDate, currency: String): Optional<BigDecimal>
    fun countByBankAccountId(bankAccountId: Long): Long
}