import { Module } from 'vuex'
import actions from '@/modules/company/store/companyActions'
import mutations from '@/modules/company/store/companyMutations'
import getters from '@/modules/company/store/companyGetters'
import CompanyState from '@/modules/company/store/companyState'
import RootState from '@/store/rootState'

const companyModule: Module<CompanyState, RootState> = {
  namespaced: true,
  state: new CompanyState(),
  mutations,
  getters,
  actions
}

export default companyModule
