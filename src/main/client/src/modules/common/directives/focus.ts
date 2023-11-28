import Vue from 'vue'

Vue.directive('focus', {
  inserted: function (el, binding) {
    // condition pass when value is set to true or undefined
    // eslint-disable-next-line no-prototype-builtins
    if (!binding.hasOwnProperty('value') || binding.value) {
      el.focus()
    }
  }
})
