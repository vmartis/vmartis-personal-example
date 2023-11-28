<template>
  <div class="form-group">
    <label v-show="label && showLabel" class="col-form-label" :class="{ required: requiredVal }">{{ label | translate }}</label>
    <validation-provider :name="fieldId" :rules="validation || validationObj" v-slot="{ errors }" slim mode="lazy">
      <div :class="{'input-group': isGroupType}">
        <div class="input-group-prepend" v-if="hasPrepend()">
          <slot name="prepend"></slot>
        </div>
        <input type="number"
               :name="fieldId"
               :id="fieldId"
               v-model="innerValue"
               :min="min"
               :max="max"
               :step="step"
               class="form-control"
               :class="generateCssClassObj(errors)"
               :placeholder="placeholder | translate"
               :readonly="readonly"/>
        <div class="input-group-append" v-show="suffix">
          <div class="input-group-text">{{ suffix }}</div>
        </div>
      </div>
      <p class="text-danger" v-if="errors.length">{{ errors[0] }}</p>
    </validation-provider>
  </div>
</template>
<script lang="ts">
import _ from 'lodash'
import { ValidationProvider } from 'vee-validate'
import Component from 'vue-class-component'
import Vue from 'vue'
import { Prop, Watch } from 'vue-property-decorator'

@Component({
  components: { ValidationProvider }
})
export default class TextField extends Vue {
  innerValue?: string = ''

  @Prop({ default: '' }) value!: any
  @Prop({ type: String }) label?: string
  @Prop({ type: String, required: false, default: '' }) cssClass?: string
  @Prop({ type: String, required: true }) fieldId?: string
  @Prop({ type: Boolean, required: false }) readonly?: boolean
  @Prop({ type: Number, required: false }) max?: number
  @Prop({ type: Number, required: false }) min?: number
  @Prop({ type: Number, required: false, default: 1 }) step?: number
  @Prop({ type: String, default: '' }) placeholder!: string
  @Prop({ type: String, required: false }) suffix?: string
  @Prop({ type: String }) validation!: string
  @Prop({ type: Object, default: () => {} }) validationObj!: any
  @Prop({ type: Boolean, default: false }) required!: boolean

  @Prop({ type: Number, required: false, default: 100000 }) maxlength?: number
  @Prop({ type: Boolean, default: false }) focus!: boolean
  @Prop({ type: Boolean, default: true }) showLabel!: boolean
  @Prop({ type: Boolean, default: true }) showInput!: boolean

  get isGroupType () {
    return !!this.suffix || this.hasPrepend()
  }

  generateCssClassObj (errors: Array<any>) {
    const cssObject: any = { 'is-invalid': errors.length }
    if (this.cssClass) {
      cssObject[this.cssClass] = true
    }
    return cssObject
  }

  @Watch('innerValue')
  onInnerValueChange (value: string) {
    if (_.isNil(value) || value === '') {
      this.$emit('input', null)
    } else if (!isNaN(Number(value))) {
      this.$emit('input', Number(value))
    }
  }

  @Watch('value')
  onValueChange (value: string) {
    this.innerValue = _.isNil(value) ? '' : value + '' // ensure not setting null value into input type number (causing error of validation)
  }

  get requiredVal () {
    return this.required || ((this.validation || '').indexOf('required') >= 0) || (this.validationObj || {}).required
  }

  hasPrepend (): boolean {
    return !!this.$slots.prepend
  }

  created () {
    if (!_.isNil(this.value)) {
      this.innerValue = this.value
    }
  }
}
</script>
