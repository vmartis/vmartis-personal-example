<template>
  <b-alert
    :show="!loading && emptyData"
    class="text-center"
    variant="warning">
    {{ text | translate }}
  </b-alert>
</template>
<script lang="ts">
import _ from 'lodash'
import Component from 'vue-class-component'
import { State } from 'vuex-class'
import { Prop } from 'vue-property-decorator'
import Vue from 'vue'

@Component
export default class NoRecords extends Vue {
  @State('loadingData', { namespace: 'app' }) loadingData!: Array<string>
  @Prop({ type: [Array, Object] }) data?: Array<any> | Object
  @Prop() loadingId!: string | string []

  @Prop({ type: String, default: 'common.data.not-records' }) text?: string

  get emptyData () {
    return !this.data || _.isEmpty(this.data)
  }

  get loading () {
    if (_.isString(this.loadingId)) {
      return this.loadingData.indexOf(this.loadingId) >= 0
    } else if (_.isArray(this.loadingId)) {
      return !!_.intersection(this.loadingData, this.loadingId).length
    } else {
      return false
    }
  }
}

</script>
