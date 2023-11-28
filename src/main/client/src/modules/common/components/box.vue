<template>
  <div ref="card" class="card" :class="{'collapsed-card' : collapsed}">
    <div class="card-header" :class="{ 'collapsible': collapsible }" v-if="hasHeader"
         v-bind:data-widget="collapsible ? 'collapse' : ''">
      <div class="card-title">
        <slot name="header"></slot>
      </div>

      <div class="card-tools">
        <slot name="tools"></slot>
        <button v-show="collapsible"  type="button" class="btn btn-tool" data-card-widget="collapse"><i class="fas" :class="collapseIcon"></i>
        </button>
      </div>
    </div>
    <div class="card-body" v-bind:style="style" v-if="hasBody">
      <slot name="body"></slot>
    </div>
    <div class="card-footer" v-if="hasFooter" :class="{'border-top-0': !this.hasHeader && !this.hasBody}">
      <slot name="footer"></slot>
    </div>
  </div>
</template>
<script lang="ts">
import Vue from 'vue'
import Component from 'vue-class-component'
import { Prop } from 'vue-property-decorator'

@Component
export default class Box extends Vue {
  show = true
  style = ''
  collapseIcon = 'fa-minus'

  @Prop({ type: Boolean, default: false }) collapsible!: boolean
  @Prop({ type: Boolean, default: false }) collapsed!: boolean

  get hasHeader (): boolean {
    return !!this.$slots.header
  }

  get hasFooter (): boolean {
    return !!this.$slots.footer
  }

  get hasBody (): boolean {
    return !!this.$slots.body
  }

  mounted () {
    if (this.collapsed) {
      this.style = 'display: none;'
      this.collapseIcon = 'fa-plus'
    }
    const element: any = $(this.$refs.card)
    element.CardWidget({ collapseIcon: 'fa-chevron-right', expandIcon: 'fa-chevron-down' })
  }
}
</script>
