import i18n from '@/i18n'
import miniToastr from 'mini-toastr'
import VueI18n from 'vue-i18n'

export default {
  init () {
    // If using mini-toastr, provide additional configuration
    const toastTypes = {
      success: 'success',
      error: 'error',
      info: 'info',
      warn: 'warn'
    }

    miniToastr.init({ types: toastTypes, timeout: 5000 })
  },

  info (key: string, values?: VueI18n.Values) {
    miniToastr.info(i18n.message(key, values))
  },
  success (key: string, values?: VueI18n.Values) {
    miniToastr.success(i18n.message(key, values))
  },
  error (key: string, values?: VueI18n.Values) {
    miniToastr.error(i18n.message(key, values))
  }
}
