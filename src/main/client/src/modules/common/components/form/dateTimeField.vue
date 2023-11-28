<template>
  <div class="form-group">
    <label v-show="label" class="col-form-label" :class="{ required: requiredVal }">{{ label | translate }}</label>
    <validation-provider :name="fieldId" :rules="validation || validationObj" v-slot="{ errors }" slim mode="lazy">
      <date-field :locale="locale"
                  :id="fieldId"
                  :class="{ 'is-invalid': errors.length, ...cssClass }"
                  :input-size="inputSize"
                  :no-label="true"
                  :max-date="maxDateData"
                  :min-date="minDateData"
                  :color="color"
                  :right="right"
                  :formatted="formatted()"
                  :format="format()"
                  v-model="innerValue"
                  :only-date="!showTime"
                  :only-time="!showDate"
                  :label="placeholder"
                  :disabled="disabled"
                  :no-clear-button="!clearable"
                  no-button-now></date-field>
      <p class="text-danger" v-if="errors.length">{{ errors[0] }}</p>
    </validation-provider>
  </div>
</template>
<script lang="ts">
import Component from 'vue-class-component'
import dateField from 'vue-ctk-date-time-picker'
import i18n from '@/i18n/i18n'
import { COLOR } from '@/config'
import moment, { Moment } from 'moment'
import { Prop, Watch } from 'vue-property-decorator'
import Vue from 'vue'

@Component({
  components: { dateField }
})
export default class DateTimeField extends Vue {
  locale = i18n.locale
  dateFormat = 'YYYY-MM-DD'
  dateTimeFormat = 'YYYY-MM-DDTHH:mm:ss.sss'
  timeFormat = 'HH:mm:ss.sss'
  innerValue?: string | null = null
  maxDateData?: string | null = null
  minDateData?: string | null = null
  readonly color = COLOR

  @Prop({ type: String }) cssClass?: string
  @Prop({ type: String, required: false }) fieldId?: string
  @Prop({ type: String }) label?: string
  @Prop({ type: String, default: 'sm' }) inputSize!: string
  @Prop({ type: Object }) maxDate?: Moment
  @Prop({ type: Object }) minDate?: Moment
  @Prop({ type: String, default: '' }) placeholder!: string
  @Prop({ type: String, default: '' }) validation!: string
  @Prop({ type: Object, default: () => {} }) validationObj!: any
  @Prop({ type: Object }) value!: Moment
  @Prop({ type: Boolean, default: true }) showTime!: boolean
  @Prop({ type: Boolean, default: true }) showDate!: boolean
  @Prop({ type: Boolean, default: false }) disabled!: boolean
  @Prop({ type: Boolean, default: false }) right!: boolean
  @Prop({ type: Boolean, default: true }) clearable?: boolean

  @Watch('innerValue')
  onInnerValueChange (value: string) {
    let valueToEmit : moment.Moment | null = null

    if (value) {
      valueToEmit = this.onlyTime ? moment(value, this.timeFormat) : moment(value)
    }

    this.$emit('input', valueToEmit)
  }

  @Watch('value')
  onValueChange (value: Moment) {
    this.seInnerValue(value)
  }

  @Watch('maxDate')
  onMaxDateChange (maxDate: Moment) {
    this.maxDateData = maxDate ? maxDate.format(this.showTime ? this.dateTimeFormat : this.dateFormat) : null
  }

  @Watch('minDate')
  onMinDateChange (minDate: Moment) {
    this.minDateData = minDate ? minDate.format(this.showTime ? this.dateTimeFormat : this.dateFormat) : null
  }

  get requiredVal () {
    return ((this.validation || '').indexOf('required') >= 0) || (this.validationObj || {}).required
  }

  get onlyDate () {
    return this.showDate && !this.showTime
  }

  get onlyTime () {
    return !this.showDate && this.showTime
  }

  private formatted () {
    if (this.showDate && this.showTime) {
      return 'lll'
    } else if (this.onlyDate) {
      return 'L'
    } else {
      return 'HH:mm'
    }
  }

  private format () {
    if (this.showDate && this.showTime) {
      return this.dateTimeFormat
    } else if (this.onlyDate) {
      return this.dateFormat
    } else {
      return this.timeFormat
    }
  }

  private seInnerValue (value?: Moment) {
    if (value) {
      if (this.onlyTime) {
        this.innerValue = value.format(this.timeFormat)
      } else if (this.onlyDate) {
        this.innerValue = value.format(this.dateFormat)
      } else {
        this.innerValue = value.format(this.dateTimeFormat)
      }
    } else {
      this.innerValue = null
    }
  }

  created () {
    this.seInnerValue(this.value)
  }
}
</script>
