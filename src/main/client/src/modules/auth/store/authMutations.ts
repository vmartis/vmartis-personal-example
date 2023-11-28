import { AUTH_LOG_IN, AUTH_LOG_OUT, AUTH_USER_UPDATE } from './authMutationTypes'
import AuthState from '@/modules/auth/store/authState'

const mutations = {
  async [AUTH_LOG_IN] (state: AuthState, action: any) {
    state.validTo = action.validTo
    state.accessToken = action.accessToken
    state.refreshToken = action.refreshToken
  },
  async [AUTH_USER_UPDATE] (state: AuthState, action: any) {
    state.user = action.user
  },
  async [AUTH_LOG_OUT] (state: AuthState) {
    state.user = null
    state.accessToken = null
    state.validTo = null
    state.refreshToken = null
  }
}

export default mutations
