import _ from 'lodash'
import moment from 'moment'
import { YEARS_MIN } from '@/config'
import StaticValue from '@/modules/common/values/staticValue'

const years = _.range(YEARS_MIN, moment().year() + 2).reverse().map(year => (new StaticValue(year, year + '')))

export default years
