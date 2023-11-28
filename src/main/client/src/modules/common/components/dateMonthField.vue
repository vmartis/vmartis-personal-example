<template>
  <span class="now">
    <button class="btn btn-outline-secondary mr-2" @click="prevMonth()"><i class="fas fa-chevron-left"></i></button>
    {{ innerValue | dateMonth }}
    <button class="btn btn-outline-secondary ml-2" @click="nextMonth()"><i class="fas fa-chevron-right"></i></button>
  </span>
</template>

<script lang="ts">

import Component from 'vue-class-component'
import Vue from 'vue'
import { Prop, Watch } from 'vue-property-decorator'
import moment, { Moment } from 'moment'

@Component
export default class DateMonthField extends Vue {
  innerValue?: moment.Moment | null = null

  @Prop({ type: Object }) value!: Moment

  prevMonth () {
    if (this.innerValue) {
      this.innerValue = this.innerValue.subtract(1, 'month')
      this.$emit('input', moment(this.innerValue))
    }
  }

  nextMonth () {
    if (this.innerValue) {
      this.innerValue = this.innerValue.add(1, 'month')
      this.$emit('input', moment(this.innerValue))
    }
  }

  @Watch('value')
  onValueChange (value?: Moment) {
    this.innerValue = value ? moment(value) : null
  }

  created () {
    if (this.value) {
      this.innerValue = moment(this.value)
    }
  }
}
</script>
