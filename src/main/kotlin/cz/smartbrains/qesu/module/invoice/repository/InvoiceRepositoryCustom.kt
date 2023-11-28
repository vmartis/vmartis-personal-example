package cz.smartbrains.qesu.module.invoice.repository

import cz.smartbrains.qesu.module.invoice.dto.InvoiceFilter
import cz.smartbrains.qesu.module.invoice.entity.Invoice

interface InvoiceRepositoryCustom {
    fun findByFilter(filter: InvoiceFilter): List<Invoice>
}