package cz.smartbrains.qesu.module.common.entity

import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@MappedSuperclass
open class AbstractEntity protected constructor() {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "serial")
    @Id
    open var id: Long? = null

    @Column(name = "CREATED", nullable = false)
    open var created: LocalDateTime? = null

    @Column(name = "UPDATED")
    open var updated: LocalDateTime? = null
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val that = other as AbstractEntity
        return id == that.id
    }

    @PrePersist
    private fun prePersist() {
        created = LocalDateTime.now()
    }

    @PreUpdate
    private fun preUpdate() {
        updated = LocalDateTime.now()
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }

    init {
        created = LocalDateTime.now()
    }
}