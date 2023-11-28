import { AUTH_LOG_IN, AUTH_USER_UPDATE, AUTH_LOG_OUT } from '@/modules/app/store/mutationTypes'
import { AUTH_STORAGE_KEY } from '@/config'

/**
 * react to AUTH_LOG_IN mutation and store authentication values to localstore
 * react to AUTH_USER_UPDATE mutation and store current user data to localstore
 * react to AUTH_LOG_OUT mutation and remove all authentication data from localstore
 * @param store
 */
const localStoragePlugin = (store: any) => {
  store.subscribe((mutation: any, state: any) => {
    const AUTH_MODULE_PREFIX = 'auth/'
    if (mutation.type === (AUTH_MODULE_PREFIX + AUTH_LOG_IN)) {
      localStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify(mutation.payload))
    } else if (mutation.type === (AUTH_MODULE_PREFIX + AUTH_USER_UPDATE)) {
      const authData = JSON.parse(localStorage.getItem(AUTH_STORAGE_KEY) || '{}')
      authData.user = mutation.payload.user
      localStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify(authData))
    } else if (mutation.type === (AUTH_MODULE_PREFIX + AUTH_LOG_OUT)) {
      localStorage.removeItem(AUTH_STORAGE_KEY)
    }
  })
}

export default localStoragePlugin
