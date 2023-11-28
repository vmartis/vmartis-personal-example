import { Moment } from 'moment'

export default (value: Moment) => {
  return value ? value.format('MM/YYYY') : null
}
