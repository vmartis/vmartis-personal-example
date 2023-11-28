import _ from 'lodash'

export default (value: any, codeList: Array<any> = [], property: string) => {
  return value ? _.find(codeList, { value: value })[property] : null
}
