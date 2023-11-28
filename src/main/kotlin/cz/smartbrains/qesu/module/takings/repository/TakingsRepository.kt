package cz.smartbrains.qesu.module.takings.repository

import cz.smartbrains.qesu.module.takings.entity.Takings
import org.springframework.data.jpa.repository.JpaRepository

interface TakingsRepository : JpaRepository<Takings, Long>, TakingsRepositoryCustom