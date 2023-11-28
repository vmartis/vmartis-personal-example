import { Entity } from '@/modules/common/domain/entity'
import Address from '@/modules/common/domain/address'
import Company from '@/modules/company/domain/company'

class CompanyBranch extends Entity {
  name?: string
  address?: Address
  company?: Company
  active?: boolean
  forOrder?: boolean

  constructor (data: any) {
    super(data, 'id', 'name')

    this.address = data.address ? new Address(data.address) : undefined
    this.company = data.company ? new Company(data.company) : undefined

    this.extendSearchString()
  }

  extendSearchString () {
    this.searchString = this.name || ''
    if (this.address) {
      this.searchString += ' ' + this.address.format()
      if (this.address.region) {
        this.searchString += ' ' + this.address.region.label
      }
    }
  }
}

export default CompanyBranch
