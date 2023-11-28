package cz.smartbrains.qesu.module.common.mapper

import cz.smartbrains.qesu.module.common.service.Messages
import cz.smartbrains.qesu.module.region.mapper.RegionMapper
import cz.smartbrains.qesu.module.common.dto.AddressDto
import cz.smartbrains.qesu.module.common.dto.AddressRDto
import cz.smartbrains.qesu.module.common.entity.Address
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.springframework.beans.factory.annotation.Autowired

@Mapper(componentModel = "spring", uses = [CodeListMapper::class, RegionMapper::class])
abstract class AddressMapper {
    @Autowired
    protected var messages: Messages? = null
    abstract fun dtoToDo(address: AddressDto?): Address?
    abstract fun doToDto(address: Address?): AddressDto?
    @Mapping(target = "country", expression = "java(this.getMessages().getMessage(\"country.\" + address.getCountry()))")
    abstract fun doToRDto(address: Address?): AddressRDto?
}