import _ from 'lodash'
import AuthState from '@/modules/auth/store/authState'
import { GetterTree } from 'vuex'
import RootState from '@/store/rootState'

const getters: GetterTree<AuthState, RootState> = {
  loggedIn: (state: AuthState) => {
    return !!state.accessToken && state.validTo && state.validTo.isAfter()
  },
  hasPermission: (state: AuthState, getters: any) => (role: string) => {
    return getters.loggedIn && state.user && state.user.permissions && state.user.permissions.includes(role)
  },
  hasAnyPermission: (state: AuthState, getters: any) => (...roles: string[]) => {
    return getters.loggedIn && state.user && state.user.permissions && _.intersection(state.user.permissions, roles).length > 0
  }
}

export default getters
