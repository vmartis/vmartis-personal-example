package cz.smartbrains.qesu.module.item.repository

import cz.smartbrains.qesu.module.item.entity.ItemBatch
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface ItemBatchRepository : JpaRepository<ItemBatch, Long>, ItemBatchRepositoryCustom {
    @Query("select ib from ItemBatch ib where ib.name=:name and ib.item.id=:itemId and ib.stock.id=:stockId")
    fun find(@Param("stockId") stockId: Long, @Param("itemId") itemId: Long, @Param("name") name: String): Optional<ItemBatch>

    @Modifying
    @Query("update ItemBatch ib set ib.active=false where ib.stock.id=:stockId and ib.item.id=:itemId")
    fun deactivateAllForItem(@Param("stockId") stockId: Long, @Param("itemId") itemId: Long): Int

    @Modifying
    @Query("update ItemBatch ib set ib.active=false where ib.stock.id=:stockId and ib.item.id=:itemId and ib.name not in :exceptNames")
    fun deactivateAllForItem(@Param("stockId") stockId: Long, @Param("itemId") itemId: Long, @Param("exceptNames") exceptNames: Collection<String>): Int
}