<template>
  <div class="form-group" >
    <label class="col-form-label" :class="{ required: requiredVal, 'no-label': !label }" v-if="showLabel">
      {{ label | translate }}
    </label>
    <validation-provider :rules="validation || validationObj" v-slot="{ errors }" mode="lazy" tag="div">
      <div class="radio-field" :class="{'is-invalid': errors.length}">
        <div :class="{'btn-group btn-group-toggle': buttons}">
          <label class="text-nowrap"
                 :class="classObj(option.value)"
                 :for="fieldId + '-' + option.value"
                 v-for="option in options"
                 :title="showIcon ? printValue(option.label) : null" v-b-tooltip
                 v-bind:key="option.value">
            <input type="radio"
                   :id="fieldId + '-' + option.value"
                   class="form-check-input"
                   :class="{'is-invalid' : errors.length, ...cssClass}"
                   :name="fieldId"
                   :disabled="disabled"
                   :value="option.value"
                   v-model="innerValue">
            <i v-if="showIcon" :class="optionIcon(option.value)"></i>
            <template v-if="!showIcon">{{ printValue(option.label) }}</template>
          </label>
        </div>
      </div>
      <div class="text-danger" v-if="errors.length">{{ errors[0] }}</div>
    </validation-provider>
  </div>
</template>

<script lang="ts">
import Component from 'vue-class-component'
import { ValidationProvider } from 'vee-validate'
import Vue from 'vue'
import { Prop, Watch } from 'vue-property-decorator'
import _ from 'lodash'
import i18n from '@/i18n'

@Component({
  components: { ValidationProvider }
})
export default class RadioField extends Vue {
  innerValue?: string | null = null

  @Prop({ type: String, default: '' }) value!: string
  @Prop({ type: String }) label?: string
  @Prop({ type: String, required: false }) cssClass?: string
  @Prop({ type: String, required: true }) fieldId?: string
  @Prop({ type: String }) validation!: string
  @Prop({ type: Object, default: () => {} }) validationObj!: any
  @Prop({ type: Boolean, required: false, default: true }) inline?: boolean
  @Prop({ type: Array, required: true, default: () => [] }) options?: Array<any>
  @Prop({ type: Boolean, default: true }) showLabel!: boolean
  @Prop({ type: Boolean, default: false }) buttons!: boolean
  @Prop({ type: Boolean, default: false }) disabled!: boolean
  @Prop({ type: Boolean, default: true }) translate!: boolean
  @Prop({ type: Boolean, default: false }) showIcon!: boolean
  @Prop({ type: Object, default: () => {} }) icons: any
  @Prop({ type: Function }) optionClass?: any
  @Prop({ type: Function, default: () => null }) optionIcon?: any

  @Watch('innerValue')
  onInnerValueChange (value: string) {
    this.$emit('input', value)
  }

  @Watch('value')
  onValueChange (value: string) {
    this.innerValue = value
  }

  get requiredVal () {
    return ((this.validation || '').indexOf('required') >= 0) || (this.validationObj || {}).required
  }

  printValue (value?: string) {
    if (_.isNil(value)) {
      return null
    } else {
      return this.translate ? i18n.message(value) : value
    }
  }

  classObj (value: any) {
    const active = this.innerValue === value
    const classObj : any = {
      btn: this.buttons,
      active: active,
      'form-check-label': !this.buttons,
      'form-check': !this.buttons,
      disabled: this.disabled,
      'form-check-inline': this.inline && !this.buttons
    }
    if (active && this.optionClass && this.optionClass(value)) {
      classObj[this.optionClass(value)] = true
    } else if (this.buttons) {
      classObj['btn-default'] = true
    }
    return classObj
  }

  created () {
    if (this.value) {
      this.innerValue = this.value
    }
  }
}
</script>
