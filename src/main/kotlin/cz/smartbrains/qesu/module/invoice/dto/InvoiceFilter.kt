package cz.smartbrains.qesu.module.invoice.dto

import cz.smartbrains.qesu.module.invoice.type.InvoiceStatus
import cz.smartbrains.qesu.module.invoice.type.InvoiceType
import java.time.LocalDate

class InvoiceFilter(var subscriberId: Long? = null,
                    var dateOfIssueFrom: LocalDate? = null,
                    var dateOfIssueTo: LocalDate? = null,
                    var type: InvoiceType? = null,
                    var status: InvoiceStatus? = null,
                    var limit: Int? = null,
                    var currency: String? = null,
                    var excludeId: Long? = null)