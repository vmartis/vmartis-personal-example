package cz.smartbrains.qesu.module.common.dto

import cz.smartbrains.qesu.module.common.dto.validation.OnCreate
import cz.smartbrains.qesu.module.common.dto.validation.OnUpdate
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Null

open class AbstractDto : EntityDto {
    override var id: @Null(groups = [OnCreate::class]) @NotNull(groups = [OnUpdate::class]) Long? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val that = other as AbstractDto
        return id == that.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }
}