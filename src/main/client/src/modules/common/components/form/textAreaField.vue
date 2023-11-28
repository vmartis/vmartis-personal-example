<template>
  <div class="form-group">
    <label v-if="label" class="col-form-label" :class="{ required: requiredVal }">{{ label | translate }}</label>
    <validation-provider :rules="validation || validationObj" v-slot="{ errors }">
        <textarea :name="fieldId"
                  :id="fieldId"
                  v-model="innerValue"
                  :maxlength="maxlength"
                  class="form-control"
                  :class="{ 'is-invalid': errors.length, ...cssClass }"></textarea>
      <p class="text-danger" v-if="errors.length">{{ errors[0] }}</p>
    </validation-provider>
  </div>
</template>
<script lang="ts">
import Vue from 'vue'
import Component from 'vue-class-component'
import { ValidationProvider } from 'vee-validate'
import { Prop, Watch } from 'vue-property-decorator'

@Component({
  components: { ValidationProvider }
})
export default class TextAreaField extends Vue {
  innerValue?: string | null = null

  @Prop({ type: String, required: false }) fieldId?: string
  @Prop({ type: String }) label?: string
  @Prop({ type: Number, required: false, default: Number.MAX_VALUE }) maxlength?: number
  @Prop({ type: String, required: false }) cssClass?: string
  @Prop({ type: String }) validation!: string
  @Prop({ type: Object, default: () => {} }) validationObj!: any
  @Prop({ type: String, default: '' }) value!: string

  get requiredVal () {
    return ((this.validation || '').indexOf('required') >= 0) || (this.validationObj || {}).required
  }

  @Watch('innerValue')
  onInnerValueChange (value: string) {
    this.$emit('input', value)
  }

  @Watch('value')
  onValueChange (value: string) {
    this.innerValue = value
  }

  created () {
    if (this.value) {
      this.innerValue = this.value
    }
  }
}
</script>
