package cz.smartbrains.qesu.module.item.repository

import cz.smartbrains.qesu.module.item.entity.ItemDispatch
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ItemDispatchRepository : JpaRepository<ItemDispatch, Long>, ItemDispatchRepositoryCustom {
    @Query("select count(id) from ItemDispatch id where id.item.id=:itemId")
    fun countForItem(@Param("itemId") itemId: Long): Int

    @Query("select count(id) from ItemDispatch id where id.subItem.id=:subItemId")
    fun countForSubItem(@Param("subItemId") subItemId: Long): Int
}