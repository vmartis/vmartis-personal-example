package cz.smartbrains.qesu.module.stock.movement.service

import org.mockito.kotlin.any
import cz.smartbrains.qesu.module.common.exception.ServiceRuntimeException
import cz.smartbrains.qesu.module.item.dto.ItemDto
import cz.smartbrains.qesu.module.item.entity.Item
import cz.smartbrains.qesu.module.item.service.ItemBatchService
import cz.smartbrains.qesu.module.stock.dto.StockDto
import cz.smartbrains.qesu.module.stock.entity.StockItem
import cz.smartbrains.qesu.module.stock.movement.dto.OutcomeStockMovementDto
import cz.smartbrains.qesu.module.stock.movement.dto.StockMovementItemDto
import cz.smartbrains.qesu.module.stock.movement.entity.OutcomeStockMovement
import cz.smartbrains.qesu.module.stock.movement.entity.StockMovementItem
import cz.smartbrains.qesu.module.stock.movement.mapper.OutcomeStockMovementMapper
import cz.smartbrains.qesu.module.stock.movement.repository.OutcomeStockMovementRepository
import cz.smartbrains.qesu.module.stock.movement.repository.StockMovementNumberFactory
import cz.smartbrains.qesu.module.stock.repository.StockItemRepository
import cz.smartbrains.qesu.module.stock.service.StockBalanceService
import cz.smartbrains.qesu.module.stock.service.StockRecordService
import cz.smartbrains.qesu.module.user.repository.UserRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.lenient
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.util.*

@ExtendWith(MockitoExtension::class)
class OutcomeStockMovementServiceImplTest {
    @Mock
    private val stockItemRepository: StockItemRepository? = null

    @Mock
    private val outcomeStockMovementRepository: OutcomeStockMovementRepository? = null

    @Mock
    private val outcomeStockMovementMapper: OutcomeStockMovementMapper? = null

    @Mock
    private val stockMovementNumberFactory: StockMovementNumberFactory? = null

    @Mock
    private val balanceService: StockBalanceService? = null

    @Mock
    private val userRepository: UserRepository? = null

    @Mock
    private val stockRecordService: StockRecordService? = null

    @Mock
    private val itemBatchService: ItemBatchService? = null

    @Mock
    private val item1: Item? = null

    @Mock
    private val itemDto1: ItemDto? = null

    @Mock
    private val item2: Item? = null

    @Mock
    private val itemDto2: ItemDto? = null

    @Mock
    private val stock: StockDto? = null
    private var service: OutcomeStockMovementServiceImpl? = null
    private var stockItems: List<StockItem>? = null
    private var originStockItem1: StockItem? = null
    private var originStockItem2: StockItem? = null

    @BeforeEach
    fun setUp() {
        service = OutcomeStockMovementServiceImpl(outcomeStockMovementRepository!!,
                stockItemRepository!!,
                outcomeStockMovementMapper!!,
                stockMovementNumberFactory!!,
                stockRecordService!!,
                balanceService!!,
                userRepository!!,
                itemBatchService!!)
        Mockito.`when`(item1!!.id).thenReturn(ITEM_1_ID)
        lenient().`when`(itemDto1!!.id).thenReturn(ITEM_1_ID)
        lenient().`when`(item2!!.id).thenReturn(ITEM_2_ID)
        lenient().`when`(itemDto2!!.id).thenReturn(ITEM_2_ID)
        Mockito.`when`(stock!!.id).thenReturn(STOCK_ID)
        originStockItem1 = StockItem()
        originStockItem1!!.amount = BigDecimal.ONE
        originStockItem1!!.price = BigDecimal.ONE
        originStockItem1!!.item = item1
        originStockItem2 = StockItem()
        originStockItem2!!.amount = BigDecimal.TEN
        originStockItem2!!.price = BigDecimal.TEN
        originStockItem2!!.item = item2
        stockItems = arrayListOf(originStockItem1!!, originStockItem2!!)
        Mockito.`when`(stockItemRepository.findAllByStockId(Mockito.eq(STOCK_ID), any())).thenReturn(stockItems)
    }

    @Test
    fun validItems_invalidItemInMovement_exIsThrown() {
        val movement = OutcomeStockMovementDto()
        val stockMovementItem = StockMovementItemDto()
        val unknownItem = ItemDto()
        unknownItem.id = 0L
        stockMovementItem.item = unknownItem
        movement.items = arrayListOf(stockMovementItem)
        movement.stock = stock
        Assertions.assertThatExceptionOfType(ServiceRuntimeException::class.java)
                .isThrownBy { service!!.validateItems(movement) }
                .withMessage("stock.movement.outcome.invalid.item")
    }

    @Test
    fun validItems_invalidItemAmountInMovement_exIsThrown() {
        val movement = OutcomeStockMovementDto()
        val stockMovementItem = StockMovementItemDto()
        stockMovementItem.item = itemDto1
        stockMovementItem.amount = originStockItem1!!.amount!!.add(BigDecimal.ONE)
        stockMovementItem.price = originStockItem1!!.price
        movement.items = arrayListOf(stockMovementItem)
        movement.stock = stock
        Assertions.assertThatExceptionOfType(ServiceRuntimeException::class.java)
                .isThrownBy { service!!.validateItems(movement) }
                .withMessage("stock.movement.outcome.invalid.amount")
    }

    @Test
    fun validItems_invalidItemPriceInMovement_exIsThrown() {
        val movement = OutcomeStockMovementDto()
        val stockMovementItem = StockMovementItemDto()
        stockMovementItem.item = itemDto1
        stockMovementItem.amount = originStockItem1!!.amount
        stockMovementItem.price = originStockItem1!!.price!!.add(BigDecimal.ONE)
        movement.items = arrayListOf(stockMovementItem)
        movement.stock = stock
        Assertions.assertThatExceptionOfType(ServiceRuntimeException::class.java)
                .isThrownBy { service!!.validateItems(movement) }
                .withMessage("stock.movement.outcome.invalid.price")
    }

    @Test
    fun validItems_allItemAraValidInMovement_noExIsThrown() {
        val movement = OutcomeStockMovementDto()
        val stockMovementItem1 = StockMovementItemDto()
        stockMovementItem1.item = itemDto1
        stockMovementItem1.amount = originStockItem1!!.amount
        stockMovementItem1.price = originStockItem1!!.price
        val stockMovementItem2 = StockMovementItemDto()
        stockMovementItem2.item = itemDto2
        stockMovementItem2.amount = originStockItem2!!.amount
        stockMovementItem2.price = originStockItem2!!.price
        movement.items = arrayListOf(stockMovementItem1, stockMovementItem2)
        movement.stock = stock
        service!!.validateItems(movement)
    }

    @Test
    fun validItems_updateAmountGreaterThanCurrentAmountAndEqualThanSum_validationPass() {
        val originMovement = OutcomeStockMovement()
        val stockMovementItem = StockMovementItem()
        stockMovementItem.item = item1
        stockMovementItem.amount = BigDecimal.ONE
        originMovement.items.add(stockMovementItem)
        Mockito.`when`(outcomeStockMovementRepository!!.findById(1L)).thenReturn(Optional.of(originMovement))
        val movement = OutcomeStockMovementDto()
        val stockMovementItemDto = StockMovementItemDto()
        stockMovementItemDto.item = itemDto1
        stockMovementItemDto.amount = BigDecimal.valueOf(2)
        stockMovementItemDto.price = originStockItem1!!.price
        movement.items = arrayListOf(stockMovementItemDto)
        movement.stock = stock
        movement.id = 1L
        service!!.validateItems(movement)
    }

    @Test
    fun validItems_updateAmountGreaterThanSum_validationPass() {
        val originMovement = OutcomeStockMovement()
        val stockMovementItem = StockMovementItem()
        stockMovementItem.item = item1
        stockMovementItem.amount = BigDecimal.ONE
        originMovement.items.add(stockMovementItem)
        Mockito.`when`(outcomeStockMovementRepository!!.findById(1L)).thenReturn(Optional.of(originMovement))
        val movement = OutcomeStockMovementDto()
        val stockMovementItemDto = StockMovementItemDto()
        stockMovementItemDto.item = itemDto1
        stockMovementItemDto.amount = BigDecimal.valueOf(3)
        stockMovementItemDto.price = originStockItem1!!.price
        movement.items = arrayListOf(stockMovementItemDto)
        movement.stock = stock
        movement.id = 1L
        Assertions.assertThatExceptionOfType(ServiceRuntimeException::class.java)
                .isThrownBy { service!!.validateItems(movement) }
                .withMessage("stock.movement.outcome.invalid.amount")
    }

    companion object {
        private const val ITEM_1_ID = 5L
        private const val ITEM_2_ID = 6L
        private const val STOCK_ID = 10L
    }
}