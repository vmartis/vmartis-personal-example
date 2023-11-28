<template>
  <div v-if="item">
    <form-modal
      v-if="item"
      :show-modal="show"
      :model="item"
      :title="isEdit() ? 'company-branch.edit.label' : 'company-branch.create.label'"
      :footer-class="isEdit() ? 'justify-content-between' : ''"
      @submit="onSubmit"
      @close="onClose">
      <div class="row form-row">
        <div class="col-12">
          <text-field field-id="name"
                      label="company-branch.name.label"
                      placeholder="company-branch.name.placeholder"
                      v-model="item.name"
                      :maxlength="100"
                      validation="required|min:3|max:100"></text-field>
        </div>
      </div>
      <hr>
      <address-form :address="item.address"></address-form>

      <div class="row form-row" v-if="showForOrder">
        <div class="col-12 mt-2">
          <checkbox-field
            field-id="forOrder"
            label="company.for-order.label"
            :toggle="true"
            v-model="item.forOrder">
          </checkbox-field>
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
import formModal from '@/modules/common/components/formModal.vue'
import Component, { mixins } from 'vue-class-component'
import textField from '@/modules/common/components/form/textField.vue'
import checkboxField from '@/modules/common/components/form/checkboxField.vue'
import selectField from '@/modules/common/components/form/selectField.vue'
import createModal from '@/modules/common/components/createModal'
import addressForm from '@/modules/common/components/addressForm.vue'
import { Prop } from 'vue-property-decorator'

@Component({
  components: { selectField, textField, checkboxField, formModal, addressForm }
})
export default class CompanyBranchCreateModal extends mixins(createModal) {
  moduleName = 'companyBranch'

  @Prop({ type: Boolean, default: true }) showForOrder!: boolean

  afterSuccessSave () {
    this.$store.dispatch('company/refresh')
  }
}
</script>
