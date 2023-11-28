import _ from 'lodash'

export default class FilterData {
  [key: string]: any

  constructor (data?: any) {
    _.merge(this, data)
  }
}
