<template>
  <div v-if="item">
    <form-modal
      v-if="item"
      :show-modal="show"
      :model="item"
      :title="isEdit() ? 'contact.edit.label' : 'contact.create.label'"
      :footer-class="isEdit() ? 'justify-content-between' : ''"
      @submit="onSubmit"
      @close="onClose">

      <div class="row form-row">
        <div class="col-md-12">
          <text-field field-id="name"
                      label="contact.name.label"
                      placeholder="contact.name.placeholder"
                      v-model="item.name"
                      :maxlength="100"
                      validation="required|min:3|max:100"></text-field>
        </div>
      </div>
      <div class="row form-row">
        <div class="col-md-6">
          <text-field field-id="email"
                      label="common.email"
                      placeholder="common.email.placeholder"
                      v-model="item.email"
                      :maxlength="50"
                      validation="min:6|max:50"></text-field>
        </div>
        <div class="col-md-6">
          <text-field field-id="phoneNumber"
                      label="contact.phone-number.label"
                      placeholder="contact.phone-number.placeholder"
                      v-model="item.phoneNumber"
                      :maxlength="30"
                      validation="min:9|max:30"></text-field>
        </div>
      </div>
      <div class="row form-row">
        <div class="col-md-6">
          <text-field field-id="position"
                      label="contact.position.label"
                      placeholder="contact.position.placeholder"
                      v-model="item.position"
                      :maxlength="50"
                      validation="min:2|max:50"></text-field>
        </div>
      </div>
      <div class="row form-row">
        <div class="col-md-12">
          <text-field v-model="item.note" label="common.note" field-id="note" :maxlength="300"
                      validation="max:300"></text-field>
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

@Component({
  components: { selectField, textField, checkboxField, formModal }
})
export default class ContactCreateModal extends mixins(createModal) {
    moduleName = 'contact'
}
</script>
