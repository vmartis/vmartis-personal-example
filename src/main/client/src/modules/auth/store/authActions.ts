import { ActionTree, Commit } from 'vuex'
import { AUTH_LOG_IN, AUTH_LOG_OUT, AUTH_USER_UPDATE } from './authMutationTypes'
import authService from '../services/authService'
import AuthState from '@/modules/auth/store/authState'
import Credentials from '@/modules/auth/domain/credentials'
import userService from '@/modules/user/services/userService'
import RootState from '@/store/rootState'

const actions: ActionTree<AuthState, RootState> = {
  async logIn ({ dispatch, commit }, credentials: Credentials) {
    try {
      const token = await authService.login(credentials)
      await commit(AUTH_LOG_IN, token)
      await dispatch('fetchMe')
      return true
    } catch (ex) {
      return false
    }
  },
  async logOut ({ dispatch, commit }) {
    await dispatch('app/loadingDataEnable', 'logout', { root: true })
    await authService.logOut()
    await commit(AUTH_LOG_OUT)
  },
  async fetchMe ({ commit }: { commit: Commit }) {
    const user = await userService.me()
    if (user) {
      await commit(AUTH_USER_UPDATE, {
        user: user
      })
      return user
    }
  }
}

export default actions
