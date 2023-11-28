/**
 * Mixin enforces instant of validator per vue component. Automatic installation of $validator is disabled by
 * VeeValidation configuration in CLIENT_SOURCE/validation/index.js
 */
import Component, { mixins } from 'vue-class-component'
import submitProtectionMixin from '@/modules/common/mixins/submitProtectionMixin'

@Component
export default class ValidationMixin extends mixins(submitProtectionMixin) {
  async submit (form: any) {
    await this.protect()
    const isValid = await form.validate()
    if (!isValid) {
      await this.unprotect()
    } else {
      const promise = (<any> this).onSubmit()
      if (promise) {
        promise.finally(this.unprotect)
      }
    }
  }
}
