import _ from 'lodash'
import Address from '@/modules/common/domain/address'
import CompanyBranch from '@/modules/company/domain/companyBranch'
import { CompanyRole } from '@/modules/company/type/companyRole'
import CodeList from '@/modules/common/domain/codeList'
import Subject from '@/modules/subject/domain/subject'
import SaleSettings from '@/modules/company/domain/saleSettings'
import EntityMap from '@/modules/common/domain/entityMap'

class Company extends Subject {
  name?: string
  role?: CompanyRole
  companyId?: string
  vatId: string | null
  vatIdLocal?: string
  defaultCurrency?: string
  web?: string
  branches: Array<CompanyBranch> = []
  branchesMap: EntityMap<CompanyBranch>
  saleSettings?: SaleSettings
  forOrder?: boolean

  constructor (data: any) {
    super(data, 'name')
    this.branches = data.branches ? data.branches.map((branch: any) => new CompanyBranch(branch)) : []
    this.branchesMap = _.keyBy(this.branches, 'id')
    this.role = data.role ? (<any>CompanyRole)[data.role] : undefined
    this.address = data.address ? new Address(data.address) : undefined
    this.categories = data.categories ? data.categories.map((category: any) => new CodeList(category)) : []
    this.saleSettings = data.saleSettings ? new SaleSettings(data.saleSettings) : new SaleSettings({})
    this.vatId = data.vatId || null

    if (!this['@type']) {
      this['@type'] = 'company'
    }

    this.relations('saleSettings')

    this.extendSearchString()
  }

  get orderStatementEnabled () {
    return this.saleSettings && this.saleSettings.paymentType && this.saleSettings.paymentType.statementEnabled
  }

  get deliverNoteEnabled () {
    return this.saleSettings && this.saleSettings.deliveryNoteEnabled
  }

  extendSearchString () {
    this.searchString = this.name || ''
    if (this.address) {
      this.searchString += ' ' + this.address.format()
      if (this.address.region) {
        this.searchString += ' ' + this.address.region.label
      }
    }
    if (this.categories) {
      this.categories.forEach((category: CodeList) => {
        this.searchString += ' ' + category.label
      })
    }
  }
}

export default Company
