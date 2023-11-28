import sk from '@/i18n/sk'
import cs from '@/i18n/cs'
import { DEFAULT_LOCALE } from '@/config'
import Vue from 'vue'
import VueI18n from 'vue-i18n'

Vue.use(VueI18n)

const i18n = new VueI18n({
  locale: DEFAULT_LOCALE,
  messages: {
    sk,
    cs
  }
})

export default i18n
