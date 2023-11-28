package cz.smartbrains.qesu.module.order.dto

import java.time.LocalDate

class OrderStatementFilter(var dateFrom: LocalDate? = null,
                           var dateTo: LocalDate? = null,
                           var paymentTypeId: Long? = null,
                           var subscriberId: Long? = null)