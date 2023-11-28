import Company from '@/modules/company/domain/company'

export default class CompanyState {
  partners: Array<Company> = []
  owned?: Company | null = null
  selected?: Company | null = null
}
