package cz.smartbrains.qesu.module.invoice.mapper

import cz.smartbrains.qesu.module.common.mapper.CodeListMapper
import cz.smartbrains.qesu.module.invoice.dto.VatRateDto
import cz.smartbrains.qesu.module.invoice.entity.VatRate
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface VatRateMapper : CodeListMapper<VatRate, VatRateDto>