import { Module } from 'vuex'
import actions from '@/modules/common/store/entityActions'
import mutations from '@/modules/common/store/entityMutations'
import getters from '@/modules/common/store/entityGetters'
import RootState from '@/store/rootState'
import EntityState from '@/modules/common/store/entityState'
import EntityService from '@/modules/common/services/entityService'
import CompanyBranchResource from '@/modules/company/services/companyBranchResource'
import CompanyBranch from '@/modules/company/domain/companyBranch'

const entityService = new EntityService<CompanyBranch>(CompanyBranchResource, CompanyBranch)
const companyModule: Module<EntityState<CompanyBranch>, RootState> = {
  namespaced: true,
  state: new EntityState(),
  mutations,
  getters,
  actions: actions(entityService, 'company-branch')
}

export default companyModule
