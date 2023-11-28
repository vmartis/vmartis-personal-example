import StaticValue from '@/modules/common/values/staticValue'
import i18n from '@/i18n'

export default [
  new StaticValue('EUR', i18n.message('value.currency.label.EUR'), i18n.message('value.currency.description.EUR')),
  new StaticValue('CZK', i18n.message('value.currency.label.CZK'), i18n.message('value.currency.description.CZK'))
]
