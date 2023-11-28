import * as types from './companyMutationTypes'
import CompanyState from '@/modules/company/store/companyState'
import { ActionTree } from 'vuex'
import Company from '@/modules/company/domain/company'
import RootState from '@/store/rootState'
import companyService from '@/modules/company/services/companyService'
import { CompanyRole } from '@/modules/company/type/companyRole'
import EntityCreateParams from '@/modules/common/store/entityCreateParams'

const actions: ActionTree<CompanyState, RootState> = {
  async select ({ dispatch, commit }, id) {
    await dispatch('clearSelected')
    await dispatch('app/loadingDataEnable', 'company', { root: true })
    const company = await companyService.find(id)
    await commit(types.SELECT, {
      item: company
    })
    await dispatch('app/loadingDataDisable', 'company', { root: true })
  },
  async getAllPartners ({ state, dispatch, commit }, force?: boolean) {
    if (!state.partners || state.partners.length === 0 || force) {
      dispatch('app/loadingDataEnable', 'partners', { root: true })
      await dispatch('clearAll')
      const companies = await companyService.findAll(CompanyRole.PARTNER)
      await commit(types.PARTNER_LIST, {
        items: companies
      })
      dispatch('app/loadingDataDisable', 'partners', { root: true })
    }
  },
  async getOwned ({ state, dispatch, commit }, force?: boolean) {
    if (!state.owned || force) {
      dispatch('app/loadingDataEnable', 'company', { root: true })
      const companies = await companyService.findAll(CompanyRole.OWNED)
      await commit(types.OWNED_GET, {
        item: companies && companies.length ? companies[0] : null
      })
      dispatch('app/loadingDataDisable', 'company', { root: true })
    }
    return state.owned
  },
  async refresh ({ dispatch, commit }, role: CompanyRole) {
    if (role === CompanyRole.PARTNER) {
      await dispatch('getAllPartners', true)
    } else {
      await dispatch('getOwned', true)
    }
  },
  async refreshSelected ({ state, dispatch }, role: CompanyRole) {
    if (state.selected) {
      await dispatch('select', state.selected.id)
    }
  },
  async create ({ dispatch, commit }, entityCreateParams: EntityCreateParams<Company>) {
    const newCompany = await companyService.create(entityCreateParams.entity as Company)
    if (newCompany) {
      if (entityCreateParams.doFetchAfter) {
        await dispatch('refresh', newCompany.role)
      }
      return newCompany
    } else {
      return false
    }
  },
  async update ({ dispatch, commit }, entityCreateParams: EntityCreateParams<Company>) {
    const updatedCompany = await companyService.update(entityCreateParams.entity as Company)
    if (updatedCompany) {
      if (entityCreateParams.doFetchAfter) {
        await dispatch('refresh', updatedCompany.role)
        await dispatch('refreshSelected')
      }
      return true
    } else {
      return false
    }
  },
  async clearAll ({ commit }) {
    await commit(types.CLEAR_ALL)
  },
  async clearSelected ({ commit }) {
    await commit(types.CLEAR_SELECTED)
  }
}

export default actions
