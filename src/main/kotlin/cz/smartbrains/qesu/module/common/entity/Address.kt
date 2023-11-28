package cz.smartbrains.qesu.module.common.entity

import cz.smartbrains.qesu.module.region.entity.Region
import lombok.ToString
import javax.persistence.Embeddable
import javax.persistence.FetchType
import javax.persistence.ManyToOne

@ToString
@Embeddable
class Address {
    var streetName: String? = null
    var houseNumber: String? = null
    var city: String? = null
    var zipCode: String? = null

    @ManyToOne(fetch = FetchType.EAGER)
    var region: Region? = null
    var country: String? = null
}