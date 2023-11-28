package cz.smartbrains.qesu.module.stock.repository

import cz.smartbrains.qesu.module.common.repository.OrderableEntityRepository
import cz.smartbrains.qesu.module.stock.entity.Stock
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface StockRepository : OrderableEntityRepository<Stock> {
    @Modifying
    @Query("UPDATE Stock s SET s.defaultForDispatch=false where s.companyBranch.id=:companyBranchId")
    fun disableDefaultForDispatch(@Param("companyBranchId") companyBranchId: Long)
}