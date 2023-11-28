'use strict'
import _ from 'lodash'

export default (value?: string) => {
  if (_.isNil(value)) {
    return null
  } else {
    return value.toLocaleLowerCase().startsWith('http') ? value : 'http://' + value
  }
}
