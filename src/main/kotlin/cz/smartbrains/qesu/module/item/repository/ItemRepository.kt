package cz.smartbrains.qesu.module.item.repository

import cz.smartbrains.qesu.module.item.entity.Item
import org.springframework.data.jpa.repository.JpaRepository

interface ItemRepository : JpaRepository<Item, Long>, ItemRepositoryCustom