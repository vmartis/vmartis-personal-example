import _ from 'lodash'
import { FORMAT_CURRENCY } from '@/utils'
import currencies from '@/modules/common/values/currencies'

export default (value: number, currency: string, defaultValue?: number) => {
  if (!_.isNil(value)) {
    return FORMAT_CURRENCY(value, currency, currencies)
  } else if (!_.isNil(defaultValue)) {
    return FORMAT_CURRENCY(defaultValue, currency, currencies)
  } else {
    return null
  }
}
