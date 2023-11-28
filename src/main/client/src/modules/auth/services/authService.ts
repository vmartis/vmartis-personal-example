import Credentials from '@/modules/auth/domain/credentials'
import moment from 'moment'
import { OAUTH_CLIENT_ID, OAUTH_SECRET } from '@/config'
import { PermissionType } from '@/modules/user/type/permissionType'
import { Route } from 'vue-router'
import User from '@/modules/user/domain/user'
import Vue from 'vue'

const LOGIN_URL = '/oauth/token'
const LOGOUT_URL = '/api/logout'
const REDIRECT_TO_PARAM = 'redirectTo'

export default {
  async login (credentials: Credentials) {
    const params = {
      grant_type: 'password',
      scope: 'read write',
      username: credentials.username,
      password: credentials.password,
      client_id: OAUTH_CLIENT_ID,
      client_secret: OAUTH_SECRET
    }

    const AUTH_BASIC_HEADERS = {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
        Accept: 'application/json',
        Authorization: 'Basic ' + window.btoa(OAUTH_CLIENT_ID + ':' + OAUTH_SECRET)
      },
      emulateJSON: true
    }

    const response = await (<any>Vue).http.post(LOGIN_URL, params, AUTH_BASIC_HEADERS)

    return {
      accessToken: response.data.access_token,
      refreshToken: response.data.refresh_token,
      validTo: moment().add(response.data.expires_in, 's')
    }
  },
  async logOut () {
    return (<any>Vue).http.get(LOGOUT_URL)
  },
  loginPath (route: Route) {
    let path = '/login'
    if (route.query[REDIRECT_TO_PARAM]) {
      path += `?${REDIRECT_TO_PARAM}=${route.query[REDIRECT_TO_PARAM]}`
    } else if (['login', 'logout', 'root', 'home'].indexOf(route.name || '') < 0) {
      path += `?${REDIRECT_TO_PARAM}=${encodeURIComponent(route.fullPath)}`
    }
    return path
  },
  mainPath (currentRoute: Route, user?: User) {
    const redirectToVal = currentRoute.query[REDIRECT_TO_PARAM]
    if (redirectToVal) {
      return decodeURIComponent(<string>redirectToVal)
    } else if (user && user.permissions.indexOf(PermissionType.WORK_RECORD_PERSONAL.toString()) >= 0) {
      return { name: 'workRecordsPersonal' }
    } else if (user && user.permissions.indexOf(PermissionType.COMPANY_PARTNER.toString()) >= 0) {
      return { name: 'partners' }
    } else {
      return { name: 'home' }
    }
  }
}
