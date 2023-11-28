import authHook from './routerAuthHook'
import routerTitleHook from './routerTitleHook'
import routerPageGroupHook from './routerPageGroupHook'
import { Store } from 'vuex'
import { VueRouter } from 'vue-router/types/router'

export default (store: Store<any>, router: VueRouter) => {
  router.beforeEach(authHook(store))
  router.beforeEach(routerTitleHook)
  router.afterEach(routerPageGroupHook(store))
}
