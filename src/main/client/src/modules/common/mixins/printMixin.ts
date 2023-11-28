import Component from 'vue-class-component'
import Vue from 'vue'

@Component
export default class PrintMixin extends Vue {
  print () {
    // print need to be postponed to apply rendering
    this.$nextTick(() => {
      window.print()
    })
  }
}
