import { FORMAT_DATE, FORMAT_DATE_PERIOD_SLASH, FORMAT_DATE_WEEK_OF_YEAR } from '@/utils'
import i18n from '@/i18n'
export class GroupType {
  value: string
  label: string
  dateFormat: Function

  constructor (value: string, label: string, dateFormat: Function) {
    this.value = value
    this.label = label
    this.dateFormat = dateFormat
  }
}

export default [
  new GroupType('month', i18n.message('group-type.month.label'), FORMAT_DATE_PERIOD_SLASH),
  new GroupType('week', i18n.message('group-type.week.label'), FORMAT_DATE_WEEK_OF_YEAR),
  new GroupType('day', i18n.message('group-type.day.label'), FORMAT_DATE)
]
