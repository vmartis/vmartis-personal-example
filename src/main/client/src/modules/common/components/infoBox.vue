<template>
  <div class="info-box" :class="{ [bgClass] : fill, md: medium }">
    <span class="info-box-icon" :class="{ [bgClass] : !fill }"><i :class="iconClass"></i></span>
    <div class="info-box-content">
      <span v-if="title" class="info-box-text" :class="[titleClass]">{{ title | translate }}</span>
      <span v-if="value" class="info-box-number" :class="[valueClass]">{{ value }}</span>
      <slot name="content"></slot>
    </div>
  </div>
</template>

<script lang="ts">
import Vue from 'vue'
import Component from 'vue-class-component'
import { Prop } from 'vue-property-decorator'

@Component
export default class InfoBox extends Vue {
  @Prop(String) title?: string
  @Prop(String) value?: string
  @Prop(String) iconClass?: string
  @Prop(String) titleClass?: string
  @Prop({ type: String, default: 'bg-teal' }) bgClass?: string
  @Prop(String) valueClass?: string
  @Prop({ type: Boolean, default: false }) fill?: boolean
  @Prop({ type: Boolean, default: false }) medium?: boolean
}
</script>
