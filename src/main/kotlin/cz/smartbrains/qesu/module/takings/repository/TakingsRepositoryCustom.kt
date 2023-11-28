package cz.smartbrains.qesu.module.takings.repository

import cz.smartbrains.qesu.module.takings.dto.TakingsFilter
import cz.smartbrains.qesu.module.takings.entity.Takings

interface TakingsRepositoryCustom {
    fun findByFilter(filter: TakingsFilter): List<Takings>
}