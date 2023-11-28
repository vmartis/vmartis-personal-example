<template>
  <div v-if="item">
    <form-modal
      v-if="item"
      :show-modal="show"
      :model="item"
      title="company.sale-settings.edit.label"
      @submit="onSubmit"
      @close="onClose">
      <div class="row form-row">
        <div class="col-6">
          <select-field
            :options="paymentTypes"
            v-model="item.saleSettings.paymentType"
            field-id="paymentType"
            :translate="false"
            label="company.sale-settings.payment-type.label"></select-field>
        </div>
        <div class="col-3">
          <number-field
            v-model="item.saleSettings.dueDateOffset"
            label="company.sale-settings.due-date-offset.label"
            field-id="dueDateOffset"
            :min="0"
            :step="10"
            :suffix=" 'common.days' | translate"
            validation="numeric|min_value:0"></number-field>
        </div>
      </div>
      <div class="row form-row mt-3">
        <div class="col-3">
          <checkbox-field
            field-id="deliveryNoteEnabled"
            label="company.sale-settings.delivery-note-enabled.label"
            :toggle="true"
            v-model="item.saleSettings.deliveryNoteEnabled">
          </checkbox-field>
        </div>
      </div>
    </form-modal>
  </div>
</template>

<script lang="ts">
import CodeList from '@/modules/common/domain/codeList'
import Component, { mixins } from 'vue-class-component'
import createModal from '@/modules/common/components/createModal'
import formModal from '@/modules/common/components/formModal.vue'
import { Getter } from 'vuex-class'
import checkboxField from '@/modules/common/components/form/checkboxField.vue'
import numberField from '@/modules/common/components/form/numberField.vue'
import selectField from '@/modules/common/components/form/selectField.vue'
import textField from '@/modules/common/components/form/textField.vue'

@Component({
  components: { formModal, checkboxField, numberField, selectField, textField }
})
export default class CompanySaleSettingsCreateModal extends mixins(createModal) {
  moduleName = 'company'

  @Getter('validItems', { namespace: 'paymentType' }) paymentTypes!: Array<CodeList>

  async created () {
    await this.$store.dispatch('paymentType/getAll')
  }
}
</script>
