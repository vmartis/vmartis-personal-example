package cz.smartbrains.qesu.module.order.dto

import cz.smartbrains.qesu.module.order.type.OrderState
import java.time.LocalDate

class OrderFilter(var supplierBranchId: Long? = null,
                  var subscriberId: Long? = null,
                  var regionId: Long? = null,
                  var noStatement: Boolean? = null,
                  var currency: String? = null,
                  var state: OrderState? = null,
                  var date: LocalDate? = null,
                  var dateFrom: LocalDate? = null,
                  var dateTo: LocalDate? = null)