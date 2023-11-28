import _ from 'lodash'

export default (value?: any) => {
  if (_.isNil(value)) {
    return '-'
  } else {
    return value
  }
}
