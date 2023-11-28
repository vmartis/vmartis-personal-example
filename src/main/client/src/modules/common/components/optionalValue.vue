<template>
  <span :is="tag">
    {{ value | optional }}
  </span>
</template>

<script lang="ts">
import Component from 'vue-class-component'
import { Prop } from 'vue-property-decorator'
import Vue from 'vue'

@Component
export default class OptionalValue extends Vue {
  @Prop({ type: String, default: 'span' }) tag!: string
  @Prop({ type: String }) value!: string | undefined | null
}
</script>
