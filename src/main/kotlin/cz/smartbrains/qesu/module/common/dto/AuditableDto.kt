package cz.smartbrains.qesu.module.common.dto

import cz.smartbrains.qesu.module.user.dto.UserDto
import java.time.LocalDateTime

interface AuditableDto {
    val created: LocalDateTime?
    val createdBy: UserDto?
    val updated: LocalDateTime?
    val updatedBy: UserDto?
}