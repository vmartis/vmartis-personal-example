package cz.smartbrains.qesu.module.common.dto

import java.time.LocalDate

interface CodeListDto : EntityDto {
    var label: String?
    var validFrom: LocalDate?
    var validTo: LocalDate?
    var order: Int
}