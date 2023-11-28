package cz.smartbrains.qesu.module.stock.service

import org.mockito.kotlin.any
import cz.smartbrains.qesu.module.item.entity.Item
import cz.smartbrains.qesu.module.stock.entity.Stock
import cz.smartbrains.qesu.module.stock.entity.StockBalance
import cz.smartbrains.qesu.module.stock.entity.StockItem
import cz.smartbrains.qesu.module.stock.movement.repository.StockMovementItemRepository
import cz.smartbrains.qesu.module.stock.repository.StockItemRepository
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.math.RoundingMode

@ExtendWith(MockitoExtension::class)
class StockBalanceServiceImplTest {
    @Mock
    private lateinit var stockItemRepository: StockItemRepository

    @Mock
    private lateinit var movementItemRepository: StockMovementItemRepository

    @Mock
    private lateinit var item1: Item

    @Mock
    private lateinit var item2: Item

    @Mock
    private lateinit var stock: Stock
    private var balanceService: StockBalanceService? = null
    private var balances: List<StockBalance>? = null
    private var stockItems: List<StockItem>? = null
    private var originStockItem1: StockItem? = null
    private var originStockItem2: StockItem? = null

    @BeforeEach
    fun setUp() {
        balanceService = StockBalanceServiceImpl(stockItemRepository, movementItemRepository)
        balances = listOf(StockBalance(ITEM_1_ID, BigDecimal.valueOf(20), BigDecimal.valueOf(200)), StockBalance(ITEM_2_ID, BigDecimal.valueOf(10), BigDecimal.valueOf(150)))
        Mockito.`when`(item1.id).thenReturn(ITEM_1_ID)
        Mockito.`when`(item2.id).thenReturn(ITEM_2_ID)
        originStockItem1 = StockItem()
        originStockItem1!!.amount = BigDecimal.ONE
        originStockItem1!!.price = BigDecimal.ONE
        originStockItem1!!.item = item1
        originStockItem1!!.stock = stock
        originStockItem2 = StockItem()
        originStockItem2!!.amount = BigDecimal.TEN
        originStockItem2!!.price = BigDecimal.TEN
        originStockItem2!!.item = item2
        originStockItem2!!.stock = stock
        stockItems = listOf(originStockItem1!!, originStockItem2!!)
    }

    @Test
    fun updateBalance_allStockItemsMatchesRecalcualtePrice_allNewStockItemsArePersisted() {
        Mockito.`when`(movementItemRepository.computeItemBalances(STOCK_ID)).thenReturn(balances)
        Mockito.`when`(stockItemRepository.findAllByStockId(Mockito.eq(STOCK_ID), any())).thenReturn(stockItems)
        balanceService!!.updateBalance(STOCK_ID, true)
        assertThat(originStockItem1!!.price).isEqualTo(BigDecimal.valueOf(10.00).setScale(2, RoundingMode.HALF_UP))
        assertThat(originStockItem1!!.amount).isEqualTo(BigDecimal.valueOf(20))
        assertThat(originStockItem1!!.stock).isSameAs(stock)
        assertThat(originStockItem1!!.item).isSameAs(item1)
        assertThat(originStockItem2!!.price).isEqualTo(BigDecimal.valueOf(15.00).setScale(2, RoundingMode.HALF_UP))
        assertThat(originStockItem2!!.amount).isEqualTo(BigDecimal.valueOf(10))
        assertThat(originStockItem2!!.stock).isSameAs(stock)
        assertThat(originStockItem2!!.item).isSameAs(item2)
    }
    @Test
    fun updateBalance_doNotRecalcualtePrice_allNewStockItemsArePersisted() {
        Mockito.`when`(movementItemRepository.computeItemBalances(STOCK_ID)).thenReturn(balances)
        Mockito.`when`(stockItemRepository.findAllByStockId(Mockito.eq(STOCK_ID), any())).thenReturn(stockItems)
        balanceService!!.updateBalance(STOCK_ID)
        assertThat(originStockItem1!!.price).isEqualTo(BigDecimal.ONE)
        assertThat(originStockItem1!!.amount).isEqualTo(BigDecimal.valueOf(20))
        assertThat(originStockItem1!!.stock).isSameAs(stock)
        assertThat(originStockItem1!!.item).isSameAs(item1)
        assertThat(originStockItem2!!.price).isEqualTo(BigDecimal.TEN)
        assertThat(originStockItem2!!.amount).isEqualTo(BigDecimal.valueOf(10))
        assertThat(originStockItem2!!.stock).isSameAs(stock)
        assertThat(originStockItem2!!.item).isSameAs(item2)
    }

    @Test
    fun updateBalance_obsoleteStockItem_amountIsZero() {
        Mockito.`when`(movementItemRepository.computeItemBalances(STOCK_ID)).thenReturn(emptyList())
        Mockito.`when`(stockItemRepository.findAllByStockId(Mockito.eq(STOCK_ID), any())).thenReturn(stockItems)
        balanceService!!.updateBalance(STOCK_ID)
        assertThat(originStockItem1!!.price).isEqualTo(BigDecimal.ZERO)
        assertThat(originStockItem1!!.amount).isEqualTo(BigDecimal.ZERO)
        assertThat(originStockItem1!!.stock).isSameAs(stock)
        assertThat(originStockItem1!!.item).isSameAs(item1)
        assertThat(originStockItem2!!.price).isEqualTo(BigDecimal.ZERO)
        assertThat(originStockItem2!!.amount).isEqualTo(BigDecimal.ZERO)
        assertThat(originStockItem2!!.stock).isSameAs(stock)
        assertThat(originStockItem2!!.item).isSameAs(item2)
    }

    companion object {
        private const val ITEM_1_ID = 5L
        private const val ITEM_2_ID = 6L
        private const val STOCK_ID = 10L
    }
}