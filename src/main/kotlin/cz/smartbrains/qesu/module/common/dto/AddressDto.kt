package cz.smartbrains.qesu.module.common.dto

import lombok.ToString
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotNull

@ToString
class AddressDto {
    var streetName: @NotNull @Length(min = 1, max = 50) String? = null
    var houseNumber: @NotNull @Length(min = 1, max = 10) String? = null
    var city: @NotNull @Length(min = 1, max = 50) String? = null
    var zipCode: @NotNull @Length(min = 1, max = 10) String? = null
    var country: @NotNull @Length(min = 3, max = 3) String? = null
    var region: ColorableCodeListDto? = null
}