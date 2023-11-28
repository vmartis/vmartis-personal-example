package cz.smartbrains.qesu.module.stock.service

import com.google.common.collect.Lists
import cz.smartbrains.qesu.module.common.exception.ServiceRuntimeException
import cz.smartbrains.qesu.module.stock.entity.StockBalance
import cz.smartbrains.qesu.module.stock.entity.StockItem
import cz.smartbrains.qesu.module.stock.entity.StockItem_
import cz.smartbrains.qesu.module.stock.movement.repository.StockMovementItemRepository
import cz.smartbrains.qesu.module.stock.repository.StockItemRepository
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.function.Consumer

@Service
class StockBalanceServiceImpl(private val stockItemRepository: StockItemRepository,
                              private val movementItemRepository: StockMovementItemRepository) : StockBalanceService {
    override fun updateBalance(stockId: Long, recalculatePrice: Boolean) {
        val stockItems = stockItemRepository.findAllByStockId(stockId, Sort.by(StockItem_.CREATED))
        val stockBalances = movementItemRepository.computeItemBalances(stockId)
        val currentItemIds: MutableList<Long?> = Lists.newArrayList()
        stockBalances.forEach(Consumer { stockBalance: StockBalance ->
            currentItemIds.add(stockBalance.itemId)
            val stockItemForItem = stockItems.stream().filter { stockItem: StockItem -> stockItem.item!!.id == stockBalance.itemId }.findFirst().orElseThrow { ServiceRuntimeException("stock.movement.invalid.item") }
            stockItemForItem.amount = stockBalance.sumAmount
            if (recalculatePrice && stockBalance.sumAmount > BigDecimal.ZERO) {
                stockItemForItem.price = stockBalance.sumPrice.divide(stockBalance.sumAmount, 2, RoundingMode.HALF_UP)
            }
        })

        // clear stock items without movements
        if (currentItemIds.size < stockItems.size) {
            stockItems.stream().filter { stockItem: StockItem -> !currentItemIds.contains(stockItem.item!!.id) }.forEach { stockItem: StockItem ->
                stockItem.amount = BigDecimal.ZERO
                stockItem.price = BigDecimal.ZERO
            }
        }
    }
}