'use strict'
import _ from 'lodash'

export default (value?: string) => {
  if (_.isUndefined(value) || value === null) {
    return null
  } else {
    return _(value).chain().trimStart('http://').trimStart('https://').trimEnd('/').value()
  }
}
