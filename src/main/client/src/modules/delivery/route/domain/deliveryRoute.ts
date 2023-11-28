import AuditableEntity from '@/modules/common/domain/auditableEntity'
import Company from '@/modules/company/domain/company'
import CompanyBranch from '@/modules/company/domain/companyBranch'
import { Entity } from '@/modules/common/domain/entity'
import CodeList from '@/modules/common/domain/codeList'

class DeliveryRoute extends Entity implements AuditableEntity {
  company?: Company | null
  companyBranch?: CompanyBranch | null
  region?: CodeList | null

  constructor (data: any) {
    super(data)
    this.company = data.company == null ? null : new Company(data.company)
    this.companyBranch = data.companyBranch == null ? null : new CompanyBranch(data.companyBranch)
    this.region = data.region == null ? null : new CodeList(data.region)
  }

  get active () {
    if (this.companyBranch) {
      return this.companyBranch.active
    } else if (this.company) {
      return this.company.active
    } else {
      return true
    }
  }

  get address () {
    if (this.companyBranch) {
      return this.companyBranch.address
    } else if (this.company) {
      return this.company.address
    } else {
      return null
    }
  }
}

export default DeliveryRoute
