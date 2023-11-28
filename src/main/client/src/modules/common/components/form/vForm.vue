<template>
  <validation-observer ref="form" tag="form">
    <slot></slot>
  </validation-observer>
</template>
<script lang="ts">
import Vue from 'vue'
import { ValidationObserver } from 'vee-validate'
import Component from 'vue-class-component'
import { Prop } from 'vue-property-decorator'

@Component({
  components: { ValidationObserver }
})
export default class VForm extends Vue {
  @Prop({ type: String, default: 'form' }) refName?: string

  validate (): Function {
    return (this.$refs.form as any).validate()
  }
}
</script>
