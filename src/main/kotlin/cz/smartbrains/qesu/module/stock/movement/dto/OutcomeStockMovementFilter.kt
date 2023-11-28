package cz.smartbrains.qesu.module.stock.movement.dto

import cz.smartbrains.qesu.module.stock.movement.type.OutcomeStockMovementType

class OutcomeStockMovementFilter(var stockId: Long? = null,
                                 var type: OutcomeStockMovementType? = null,
                                 var unpaired: Boolean = false)