import notificationService from '@/modules/common/services/notificationService'
import authService from '@/modules/auth/services/authService'
import { Store } from 'vuex'
import { Route } from 'vue-router'

export default (store: Store<any>) => {
  return (to: Route, from: Route, next: Function) => {
    if (to.meta.requiresLoggedIn && !store.getters['auth/loggedIn']) {
      next(authService.loginPath(to))
    } else if (to.meta.requiresPermission && !store.getters['auth/hasPermission'](to.meta.requiresPermission)) {
      notificationService.error('error.not.allowed')
    } else if (to.meta.requiresAnyPermission && !store.getters['auth/hasAnyPermission'](...to.meta.requiresAnyPermission)) {
      notificationService.error('error.not.allowed')
    } else if (to.name === 'login' && store.getters['auth/loggedIn']) {
      if (store.getters['auth/loggedIn']) {
        next(authService.mainPath(to))
      } else {
        next()
      }
    } else {
      next()
    }
  }
}
