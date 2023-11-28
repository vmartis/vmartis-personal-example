import notificationService from '@/modules/common/services/notificationService'
import { HttpResponse } from 'vue-resource/types/vue_resource'
import { Store } from 'vuex'
import { VueRouter } from 'vue-router/types/router'

export default (store: Store<any>, router: VueRouter) => {
  return () => {
    // do logout action if 401 is returned
    return function (response: HttpResponse) {
      if (response.status !== 200) {
        // authentication error
        if (response.data && response.data.error === 'invalid_grant') {
          notificationService.error('error.auth.login')
        // validation failure
        } else if (response.status === 400) {
          if (response.data.errors && response.data.errors.length && response.data.errors[0].code) {
            notificationService.error('error.' + response.data.errors[0].code)
          } else if (response.data.message && response.data.args) {
            notificationService.error('error.' + response.data.message, response.data.args)
          } else {
            notificationService.error('error.' + response.data.message)
          }
        } else if (response.status === 401 || response.status === 403) { // unauthorized error
          notificationService.error('error.not.allowed')
        } else if (response.status === 404) { // not found
          notificationService.error('error.data.notFound')
          router.push({ name: 'home' }).catch(reason => {})
        } else if (response.status === 413) { // unauthorized error
          notificationService.error('error.data.payloadTooLarge')
        } else if (response.data && response.data.message) { // general error
          // console.error(VueNotifications.error)
          notificationService.error(response.data.message)
        }
      }
    }
  }
}
