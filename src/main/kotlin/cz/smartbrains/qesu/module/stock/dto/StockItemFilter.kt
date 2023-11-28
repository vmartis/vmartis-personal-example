package cz.smartbrains.qesu.module.stock.dto

class StockItemFilter(var stockId: Long? = null,
                      var stockIds: List<Long>? = null,
                      var itemId: Long? = null,
                      var activeOnly: Boolean = false)