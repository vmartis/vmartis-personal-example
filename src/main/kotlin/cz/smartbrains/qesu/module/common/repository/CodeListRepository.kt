package cz.smartbrains.qesu.module.common.repository

import cz.smartbrains.qesu.module.common.entity.CodeList
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface CodeListRepository<E : CodeList> : OrderableEntityRepository<E>