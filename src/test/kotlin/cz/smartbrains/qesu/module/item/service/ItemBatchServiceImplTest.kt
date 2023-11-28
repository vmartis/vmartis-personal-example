package cz.smartbrains.qesu.module.item.service

import cz.smartbrains.qesu.module.inventory.dto.InventoryDto
import cz.smartbrains.qesu.module.inventory.dto.InventoryItemDto
import cz.smartbrains.qesu.module.item.dto.ItemDto
import cz.smartbrains.qesu.module.item.entity.Item
import cz.smartbrains.qesu.module.item.entity.ItemBatch
import cz.smartbrains.qesu.module.item.mapper.ItemBatchMapper
import cz.smartbrains.qesu.module.item.repository.ItemBatchRepository
import cz.smartbrains.qesu.module.item.repository.ItemRepository
import cz.smartbrains.qesu.module.stock.dto.StockDto
import cz.smartbrains.qesu.module.stock.entity.Stock
import cz.smartbrains.qesu.module.stock.movement.dto.IncomeStockMovementDto
import cz.smartbrains.qesu.module.stock.movement.dto.StockMovementItemDto
import cz.smartbrains.qesu.module.stock.repository.StockRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.*
import org.mockito.Mockito.lenient
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class ItemBatchServiceImplTest {
    @Mock
    private val mapper: ItemBatchMapper? = null

    @Mock
    private val itemBatchRepository: ItemBatchRepository? = null

    @Mock
    private val itemRepository: ItemRepository? = null

    @Mock
    private val stockRepository: StockRepository? = null

    @Captor
    private val itemBatchCaptor: ArgumentCaptor<ItemBatch>? = null
    private var service: ItemBatchService? = null
    private val item1Dto = ItemDto()
    private val item2Dto = ItemDto()
    private val item1 = Item()
    private val item2 = Item()
    private val stockDto = StockDto()
    private val stock = Stock()

    @BeforeEach
    fun setUp() {
        service = ItemBatchServiceImpl(mapper!!, itemBatchRepository!!, itemRepository!!, stockRepository!!)
        item1Dto.id = ITEM_1_ID
        item2Dto.id = ITEM_2_ID
        item1.id = ITEM_1_ID
        item2.id = ITEM_2_ID
        stockDto.id = STOCK_ID
        stock.id = STOCK_ID
        lenient().`when`(itemRepository.findById(ITEM_1_ID)).thenReturn(Optional.of(item1))
        lenient().`when`(itemRepository.findById(ITEM_2_ID)).thenReturn(Optional.of(item2))
        lenient().`when`(itemRepository.getById(ITEM_1_ID)).thenReturn(item1)
        lenient().`when`(itemRepository.getById(ITEM_2_ID)).thenReturn(item2)
        lenient().`when`(stockRepository.getById(STOCK_ID)).thenReturn(stock)
    }

    @Test
    fun createFromStockMovement_noBatchEvidence_noChangeInDb() {
        val movement = IncomeStockMovementDto()
        val movementItem = StockMovementItemDto()
        movementItem.item = item1Dto
        movementItem.itemBatches.add("0101/01")
        movement.items!!.add(movementItem)
        item1.batchEvidence = false
        service!!.createFromStockMovement(movement)
        Mockito.verify(itemRepository)!!.findById(ITEM_1_ID)
        Mockito.verify(itemBatchRepository, Mockito.never())!!.find(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong(), ArgumentMatchers.anyString())
        Mockito.verify(itemBatchRepository, Mockito.never())!!.saveAndFlush(ArgumentMatchers.any())
    }

    @Test
    fun createFromStockMovement_withNewBatches_newBatchesAreCreated() {
        val batch1 = "0101/01"
        val batch2 = "0202/02"
        val movement = IncomeStockMovementDto()
        movement.stock = stockDto
        val movementItem = StockMovementItemDto()
        movementItem.item = item1Dto
        movementItem.itemBatches.add(batch1)
        movement.items!!.add(movementItem)
        val movementItem2 = StockMovementItemDto()
        movementItem2.itemBatches.add(batch2)
        movementItem2.item = item2Dto
        movement.items!!.add(movementItem2)
        item1.batchEvidence = true
        item2.batchEvidence = true
        service!!.createFromStockMovement(movement)
        Mockito.verify(itemRepository)!!.findById(ITEM_1_ID)
        Mockito.verify(itemRepository)!!.findById(ITEM_2_ID)
        Mockito.verify(itemBatchRepository)!!.find(STOCK_ID, ITEM_1_ID, batch1)
        Mockito.verify(itemBatchRepository)!!.find(STOCK_ID, ITEM_2_ID, batch2)
        Mockito.verify(itemBatchRepository, Mockito.times(2))!!.saveAndFlush(itemBatchCaptor!!.capture())
        val newItemBatches = itemBatchCaptor.allValues
        Assertions.assertThat(newItemBatches).hasSize(2)
        Assertions.assertThat(newItemBatches[0].item!!.id).isEqualTo(ITEM_1_ID)
        Assertions.assertThat(newItemBatches[0].stock!!.id).isEqualTo(STOCK_ID)
        Assertions.assertThat(newItemBatches[0].name).isEqualTo(batch1)
        Assertions.assertThat(newItemBatches[0].active).isTrue()
        Assertions.assertThat(newItemBatches[1].item!!.id).isEqualTo(ITEM_2_ID)
        Assertions.assertThat(newItemBatches[1].stock!!.id).isEqualTo(STOCK_ID)
        Assertions.assertThat(newItemBatches[1].name).isEqualTo(batch2)
        Assertions.assertThat(newItemBatches[1].active).isTrue()
    }

    @Test
    fun createFromStockMovement_withExistingBatches_batchesAreActivated() {
        val batch1 = "0101/01"
        val batch2 = "0202/02"
        val movement = IncomeStockMovementDto()
        movement.stock = stockDto
        val movementItem = StockMovementItemDto()
        movementItem.item = item1Dto
        movementItem.itemBatches.add(batch1)
        movement.items!!.add(movementItem)
        val movementItem2 = StockMovementItemDto()
        movementItem2.itemBatches.add(batch2)
        movementItem2.item = item2Dto
        movement.items!!.add(movementItem2)
        item1.batchEvidence = true
        item2.batchEvidence = true
        val itemBatch1 = ItemBatch()
        itemBatch1.active = false
        val itemBatch2 = ItemBatch()
        itemBatch2.active = false
        Mockito.`when`(itemBatchRepository!!.find(STOCK_ID, ITEM_1_ID, batch1)).thenReturn(Optional.of(itemBatch1))
        Mockito.`when`(itemBatchRepository.find(STOCK_ID, ITEM_2_ID, batch2)).thenReturn(Optional.of(itemBatch2))
        service!!.createFromStockMovement(movement)
        Mockito.verify(itemRepository)!!.findById(ITEM_1_ID)
        Mockito.verify(itemRepository)!!.findById(ITEM_2_ID)
        Mockito.verify(itemBatchRepository).find(STOCK_ID, ITEM_1_ID, batch1)
        Mockito.verify(itemBatchRepository).find(STOCK_ID, ITEM_2_ID, batch2)
        Mockito.verify(itemBatchRepository, Mockito.times(2)).saveAndFlush(itemBatchCaptor!!.capture())
        val newItemBatches = itemBatchCaptor.allValues
        Assertions.assertThat(newItemBatches).hasSize(2)
        Assertions.assertThat(newItemBatches[0]).isSameAs(itemBatch1)
        Assertions.assertThat(newItemBatches[0].active).isTrue()
        Assertions.assertThat(newItemBatches[1]).isSameAs(itemBatch2)
        Assertions.assertThat(newItemBatches[1].active).isTrue()
    }

    @Test
    fun updateForInventory_noBatchEvidenceEnabled_noChangeInDb() {
        val inventoryDto = InventoryDto()
        val inventoryItemDto = InventoryItemDto()
        inventoryItemDto.item = item1Dto
        inventoryItemDto.itemBatches.add("0101/01")
        inventoryDto.items!!.add(inventoryItemDto)
        item1.batchEvidence = false
        service!!.updateForInventory(inventoryDto)
        Mockito.verify(itemRepository)!!.findById(ITEM_1_ID)
        Mockito.verify(itemBatchRepository, Mockito.never())!!.deactivateAllForItem(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong())
        Mockito.verify(itemBatchRepository, Mockito.never())!!.find(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong(), ArgumentMatchers.anyString())
        Mockito.verify(itemBatchRepository, Mockito.never())!!.saveAndFlush(ArgumentMatchers.any())
    }

    @Test
    fun updateForInventory_emptyBatches_allBatchesAreDeactivated() {
        val inventoryDto = InventoryDto()
        val inventoryItemDto = InventoryItemDto()
        inventoryItemDto.item = item1Dto
        inventoryDto.stock = stockDto
        inventoryDto.items!!.add(inventoryItemDto)
        item1.batchEvidence = true
        service!!.updateForInventory(inventoryDto)
        Mockito.verify(itemRepository)!!.findById(ITEM_1_ID)
        Mockito.verify(itemBatchRepository)!!.deactivateAllForItem(STOCK_ID, ITEM_1_ID)
        Mockito.verify(itemBatchRepository, Mockito.never())!!.find(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong(), ArgumentMatchers.anyString())
        Mockito.verify(itemBatchRepository, Mockito.never())!!.saveAndFlush(ArgumentMatchers.any())
    }

    @Test
    fun updateForInventory_withEmptyBatches_allBatchesAreDeactivated() {
        val inventoryDto = InventoryDto()
        inventoryDto.stock = stockDto
        val inventoryItem = InventoryItemDto()
        inventoryItem.item = item1Dto
        inventoryDto.items!!.add(inventoryItem)
        item1.batchEvidence = true
        service!!.updateForInventory(inventoryDto)
        Mockito.verify(itemRepository)!!.findById(ITEM_1_ID)
        Mockito.verify(itemBatchRepository)!!.deactivateAllForItem(STOCK_ID, ITEM_1_ID)
    }

    @Test
    fun updateForInventory_withNewBatches_newBatchesAreCreated() {
        val batch1 = "0101/01"
        val batch2 = "0202/02"
        val inventoryDto = InventoryDto()
        inventoryDto.stock = stockDto
        val inventoryItemDto = InventoryItemDto()
        inventoryItemDto.item = item1Dto
        inventoryItemDto.itemBatches.add(batch1)
        inventoryDto.items!!.add(inventoryItemDto)
        val inventoryItemDto2 = InventoryItemDto()
        inventoryItemDto2.itemBatches.add(batch2)
        inventoryItemDto2.item = item2Dto
        inventoryDto.items!!.add(inventoryItemDto2)
        item1.batchEvidence = true
        item2.batchEvidence = true
        service!!.updateForInventory(inventoryDto)
        Mockito.verify(itemRepository)!!.findById(ITEM_1_ID)
        Mockito.verify(itemRepository)!!.findById(ITEM_2_ID)
        Mockito.verify(itemBatchRepository)!!.find(STOCK_ID, ITEM_1_ID, batch1)
        Mockito.verify(itemBatchRepository)!!.find(STOCK_ID, ITEM_2_ID, batch2)
        Mockito.verify(itemBatchRepository, Mockito.times(2))!!.saveAndFlush(itemBatchCaptor!!.capture())
        val newItemBatches = itemBatchCaptor.allValues
        Assertions.assertThat(newItemBatches).hasSize(2)
        Assertions.assertThat(newItemBatches[0].item!!.id).isEqualTo(ITEM_1_ID)
        Assertions.assertThat(newItemBatches[0].stock!!.id).isEqualTo(STOCK_ID)
        Assertions.assertThat(newItemBatches[0].name).isEqualTo(batch1)
        Assertions.assertThat(newItemBatches[0].active).isTrue()
        Assertions.assertThat(newItemBatches[1].item!!.id).isEqualTo(ITEM_2_ID)
        Assertions.assertThat(newItemBatches[1].stock!!.id).isEqualTo(STOCK_ID)
        Assertions.assertThat(newItemBatches[1].name).isEqualTo(batch2)
        Assertions.assertThat(newItemBatches[1].active).isTrue()
        Mockito.verify(itemBatchRepository)!!.deactivateAllForItem(STOCK_ID, ITEM_1_ID, inventoryItemDto.itemBatches)
        Mockito.verify(itemBatchRepository)!!.deactivateAllForItem(STOCK_ID, ITEM_2_ID, inventoryItemDto2.itemBatches)
    }

    @Test
    fun updateForInventory_withExistingBatches_batchesAreActivated() {
        val batch1 = "0101/01"
        val batch2 = "0202/02"
        val inventoryDto = InventoryDto()
        inventoryDto.stock = stockDto
        val inventoryItemDto = InventoryItemDto()
        inventoryItemDto.item = item1Dto
        inventoryItemDto.itemBatches.add(batch1)
        inventoryDto.items!!.add(inventoryItemDto)
        val inventoryItemDto2 = InventoryItemDto()
        inventoryItemDto2.itemBatches.add(batch2)
        inventoryItemDto2.item = item2Dto
        inventoryDto.items!!.add(inventoryItemDto2)
        item1.batchEvidence = true
        item2.batchEvidence = true
        val itemBatch1 = ItemBatch()
        itemBatch1.active = false
        val itemBatch2 = ItemBatch()
        itemBatch2.active = false
        Mockito.`when`(itemBatchRepository!!.find(STOCK_ID, ITEM_1_ID, batch1)).thenReturn(Optional.of(itemBatch1))
        Mockito.`when`(itemBatchRepository.find(STOCK_ID, ITEM_2_ID, batch2)).thenReturn(Optional.of(itemBatch2))
        service!!.updateForInventory(inventoryDto)
        Mockito.verify(itemRepository)!!.findById(ITEM_1_ID)
        Mockito.verify(itemRepository)!!.findById(ITEM_2_ID)
        Mockito.verify(itemBatchRepository).find(STOCK_ID, ITEM_1_ID, batch1)
        Mockito.verify(itemBatchRepository).find(STOCK_ID, ITEM_2_ID, batch2)
        Mockito.verify(itemBatchRepository, Mockito.times(2)).saveAndFlush(itemBatchCaptor!!.capture())
        val newItemBatches = itemBatchCaptor.allValues
        Assertions.assertThat(newItemBatches).hasSize(2)
        Assertions.assertThat(newItemBatches[0]).isSameAs(itemBatch1)
        Assertions.assertThat(newItemBatches[0].active).isTrue
        Assertions.assertThat(newItemBatches[1]).isSameAs(itemBatch2)
        Assertions.assertThat(newItemBatches[1].active).isTrue
        Mockito.verify(itemBatchRepository).deactivateAllForItem(STOCK_ID, ITEM_1_ID, inventoryItemDto.itemBatches)
        Mockito.verify(itemBatchRepository).deactivateAllForItem(STOCK_ID, ITEM_2_ID, inventoryItemDto2.itemBatches)
    }

    companion object {
        private const val ITEM_1_ID = 1L
        private const val ITEM_2_ID = 2L
        private const val STOCK_ID = 3L
    }
}