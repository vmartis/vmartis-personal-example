<template>
  <div v-if="item">
    <form-modal
      v-if="item"
      :show-modal="show"
      :model="item"
      :title="isEdit() ? 'bank-account.update.title' : 'bank-account.create.label'"
      :footer-class="isEdit() ? 'justify-content-between' : ''"
      @submit="onSubmit"
      @close="onClose">
      <div class="row form-row">
        <div class="col-8">
          <text-field field-id="name"
                      label="bank-account.name.label"
                      placeholder="bank-account.name.placeholder"
                      v-model="item.name"
                      :maxlength="100"
                      validation="required|min:3|max:100"></text-field>
        </div>
        <div class="col-md-4">
          <select-field
            :options="currencies"
            v-model="item.currency"
            :codelist="true"
            :translate="false"
            :clearable="false"
            field-id="currency"
            label="currency.label"
            validation="required"></select-field>
        </div>
      </div>
      <div class="row form-row">
        <div class="col-md-8">
          <text-field field-id="accountId"
                      label="bank-account.number.label"
                      placeholder="bank-account.number.placeholder"
                      v-model="item.accountId"
                      :maxlength="17"
                      :validation-obj="{required: true, min: 1, max: 17, regex: /^[0-9-]{1,17}$/}"></text-field>
        </div>
        <div class="col-md-4">
          <text-field field-id="bankId"
                      label="bank-account.bank-id.label"
                      placeholder="bank-account.bank-id.placeholder"
                      v-model="item.bankId"
                      :maxlength="4"
                      :validation-obj="{required: true, length: 4, regex: /^[0-9]{4}$/}"></text-field>
        </div>
      </div>
      <div class="row form-row">
        <div class="col-md-8">
          <text-field field-id="iban"
                      label="bank-account.iban.label"
                      placeholder="bank-account.iban.placeholder"
                      v-model="item.iban"
                      :maxlength="34"
                      :validation-obj="{min: 4, max: 34, regex: /^[A-Z,0-9]{3,34}$/}"
                      @paste="onIbanPaste"></text-field>
        </div>
        <div class="col-md-4">
          <text-field field-id="bic"
                      label="bank-account.bic.label"
                      placeholder="bank-account.bic.placeholder"
                      v-model="item.bic"
                      :maxlength="11"
                      :validation-obj="{min: 1, max: 11, regex: /^[A-Z,0-9]{1,11}$/}"></text-field>
        </div>
      </div>
      <template #footer v-if="isEdit()">
        <checkbox-field
          class="float-left"
          field-id="valid"
          label="common.inactive.label"
          v-model="item.active"
          :reverse="true">
        </checkbox-field>
      </template>
    </form-modal>
  </div>
</template>

<script lang="ts">
import addressForm from '@/modules/common/components/addressForm.vue'
import Component, { mixins } from 'vue-class-component'
import createModal from '@/modules/common/components/createModal'
import Currency from '@/modules/currency/domain/currency'
import formModal from '@/modules/common/components/formModal.vue'
import { Getter } from 'vuex-class'
import checkboxField from '@/modules/common/components/form/checkboxField.vue'
import selectField from '@/modules/common/components/form/selectField.vue'
import textField from '@/modules/common/components/form/textField.vue'
import BankAccount from '@/modules/bank/account/domain/bankAccount'

@Component({
  components: { selectField, textField, checkboxField, formModal, addressForm }
})
export default class BankAccountCreateModal extends mixins<createModal<BankAccount>>(createModal) {
  moduleName = 'bankAccount'

  @Getter('validItems', { namespace: 'currency' }) currencies!: Array<Currency>

  onIbanPaste (event: ClipboardEvent) {
    this.item!.iban = (event.clipboardData!.getData('text') || '').replace(/\s/g, '')
    event.preventDefault()
  }

  async created () {
    await this.$store.dispatch('currency/getAll')
  }
}
</script>
