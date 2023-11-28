<template>
  <div v-if="item">
    <form-modal
      v-if="item"
      :show-modal="show"
      :model="item"
      :title="isEdit() ? 'code-list.edit.label' : 'code-list.create.label'"
      :footer-class="isEdit() ? 'justify-content-between' : ''"
      @submit="onSubmit"
      @close="onClose">
      <div class="row form-row">
        <div class="col-12">
          <select-field
            :options="currencies"
            v-model="item.code"
            :codelist="true"
            :translate="false"
            field-id="currency"
            label="currency.label"
            label-prop="description"
            validation="required"></select-field>
        </div>
      </div>
      <template #footer v-if="isEdit()">
        <checkbox-field
          class="float-left"
          field-id="valid"
          label="common.inactive.label"
          v-model="item.valid"
          :reverse="true">
        </checkbox-field>
      </template>
    </form-modal>
  </div>
</template>

<script lang="ts">
import _ from 'lodash'
import formModal from '@/modules/common/components/formModal.vue'
import Component, { mixins } from 'vue-class-component'
import createModal from '@/modules/common/components/createModal'
import currencies from '@/modules/common/values/currencies'
import checkboxField from '@/modules/common/components/form/checkboxField.vue'
import selectField from '@/modules/common/components/form/selectField.vue'
import textField from '@/modules/common/components/form/textField.vue'
import Currency from '@/modules/currency/domain/currency'

@Component({
  components: { formModal, checkboxField, selectField, textField }
})
export default class CodeListCreateModalGeneric extends mixins<createModal<Currency>>(createModal) {
  currencies = currencies
  moduleName = 'currency'

  beforeSave () {
    this.item!.label = _.find(currencies, { value: this.item!.code })!.description!
  }
}
</script>
