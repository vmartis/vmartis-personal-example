import actions from '@/modules/common/store/entityActions'
import Document from '@/modules/document/domain/document'
import documentResource from '@/modules/document/services/documentResource'
import EntityService from '@/modules/common/services/entityService'
import { Module } from 'vuex'
import Order from '@/modules/order/domain/order'
import RootState from '@/store/rootState'
import EntityState from '@/modules/common/store/entityState'

const entityService = new EntityService<Document>(documentResource, Order)
const contactModule: Module<EntityState<Document>, RootState> = {
  namespaced: true,
  state: new EntityState(),
  actions: actions(entityService, 'document')
}

export default contactModule
