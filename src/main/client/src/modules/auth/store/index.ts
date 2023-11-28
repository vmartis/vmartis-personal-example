import moment from 'moment'
import actions from './authActions'
import mutations from './authMutations'
import getters from './authGetters'
import { AUTH_STORAGE_KEY } from '@/config'
import User from '@/modules/user/domain/user'
import AuthState from '@/modules/auth/store/authState'

// Local storage sync state
const authData = localStorage.getItem(AUTH_STORAGE_KEY)
let initialState, storedData

if (authData) {
  storedData = JSON.parse(authData)
  initialState = new AuthState(storedData.accessToken, storedData.refreshToken, moment(storedData.validTo), new User(storedData.user))
} else {
  initialState = new AuthState()
}

export default {
  namespaced: true,
  state: initialState,
  getters: getters,
  mutations: mutations,
  actions: actions
}
