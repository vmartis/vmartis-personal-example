import { HttpResponse } from 'vue-resource/types/vue_resource'
import { Store } from 'vuex'
import { VueRouter } from 'vue-router/types/router'

export default (store: Store<any>, router: VueRouter) => {
  return () => {
    // do logout action if 401 is returned
    return function (response: HttpResponse) {
      if (response.status === 401) {
        router.push({ name: 'logout' }).catch(reason => { })
      }
    }
  }
}
