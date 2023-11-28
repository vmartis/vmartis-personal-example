import { Module } from 'vuex'
import actions from '@/modules/common/store/entityActions'
import mutations from '@/modules/common/store/entityMutations'
import getters from '@/modules/common/store/entityGetters'
import RootState from '@/store/rootState'
import EntityState from '@/modules/common/store/entityState'
import EntityService from '@/modules/common/services/entityService'
import ContactResource from '@/modules/contact/services/contactResource'
import Contact from '@/modules/contact/domain/contact'

const entityService = new EntityService<Contact>(ContactResource, Contact)
const contactModule: Module<EntityState<Contact>, RootState> = {
  namespaced: true,
  state: new EntityState(),
  mutations,
  getters,
  actions: actions(entityService, 'contact')
}

export default contactModule
