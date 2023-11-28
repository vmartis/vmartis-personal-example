/* eslint-disable no-unused-vars */

import { configure, extend, localize, ValidationProvider } from 'vee-validate'
import * as rules from 'vee-validate/dist/rules'
import decimal2 from './decimalValidator'
import { validations as skValidations } from '@/i18n/sk'
import { validations as csValidations } from '@/i18n/cs'
import Vue from 'vue'

export default () => {
  const config = {
    useConstraintAttrs: false, // disable inferred of HTML5 validations
    validity: false // disable HTML5 validations
  }

  // Sets the options.
  configure(config)

  // Register it globally
  Vue.component('ValidationProvider', ValidationProvider)

  extend('confirmed', rules.confirmed)
  extend('decimal', decimal2)
  extend('digits', rules.digits)
  extend('email', rules.email)
  extend('length', rules.length)
  extend('max', rules.max)
  extend('max_value', rules.max_value)
  extend('min', rules.min)
  extend('min_value', rules.min_value)
  extend('numeric', rules.numeric)
  extend('regex', rules.regex)
  extend('required', rules.required)

  // @ts-ignore
  localize({
    sk: { messages: skValidations },
    cs: { messages: csValidations }
  })
}
