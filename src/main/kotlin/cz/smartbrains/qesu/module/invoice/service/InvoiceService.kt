package cz.smartbrains.qesu.module.invoice.service

import cz.smartbrains.qesu.module.invoice.dto.InvoiceDto
import cz.smartbrains.qesu.module.invoice.dto.InvoiceFilter
import org.springframework.security.access.prepost.PreAuthorize
import java.math.BigDecimal

interface InvoiceService {
    @PreAuthorize("hasAuthority('INVOICE')")
    fun find(id: Long): InvoiceDto

    @PreAuthorize("hasAnyAuthority('INVOICE', 'FINANCE_MONEY_BOX')")
    fun findAll(filter: InvoiceFilter): List<InvoiceDto>

    @PreAuthorize("hasAuthority('INVOICE')")
    fun create(invoiceDto: InvoiceDto): InvoiceDto

    @PreAuthorize("hasAuthority('INVOICE')")
    fun update(invoiceDto: InvoiceDto): InvoiceDto

    @PreAuthorize("hasAuthority('INVOICE')")
    fun delete(id: Long)
    fun pay(invoiceId: Long, amount: BigDecimal)
    fun cancelPayment(invoiceId: Long, amount: BigDecimal)
}