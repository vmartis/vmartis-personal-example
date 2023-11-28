import { Route } from 'vue-router'
import { Store } from 'vuex'

// resets document title
export default (store: Store<any>) => {
  return (to: Route, from: Route) => {
    store.dispatch('app/updatePageGroup', to.meta.pageGroup)
  }
}
