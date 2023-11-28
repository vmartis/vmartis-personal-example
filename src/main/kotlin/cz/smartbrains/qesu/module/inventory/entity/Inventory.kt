package cz.smartbrains.qesu.module.inventory.entity

import cz.smartbrains.qesu.module.common.entity.AbstractEntity
import cz.smartbrains.qesu.module.common.entity.AuditableEntity
import cz.smartbrains.qesu.module.stock.entity.Stock
import cz.smartbrains.qesu.module.stock.movement.entity.StockMovement
import cz.smartbrains.qesu.module.user.entity.User
import lombok.EqualsAndHashCode
import lombok.ToString
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import java.math.BigInteger
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true, exclude = ["stock", "items", "movements", "createdBy", "updatedBy"])
@Entity
@Table(name = "INVENTORY", indexes = [Index(name = "INVENTORY_DATE_IDX", columnList = "DATE")], uniqueConstraints = [UniqueConstraint(name = "INVENTORY_NUMBER_STOCK_UC", columnNames = ["NUMBER", "STOCK_ID"])])
class Inventory : AbstractEntity(), AuditableEntity {
    @Column(name = "NUMBER", nullable = false)
    var number: BigInteger? = null

    @Column(name = "DATE", nullable = false)
    var date: LocalDateTime? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "STOCK_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "INVENTORY_STOCK_FK"), nullable = false)
    var stock: Stock? = null

    @Column(name = "NOTE", length = 300)
    var note: String? = null

    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "inventory")
    @OrderBy("ID ASC")
    var items: MutableList<InventoryItem> = ArrayList()

    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "inventory")
    var movements: MutableList<StockMovement> = ArrayList()

    @ManyToOne(optional = false)
    @JoinColumn(name = "CREATED_BY_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "INVENTORY_CREATED_BY_FK"), nullable = false)
    override var createdBy: User? = null

    @ManyToOne
    @JoinColumn(name = "UPDATED_BY_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "INVENTORY_UPDATED_BY_FK"))
    override var updatedBy: User? = null

    val formattedNumber: String?
        get(): String? {
            return if (this.number == null) null else "SI-$number"
        }
}