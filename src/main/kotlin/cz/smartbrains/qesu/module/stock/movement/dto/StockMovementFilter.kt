package cz.smartbrains.qesu.module.stock.movement.dto

import java.time.LocalDate

class StockMovementFilter(var stockId: Long? = null,
                          var itemId: Long? = null,
                          var dateFrom: LocalDate? = null,
                          var dateTo: LocalDate? = null,
                          var itemBatchNumber: String? = null)