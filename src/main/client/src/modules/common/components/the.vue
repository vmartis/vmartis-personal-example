<template>
  <th @click.prevent="sort()" role="button">{{ label | translate }}
    <i class="float-right fas pt-1 ml-2" :class="{ 'fa-sort': !active, 'fa-sort-amount-down-alt': active && sortData.asc, 'fa-sort-amount-down': active && !sortData.asc }"></i>
  </th>
</template>
<script lang="ts">

import Component from 'vue-class-component'
import { Prop } from 'vue-property-decorator'
import SortData from '@/modules/common/mixins/sortData'
import Vue from 'vue'

@Component
export default class The extends Vue {
  @Prop({ type: String, required: true }) label!: string
  @Prop({ type: String, required: true }) property!: string
  @Prop({ type: Object, required: true }) sortData!: SortData

  get active () {
    return this.sortData.by === this.property
  }

  sort () {
    const asc = this.sortData.by === this.property ? !this.sortData.asc : this.sortData.asc
    this.$emit('sort',
      {
        by: this.property,
        asc: asc
      }
    )
  }
}
</script>
