import { Module } from 'vuex'
import actions from '@/modules/common/store/entityActions'
import Currency from '@/modules/currency/domain/currency'
import currencyResource from '@/modules/currency/services/currencyResource'
import EntityState from '@/modules/common/store/entityState'
import EntityService from '@/modules/common/services/entityService'
import getters from '@/modules/common/store/codeListGetters'
import RootState from '@/store/rootState'
import mutations from '@/modules/common/store/entityMutations'

const contactModule: Module<EntityState<Currency>, RootState> = {
  namespaced: true,
  state: new EntityState(),
  mutations,
  getters,
  actions: actions(new EntityService(currencyResource, Currency), 'currency')
}

export default contactModule
