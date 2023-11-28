import { Module } from 'vuex'
import actions from '@/modules/common/store/entityActions'
import mutations from '@/modules/common/store/entityMutations'
import getters from '@/modules/common/store/entityGetters'
import RootState from '@/store/rootState'
import EntityState from '@/modules/common/store/entityState'
import EntityService from '@/modules/common/services/entityService'
import BankTxImport from '@/modules/bank/transaction/import/domain/bankTxImport'
import BankTxImportResource from '@/modules/bank/transaction/import/services/bankTxImportResource'

const entityService = new EntityService<BankTxImport>(BankTxImportResource, BankTxImport)
const configurationModule: Module<EntityState<BankTxImport>, RootState> = {
  namespaced: true,
  state: new EntityState(),
  mutations,
  getters,
  actions: actions(entityService, 'bank-tx-import')
}

export default configurationModule
