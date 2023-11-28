package cz.smartbrains.qesu.module.common.entity

import cz.smartbrains.qesu.module.user.entity.User

interface AuditableEntity {
    val createdBy: User?
    val updatedBy: User?
}