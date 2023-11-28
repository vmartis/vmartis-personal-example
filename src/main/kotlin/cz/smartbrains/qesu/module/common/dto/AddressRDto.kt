package cz.smartbrains.qesu.module.common.dto

import lombok.ToString

@ToString(callSuper = true)
class AddressRDto {
    var streetName: String? = null
    var houseNumber: String? = null
    var city: String? = null
    var zipCode: String? = null
    var country: String? = null
}