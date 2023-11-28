package cz.smartbrains.qesu.module.common.mapper

import cz.smartbrains.qesu.module.common.entity.AbstractEntity

interface EntityProvider<DomainT : AbstractEntity> {

    fun createEntity(): DomainT
}