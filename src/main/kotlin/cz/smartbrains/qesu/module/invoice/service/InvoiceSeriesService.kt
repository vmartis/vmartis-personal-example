package cz.smartbrains.qesu.module.invoice.service

import cz.smartbrains.qesu.module.invoice.dto.InvoiceSeriesDto
import cz.smartbrains.qesu.module.invoice.entity.InvoiceSeriesNumber
import org.springframework.security.access.prepost.PreAuthorize

interface InvoiceSeriesService {
    @PreAuthorize("hasAnyAuthority('SETTING_INVOICE', 'INVOICE')")
    fun findAll(): List<InvoiceSeriesDto>

    @PreAuthorize("hasAuthority('SETTING_INVOICE')")
    fun create(invoiceSeries: InvoiceSeriesDto): InvoiceSeriesDto

    @PreAuthorize("hasAuthority('SETTING_INVOICE')")
    fun update(invoiceSeries: InvoiceSeriesDto): InvoiceSeriesDto

    @PreAuthorize("hasAuthority('SETTING_INVOICE')")
    fun updatePosition(invoiceSeriesId: Long, position: Int): InvoiceSeriesDto
    fun nextNumber(id: Long): InvoiceSeriesNumber
}