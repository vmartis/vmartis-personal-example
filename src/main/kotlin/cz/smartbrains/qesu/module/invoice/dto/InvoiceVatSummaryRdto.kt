package cz.smartbrains.qesu.module.invoice.dto

import java.math.BigDecimal

class InvoiceVatSummaryRdto(var vatRate: BigDecimal? = null, var amount: BigDecimal? = null, var vat: BigDecimal? = null)