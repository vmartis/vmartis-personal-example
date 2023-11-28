<template>
  <validation-provider :rules="validation || validationObj" v-slot="{ validate, errors }">
    <div class="form-group" :class="{ 'has-error': errors.length }">
      <label v-if="label && showLabel" class="col-form-label" :class="{ required: requiredVal }">{{ label | translate }}</label>
      <span class="static-field" v-if="readonly && selectedValue">{{ printValue(selectedValue.label) }}</span>
      <v-select v-if="!readonly"
                :value="selectedValue"
                @input="updateValue($event) && validate($event)"
                :placeholder="placeholder | translate"
                :multiple="multiple"
                :searchable="searchable"
                :filter-by="filterBy"
                :options="options"
                :clearable="clearable"
                :disabled="disabled"
                :label="labelProp"
                :taggable="taggable"
                :reduce="reduce"
                @search="onSearch">
        <div slot="no-options">{{ 'common.options.empty' | translate }}</div>
        <template slot="option" slot-scope="option">
          <i v-if="option._icon" :class="option._icon" class="pr-2"></i>
          <span>{{ printValue(findOption(option)[labelProp]) }}</span>
          <span v-if="option.labelDescription" class="text-description"><br/>({{ printValue(option.labelDescription) }})</span>
        </template>
        <template slot="selected-option" slot-scope="option">
          <i v-if="option._icon" :class="option._icon" class="pr-2"></i>
          <span> {{ printValue(findOption(option)[labelProp]) }}</span>
        </template>
      </v-select>
      <p class="text-danger" v-if="errors.length">{{ errors[0] }}</p>
    </div>
  </validation-provider>
</template>
<script lang="ts">
import _ from 'lodash'
import Component from 'vue-class-component'
import i18n from '@/i18n'
import Labeled from '@/modules/common/values/labeled'
import { normalize } from '@/utils'
import { Prop, Watch } from 'vue-property-decorator'
import { ValidationProvider } from 'vee-validate'
import vSelect from 'vue-select'
import Vue from 'vue'

@Component({
  components: { ValidationProvider, vSelect }
})
export default class SelectField extends Vue {
  selectedValue: any | null = null

  @Prop({ default: null }) value?: any
  @Prop({ type: String }) label?: string
  @Prop({ type: String, default: 'label' }) labelProp!: string
  @Prop({ type: String, default: 'label', required: true }) fieldId!: string
  @Prop({ type: String }) placeholder?: string
  @Prop({ type: String }) scope?: string
  @Prop({ type: Array, required: true, default: () => [] }) options?: Array<any>
  @Prop({ type: String, default: '' }) validation?: string
  @Prop({ type: Object, default: () => {} }) validationObj!: any
  // when codelist flag is set, only "value" is propagated to bind model, so return is string[]|int[] or string[]|int[]
  @Prop({ type: Boolean, default: false }) codelist?: boolean
  @Prop({ type: Boolean, default: false }) taggable?: boolean
  @Prop({ type: Boolean, default: false }) readonly?: boolean
  @Prop({ type: Boolean, default: true }) searchable?: boolean
  @Prop({ type: Boolean, default: true }) clearable?: boolean
  @Prop({ type: Function, default (option: Labeled, label: string, search: string) { return normalize(label).indexOf(normalize(search)) > -1 } }) filterBy?: Function
  @Prop({ type: Boolean, default: false }) multiple?: boolean
  @Prop({ type: Boolean, default: false }) disabled?: boolean
  @Prop({ type: Function, default: () => {} }) onSearch?: Function
  @Prop({ type: Boolean, default: true }) showLabel!: boolean
  @Prop({ type: Boolean, default: true }) translate!: boolean

  updateValue (value: any) {
    if (this.multiple && this.taggable) {
      this.selectedValue = _.uniq(value)
    } else {
      this.selectedValue = value
    }
    this.$emit('input', this.selectedValue)
  }

  valueUpdated (newValue: any) {
    this.selectedValue = newValue
  }

  get requiredVal () {
    return ((this.validation || '').indexOf('required') >= 0) || (this.validationObj || {}).required
  }

  @Watch('value')
  onValueChanged (newValue?: any) {
    this.valueUpdated(newValue)
  }

  // for situations, where value is set earlier then options -> e.g. filters with dynamic values from server
  // we need to simulate setting of value again
  @Watch('options')
  onOptionsChanged (newValues?: Array<any>) {
    if (newValues && newValues.length && this.value !== null) {
      this.valueUpdated(this.value)
    }
  }

  // find regular option -> referenced value can be only object with id/value
  findOption (option: any) {
    const optionFromList = _.find(this.options, { value: _.isNil(option.value) ? option[this.labelProp] : option.value })
    // if found in list of options, just retunn it
    if (optionFromList) {
      return optionFromList
    } else if (this.codelist && this.taggable) {
      // not possible to show label of codelist value
      return { label: option.label }
    } else if (this.codelist) {
      // not possible to show label of codelist value
      return { label: this.translate ? 'common.not-valid' : i18n.message('common.not-valid') }
    } else {
      // option is not in list of options -> probably deleted option, show it as selected
      return option
    }
  }

  printValue (value?: string) {
    if (_.isNil(value)) {
      return null
    } else {
      return this.translate ? i18n.message(value) : value
    }
  }

  private reduce (value: any) {
    if (this.codelist) {
      if (_.isString(value)) {
        return value
      } else {
        return _.isNil(value.value) ? value[this.labelProp] : value.value
      }
    }
    return value
  }

  // for initial fill -> watch doesn't work here
  mounted () {
    this.selectedValue = this.value
  }
}
</script>
