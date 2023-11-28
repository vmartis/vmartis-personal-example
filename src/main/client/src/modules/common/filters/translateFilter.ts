'use strict'
import i18n from '@/i18n/'

import _ from 'lodash'

export default (value?: string, args?: Array<String>) => {
  if (_.isNil(value)) {
    return null
  } else if (value === '') {
    return value
  } else {
    return i18n.message(value, args)
  }
}
