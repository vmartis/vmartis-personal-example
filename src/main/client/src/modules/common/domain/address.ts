import _ from 'lodash'
import countries from '@/modules/common/values/countries'
import ColorableCodeList from '@/modules/common/domain/colorableCodeList'

class Address {
  streetName?: string
  houseNumber?: string
  city?: string
  zipCode?: string
  country?: string
  region?: ColorableCodeList

  constructor (data: any = {}) {
    this.streetName = data.streetName
    this.houseNumber = data.houseNumber
    this.city = data.city
    this.zipCode = data.zipCode
    this.country = data.country
    if (data.region) {
      this.region = new ColorableCodeList(data.region)
    }
  }

  format (lined: boolean = false, includeZipCode = true, includeCountry = true) {
    const result = []
    if (this.streetName && this.houseNumber) {
      result.push(this.streetName)
      result.push(this.houseNumber + ',')
    } else if (this.streetName) {
      result.push(this.streetName + ',')
    }
    if (lined && result.length > 0) {
      result[result.length - 1] = result[result.length - 1] + '\n'
    }
    if (this.zipCode && includeZipCode) {
      result.push(this.zipCode)
    }
    if (this.city) {
      result.push(includeCountry ? (this.city + ',') : this.city)
    }
    if (this.country && includeCountry) {
      result.push(_.find(countries, { value: this.country })!.label)
    }
    return result.join(' ')
  }
}

export default Address
