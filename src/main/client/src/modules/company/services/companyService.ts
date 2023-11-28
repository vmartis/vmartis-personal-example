import companyResource from './companyResource'
import _ from 'lodash'
import Company from '../domain/company'
import { CompanyRole } from '@/modules/company/type/companyRole'

export default {
  async find (id: string) {
    const response = await companyResource.get({ id })
    if (response.ok) {
      return new Company(response.data)
    } else {
      return null
    }
  },
  async findAll (role: CompanyRole) {
    const response = await companyResource.query({ role: CompanyRole[role] })
    if (response.ok) {
      return response.data.map((companyData: any) => new Company(companyData))
    } else {
      return null
    }
  },
  async create (company: Company) {
    const response = await companyResource.save({}, _.pickBy(company))
    if (response.ok) {
      return new Company(response.data)
    } else {
      return null
    }
  },
  async update (company: Company) {
    const response = await companyResource.update({}, _.pickBy(company))
    if (response.ok) {
      return new Company(response.data)
    } else {
      return null
    }
  }
}
