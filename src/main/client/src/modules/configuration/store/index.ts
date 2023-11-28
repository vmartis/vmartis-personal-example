import { Module } from 'vuex'
import actions from '@/modules/common/store/entityActions'
import mutations from '@/modules/common/store/entityMutations'
import getters from '@/modules/common/store/entityGetters'
import RootState from '@/store/rootState'
import EntityState from '@/modules/common/store/entityState'
import EntityService from '@/modules/common/services/entityService'
import ConfigurationResource from '@/modules/configuration/services/configurationResource'
import Configuration from '@/modules/configuration/domain/configuration'

const entityService = new EntityService<Configuration>(ConfigurationResource, Configuration)
const configurationModule: Module<EntityState<Configuration>, RootState> = {
  namespaced: true,
  state: new EntityState(),
  mutations,
  getters,
  actions: actions(entityService, 'configuration')
}

export default configurationModule
