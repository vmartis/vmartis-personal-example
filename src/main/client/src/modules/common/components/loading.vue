<template>
  <div class="text-center m-3" v-if="loading">
    <b-spinner :variant="variant" :label="'common.loading' | translate"></b-spinner>
  </div>
</template>
<script lang="ts">
import _ from 'lodash'
import Component from 'vue-class-component'
import { Prop } from 'vue-property-decorator'
import { State } from 'vuex-class'
import AppState from '@/modules/app/store/AppState'
import Vue from 'vue'

/**
 * "id" has to be a value in initial state of app.loadingData array.
 */
@Component
export default class Loading extends Vue {
  @Prop() id!: string | string []
  @Prop({ type: String, default: 'primary' }) variant!: string
  @State('app') appState!: AppState

  get loading () {
    if (_.isString(this.id)) {
      return this.appState.loadingData.indexOf(this.id) >= 0
    } else if (_.isArray(this.id)) {
      return !!_.intersection(this.appState.loadingData, this.id).length
    } else {
      return false
    }
  }
}
</script>
