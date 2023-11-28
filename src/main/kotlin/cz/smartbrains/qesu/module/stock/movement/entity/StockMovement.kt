package cz.smartbrains.qesu.module.stock.movement.entity

import cz.smartbrains.qesu.module.user.entity.User
import cz.smartbrains.qesu.module.common.entity.AbstractEntity
import cz.smartbrains.qesu.module.common.entity.AuditableEntity
import cz.smartbrains.qesu.module.inventory.entity.Inventory
import cz.smartbrains.qesu.module.stock.entity.Stock
import cz.smartbrains.qesu.module.stock.movement.type.StockMovementType
import lombok.EqualsAndHashCode
import lombok.ToString
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@EqualsAndHashCode(callSuper = false)
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name = "STOCK_MOVEMENT", indexes = [Index(name = "STOCK_MOVEMENT_DATE_IDX", columnList = "DATE"), Index(name = "STOCK_MOVEMENT_CREATED_IDX", columnList = "CREATED"), Index(name = "STOCK_MOVEMENT_NUMBER_TYPE_STOCK_IDX", columnList = "NUMBER,TYPE,STOCK_ID"), Index(name = "STOCK_MOVEMENT_DATE_STOCK_IDX", columnList = "DATE,STOCK_ID")])
abstract class StockMovement(// important only for STOCK_MOVEMENT_NUMBER_TYPE_STOCK_IDX constraint
        mainType: StockMovementType) : AbstractEntity(), AuditableEntity {

    @Column(name = "TYPE", nullable = false, length = 7) @Enumerated(EnumType.STRING)
    open var mainType = mainType

    @Column(name = "NUMBER", nullable = false)
    open var number: BigInteger? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "STOCK_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "STOCK_MOVEMENT_STOCK_FK"), nullable = false)
    open var stock: Stock? = null

    @Column(name = "DATE", nullable = false)
    open var date: LocalDateTime? = null

    @Column(name = "NOTE", length = 300)
    open var note: String? = null

    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "movement")
    @OrderBy("ID ASC")
    open var items: MutableList<StockMovementItem> = ArrayList()

    @Column(name = "TOTAL_PRICE", precision = 10, scale = 2, nullable = false)
    open var totalPrice: BigDecimal? = null

    @ManyToOne
    @JoinColumn(name = "INVENTORY_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "STOCK_MOVEMENT_INVENTORY_FK"))
    open var inventory: Inventory? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "CREATED_BY_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "STOCK_MOVEMENT_CREATED_BY_FK"), nullable = false)
    override var createdBy: User? = null

    @ManyToOne
    @JoinColumn(name = "UPDATED_BY_ID", columnDefinition = "bigint", foreignKey = ForeignKey(name = "STOCK_MOVEMENT_UPDATED_BY_FK"))
    override var updatedBy: User? = null

    override fun toString(): String {
        return "StockMovement(mainType=$mainType, number=$number, stock=${stock?.id}, date=$date, note=$note, items=$items, totalPrice=$totalPrice, inventory=$inventory, createdBy=${createdBy?.id}, updatedBy=${updatedBy?.id})"
    }


}