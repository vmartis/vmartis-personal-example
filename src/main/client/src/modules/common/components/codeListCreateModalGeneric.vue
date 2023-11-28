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
          <text-field field-id="label"
                      label="common.name"
                      placeholder="common.name.placeholder"
                      v-model="item.label"
                      :maxlength="100"
                      :validation-obj="{ required: true, min: labelMinLength, max: 100 }"></text-field>
        </div>
      </div>
      <div class="row form-row" v-if="colorable">
        <div class="col-12">
          <div class="form-group">
            <label class="col-form-label required">{{ 'common.color' | translate }}</label>
            <div>
              <v-swatches v-model="item.color" swatches="text-advanced"></v-swatches>
            </div>
          </div>
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
import formModal from '@/modules/common/components/formModal.vue'
import Component, { mixins } from 'vue-class-component'
import textField from '@/modules/common/components/form/textField.vue'
import checkboxField from '@/modules/common/components/form/checkboxField.vue'
import createModal from '@/modules/common/components/createModal'
import { Prop } from 'vue-property-decorator'
import VSwatches from 'vue-swatches'

@Component({
  components: { textField, checkboxField, formModal, VSwatches }
})
export default class CodeListCreateModalGeneric extends mixins(createModal) {
  @Prop(String) moduleName!: string
  @Prop({ type: Boolean, default: false }) colorable?: boolean
  @Prop({ type: Number, default: 2 }) labelMinLength!: boolean
}
</script>
