package cz.smartbrains.qesu.module.common.repository

import cz.smartbrains.qesu.module.common.entity.Orderable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface OrderableEntityRepository<T : Orderable> : JpaRepository<T, Long> {
    fun findByOrderByOrderAsc(): List<T>

    @Modifying(flushAutomatically = true)
    @Query("UPDATE #{#entityName} as entity SET entity.order = entity.order+1 WHERE entity.order >= :start AND entity.order <= :end")
    fun incrementOrderRange(start: Int, end: Int): Int

    @Modifying(flushAutomatically = true)
    @Query("UPDATE #{#entityName} as entity SET entity.order = entity.order-1 WHERE entity.order >= :start AND entity.order <= :end")
    fun decrementOrderRange(start: Int, end: Int): Int
}