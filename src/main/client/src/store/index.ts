import * as modules from './storeModules'
import plugins from './plugins'
import RootState from '@/store/rootState'
import Vuex from 'vuex'

const store = new Vuex.Store<RootState>({
  modules: modules,
  plugins: plugins
})

export default store
