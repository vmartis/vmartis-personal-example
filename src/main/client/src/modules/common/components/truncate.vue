<template>
  <div :class="className">
    <template v-if="!content"></template>
    <template v-else-if="show || content.length <= minLength" :class="{ 'formatted-text': show}">
      {{ content }}
    </template>
    <template v-else>
      {{ content | truncate(minLength) }} <a @click.prevent.stop="show = true" role="button" class="text-primary">{{ 'common.show.all.2' | translate }}</a>
    </template>
  </div>
</template>

<script lang="ts">
import Vue from 'vue'
import { Prop } from 'vue-property-decorator'
import Component from 'vue-class-component'

@Component
export default class Truncate extends Vue {
  show = false

  @Prop({ type: String }) content?: string
  @Prop({ type: Number, default: 100 }) minLength?: number
  @Prop({ type: String, default: '' }) className!: string
}
</script>
