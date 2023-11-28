'use strict'

import _ from 'lodash'

export default (value: string, length: number) => {
  if (_.isUndefined(value) || value === null || !length) {
    return null
  } else {
    return _.truncate(value, { length: length })
  }
}
