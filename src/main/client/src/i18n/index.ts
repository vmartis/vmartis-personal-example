import i18n from './i18n'
import { localize } from 'vee-validate'
import VueI18n from 'vue-i18n'

export default {
  message (key: string, values?: VueI18n.Values) {
    return i18n.t(key, values) as string
  },
  changeLocale (locale: string) {
    i18n.locale = locale
    localize(locale)
  }
}
