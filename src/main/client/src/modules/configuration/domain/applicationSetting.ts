import _ from 'lodash'
import { LocalizationType } from '@/modules/configuration/type/LocalizationType'

export default class ApplicationSetting {
  localization?: LocalizationType

  constructor (data: any) {
    _.merge(this, data)
  }
}
