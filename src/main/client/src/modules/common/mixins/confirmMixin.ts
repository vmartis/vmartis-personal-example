import Vue from 'vue'
import i18n from '@/i18n'
import Component from 'vue-class-component'

@Component
export default class ConfirmMixin extends Vue {
  confirm (confirmationTextKey: string, args?: Array<any>) {
    return this.$bvModal.msgBoxConfirm(i18n.message(confirmationTextKey, args), {
      title: i18n.message('confirmation.title'),
      size: 'sm',
      buttonSize: 'md',
      okVariant: 'danger',
      okTitle: i18n.message('confirmation.button.yes'),
      cancelTitle: i18n.message('common.cancel'),
      footerClass: 'p-2',
      hideHeaderClose: false,
      centered: true
    })
  }
}
