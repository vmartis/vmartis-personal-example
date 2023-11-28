<template>
  <validation-provider :rules="validation" v-slot="{ errors }">
    <div class="form-group" :class="{ 'has-error': errors.length }">
      <label class="col-form-label pt-0 d-flex align-items-center" :class="{'checkbox-middle': middle, required: requiredVal}">
        <i class="fas fa-toggle-off fa-2x pr-1 checkbox-toggle"
           :title="label | translate"
           v-b-tooltip
           v-if="toggle"
           :class="{ 'fa-toggle-off': !innerValue, 'fa-toggle-on text-success':  innerValue, 'disabled': disabled}"></i>
        <input type="checkbox"
               v-show="!toggle"
               :class="cssClass"
               :name="fieldId"
               :disabled="disabled"
               v-model="innerValue"/>
        {{ label | translate }}
        <p class="text-danger" v-if="errors.length">{{ errors[0] }}</p>
      </label>
    </div>
  </validation-provider>
</template>
<script lang="ts">
import Vue from 'vue'
import { ValidationProvider } from 'vee-validate'
import Component from 'vue-class-component'
import { Prop, Watch } from 'vue-property-decorator'

@Component({
  components: { ValidationProvider }
})
export default class CheckboxField extends Vue {
  innerValue: boolean = false

  @Prop({ type: Boolean, default: false }) value!: boolean
  @Prop({ type: String, required: true }) label!: string
  @Prop({ type: String, required: true }) fieldId!: string
  @Prop({ type: String, default: '' }) validation?: string
  @Prop({ type: String }) cssClass?: string
  @Prop({ type: Boolean, default: false }) middle?: boolean
  @Prop({ type: Boolean, default: false }) reverse?: boolean
  @Prop({ type: Boolean, default: false }) toggle?: boolean
  @Prop({ type: Boolean, default: false }) disabled?: boolean

  @Watch('innerValue')
  onInnerValueChange (value: boolean) {
    this.$emit('input', this.reverse ? !value : value)
  }

  @Watch('value')
  onValueChange (value: boolean) {
    this.innerValue = this.reverse ? !value : value
  }

  get requiredVal () {
    return ((this.validation || '').indexOf('required') >= 0)
  }

  created () {
    this.innerValue = this.reverse ? !this.value : this.value
  }
}
</script>
