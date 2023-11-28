<template>
  <div class="form-group">
    <label class="col-form-label" :class="{ required: requiredVal }" v-show="showLabel">
      <slot name="label"></slot>
      {{ label | translate }}
    </label>
    <validation-provider :name="fieldId" :rules="validation || validationObj" v-slot="{ errors }" slim mode="lazy">
      <input :type="type"
             :id="fieldId"
             :ref="inputRef"
             v-model="innerValue"
             :placeholder="placeholderText"
             :maxlength="maxlength"
             class="form-control"
             :class="{ 'is-invalid': errors.length, ...cssClass }"
             v-focus="focus"
             @paste="paste"
             :readonly="readonly"
             :autocomplete="autocomplete ? autocomplete : ''"
             v-show="showInput"
      />
      <p class="text-danger" v-if="errors.length">{{ errors[0] }}</p>
    </validation-provider>
  </div>
</template>
<script lang="ts">
import Vue from 'vue'
import { ValidationProvider } from 'vee-validate'
import Component from 'vue-class-component'
import { Prop, Watch } from 'vue-property-decorator'
import i18n from '@/i18n'

@Component({
  components: { ValidationProvider }
})
export default class TextField extends Vue {
  innerValue?: string | null = null

  @Prop({ type: String }) autocomplete!: string
  @Prop({ type: String, default: '' }) value!: string
  @Prop({ type: String, default: 'text' }) type!: string
  @Prop({ type: String }) label?: string
  @Prop({ type: String }) placeholder!: string
  @Prop({ type: String, required: false }) cssClass?: string
  @Prop({ type: String, required: true }) fieldId?: string
  @Prop({ type: Boolean, required: false }) readonly?: boolean
  @Prop({ type: Number, required: false, default: 100000 }) maxlength?: number
  @Prop({ type: String }) validation!: string
  @Prop({ type: String, required: false }) inputRef!: string
  @Prop({ type: Boolean, default: false }) required!: boolean
  @Prop({ type: Object, default: () => {} }) validationObj!: any
  @Prop({ type: Boolean, default: false }) focus!: boolean
  @Prop({ type: Boolean, default: true }) showLabel!: boolean
  @Prop({ type: Boolean, default: true }) showInput!: boolean

  paste (e: Event) {
    this.$emit('paste', e)
  }

  get placeholderText () {
    return this.placeholder ? i18n.message(this.placeholder) : ''
  }

  get requiredVal () {
    return this.required || ((this.validation || '').indexOf('required') >= 0) || (this.validationObj || {}).required
  }

  @Watch('innerValue')
  onInnerValueChange (value: string) {
    this.$emit('input', value)
  }

  @Watch('value')
  onValueChange (value: string) {
    this.innerValue = value
  }

  created () {
    if (this.value) {
      this.innerValue = this.value
    }
  }
}
</script>
