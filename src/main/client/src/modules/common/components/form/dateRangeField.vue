<template>
  <div class="form-group">
    <label v-show="label" class="col-form-label" :class="{ required: requiredVal }">{{ label | translate }}</label>
    <validation-provider :name="fieldId" :rules="validation || validationObj" v-slot="{ errors }" slim mode="lazy">
    <date-range-field :range="true"
                      :locale="locale"
                      :class="cssClass"
                      :custom-shortcuts="customShortcuts"
                      :input-size="inputSize"
                      :no-label="true"
                      :right="right"
                      :max-date="maxDateData"
                      @input="updateValue($event)"
                      :color="color"
                      formatted="L"
                      :no-clear-button="!clearable"
                      :format="dateFormat"
                      :value="rangeInternal"
                      @validate="validate"
                      :label="placeholder | translate"></date-range-field>
      <p class="text-danger" v-if="errors.length">{{ errors[0] }}</p>
    </validation-provider>
  </div>
</template>
<script lang="ts">
import { COLOR } from '@/config'
import Component from 'vue-class-component'
import dateRangeField from 'vue-ctk-date-time-picker'
import i18n from '@/i18n/i18n'
import i18nService from '@/i18n'
import moment, { Moment } from 'moment'
import { Prop, Watch } from 'vue-property-decorator'
import Range from '@/modules/common/components/form/range'
import Vue from 'vue'

@Component({
  components: { dateRangeField }
})
export default class DateRangeField extends Vue {
  locale = i18n.locale
  readonly color = COLOR
  customShortcuts = [
    { label: i18nService.message('date.today'), value: 'day', key: 'today' },
    { label: i18nService.message('date.yesterday'), value: '-day', key: 'yesterday' },
    { label: i18nService.message('date.week.current'), value: 'week', key: 'thisWeek' },
    { label: i18nService.message('date.week.last'), value: '-week', key: 'lastWeek' },
    { label: i18nService.message('date.month.current'), value: 'month', key: 'thisMonth' },
    { label: i18nService.message('date.month.last'), value: '-month', key: 'lastMonth' },
    { label: i18nService.message('date.year.current'), value: 'year', key: 'thisYear ' },
    { label: i18nService.message('date.year.last'), value: '-year', key: 'lastYear ' }
  ]

  rangeInternal: any | null = null
  maxDateData: string | null = null
  dateFormat = 'YYYY-MM-DD'

  @Prop({ type: String, required: false }) fieldId?: string
  @Prop({ type: String }) cssClass?: string
  @Prop({ type: String }) label?: string
  @Prop({ type: String, default: 'sm' }) inputSize!: string
  @Prop({ type: Object }) maxDate?: Moment
  @Prop({ type: String, default: 'common.range.label' }) placeholder!: string
  @Prop({ type: Range }) value!: Range
  @Prop({ type: Boolean, default: false }) right!: boolean
  @Prop({ type: String, default: '' }) validation!: string
  @Prop({ type: Object, default: () => {} }) validationObj!: any
  @Prop({ type: Boolean, default: true }) clearable?: boolean

  updateValue (value: any) {
    this.rangeInternal = value
    this.$emit('input', value ? new Range(value.start ? moment(value.start) : undefined, value.end ? moment(value.end) : undefined) : null)
  }

  @Watch('value')
  onValueChange (value?: Range) {
    if (value) {
      this.rangeInternal = {}
      if (value.from) {
        this.rangeInternal.start = value.from.format(this.dateFormat)
      }
      if (value.to) {
        this.rangeInternal.end = value.to.format(this.dateFormat)
      }
    }
  }

  get requiredVal () {
    return ((this.validation || '').indexOf('required') >= 0) || (this.validationObj || {}).required
  }

  @Watch('maxDate')
  onMaxDateChange (maxDate?: Moment) {
    this.maxDateData = maxDate ? maxDate.format(this.dateFormat) : null
  }

  validate () {
    this.$emit('validate')
  }

  mounted () {
    if (this.value) {
      this.rangeInternal = {}
      if (this.value.from) {
        this.rangeInternal.start = this.value.from.format(this.dateFormat)
      }
      if (this.value.to) {
        this.rangeInternal.end = this.value.to.format(this.dateFormat)
      }
      this.rangeInternal.shortcut = 'month'
    }
    if (this.maxDate) {
      this.maxDateData = this.maxDate.format(this.dateFormat)
    }
  }
}
</script>
