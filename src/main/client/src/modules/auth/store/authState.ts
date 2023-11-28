import { Moment } from 'moment'
import User from '@/modules/user/domain/user'

export default class AuthState {
  accessToken: string | null
  refreshToken: string | null
  validTo: Moment | null
  user: User | null

  constructor (accessToken?: string | null, refreshToken?: string | null, validTo?: Moment | null, user?: User | null) {
    this.accessToken = accessToken || null
    this.refreshToken = refreshToken || null
    this.validTo = validTo || null
    this.user = user || null
  }
}
