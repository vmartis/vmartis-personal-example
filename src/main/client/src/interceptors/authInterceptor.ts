import { Store } from 'vuex'
import { HttpOptions } from 'vue-resource/types/vue_resource'

export default (store: Store<any>) => {
  return (request: HttpOptions) => {
    const auth = store.state.auth
    const hasAuthHeader = request.headers.has('Authorization')

    if (!hasAuthHeader && auth.accessToken && auth.validTo && auth.validTo.isAfter()) {
      request.headers.set('Authorization', 'Bearer ' + auth.accessToken)
    }
  }
}
