import actions from './appActions'
import mutations from './appMutations'
import AppState from '@/modules/app/store/AppState'

export default {
  namespaced: true,
  state: new AppState(),
  mutations: mutations,
  actions: actions
}
