import { Module } from 'vuex'
import actions from '@/modules/common/store/entityActions'
import DeliveryRoute from '@/modules/delivery/route/domain/deliveryRoute'
import deliveryRouteResource from '@/modules/delivery/route/services/deliveryRouteResource'
import EntityService from '@/modules/common/services/entityService'
import EntityState from '@/modules/common/store/entityState'
import getters from '@/modules/common/store/entityGetters'
import mutations from '@/modules/common/store/entityMutations'
import RootState from '@/store/rootState'

const regionModule: Module<EntityState<DeliveryRoute>, RootState> = {
  namespaced: true,
  state: new EntityState<DeliveryRoute>(),
  mutations,
  getters,
  actions: actions(new EntityService<DeliveryRoute>(deliveryRouteResource, DeliveryRoute), 'delivery-route')
}

export default regionModule
