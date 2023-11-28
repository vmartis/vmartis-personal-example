<template>
  <i class="fas" :class="{'fa-check text-success': model, 'fa-times text-danger': !model}"></i>
</template>

<script lang="ts">
import Component from 'vue-class-component'
import { Prop } from 'vue-property-decorator'
import Vue from 'vue'

@Component
export default class BooleanIcon extends Vue {
  @Prop({ type: Boolean, default: false }) model!: boolean
}
</script>
