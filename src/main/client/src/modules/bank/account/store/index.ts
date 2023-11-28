import { Module } from 'vuex'
import actions from '@/modules/common/store/entityActions'
import mutations from '@/modules/common/store/entityMutations'
import getters from '@/modules/common/store/entityGetters'
import RootState from '@/store/rootState'
import EntityState from '@/modules/common/store/entityState'
import EntityService from '@/modules/common/services/entityService'
import BankAccountResource from '@/modules/bank/account/services/bankAccountResource'
import BankAccount from '@/modules/bank/account/domain/bankAccount'

const entityService = new EntityService<BankAccount>(BankAccountResource, BankAccount)
const configurationModule: Module<EntityState<BankAccount>, RootState> = {
  namespaced: true,
  state: new EntityState(),
  mutations,
  getters,
  actions: actions(entityService, 'bank-account')
}

export default configurationModule
