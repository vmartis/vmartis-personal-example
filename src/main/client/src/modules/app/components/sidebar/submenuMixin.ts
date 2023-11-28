import Vue from 'vue'
import Component from 'vue-class-component'
import { State } from 'vuex-class'

@Component
export default class ConfirmMixin extends Vue {
  pageGroup: string | null = null

  @State('pageGroup', { namespace: 'app' }) currentPageGroup?: string

  get isOpen () {
    return this.currentPageGroup === this.pageGroup
  }
}
