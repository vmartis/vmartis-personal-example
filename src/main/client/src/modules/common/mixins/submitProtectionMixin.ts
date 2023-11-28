import Vue from 'vue'
import Component from 'vue-class-component'
import { State } from 'vuex-class'
import AppState from '@/modules/app/store/AppState'

@Component
export default class SubmitProtectionMixin extends Vue {
  @State('app') appState!: AppState

  protected () {
    return this.appState.submitProtection
  }

  async protect () {
    await this.$store.dispatch('app/protect')
  }

  async unprotect () {
    await this.$store.dispatch('app/unprotect')
  }
}
