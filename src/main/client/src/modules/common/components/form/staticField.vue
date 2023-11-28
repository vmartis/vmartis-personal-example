<template>
  <div class="form-group">
    <label v-if="label && showLabel" class="col-form-label" :class="labelClass">{{ label | translate }}</label>
    <span class="static-field" :class="contentClass">
      <slot></slot>
    </span>
  </div>
</template>
<script lang="ts">
import Vue from 'vue'
import { Prop } from 'vue-property-decorator'
import Component from 'vue-class-component'

@Component
export default class StaticField extends Vue {
  @Prop({ type: String, required: true }) label!: string
  @Prop({ type: String, required: false, default: '' }) labelClass!: string
  @Prop({ type: String, required: false, default: '' }) contentClass!: string
  @Prop({ type: Boolean, default: true }) showLabel!: boolean
}
</script>
