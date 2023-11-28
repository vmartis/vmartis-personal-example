<template>
  <div>
    <b-modal
      v-model="show"
      :no-close-on-backdrop="true"
      :title="title | translate"
      @hide="onClose"
      :size="size"
      id="form-modal"
      :footer-class="footerClass">
      <v-form ref="form">
        <div class="row">
          <div class="col-12">
            <slot></slot>
          </div>
        </div>
      </v-form>
      <template #modal-footer>
        <slot name="footer"></slot>
        <button type="button" class="btn btn-success" :disabled="protected()" @click.prevent="submit($refs.form)">{{ okButtonTitle | translate }}</button>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts">
import _ from 'lodash'
import eventBus from '@/modules/common/services/eventBus'
import Component, { mixins } from 'vue-class-component'
import { Prop, Watch } from 'vue-property-decorator'
import vForm from '@/modules/common/components/form/vForm.vue'
import { BModal, BvModalEvent } from 'bootstrap-vue'
import submitProtectionMixin from '@/modules/common/mixins/submitProtectionMixin'

@Component({
  components: { BModal, vForm }
})
export default class FormModal extends mixins(submitProtectionMixin) {
  show: boolean = false

  @Prop({ type: Boolean, default: false }) showModal!: boolean
  @Prop({ type: String, default: '' }) title!: String
  @Prop({ type: String, default: 'md' }) size!: String
  @Prop({ type: String, default: '' }) footerClass!: String
  @Prop({ type: String, default: 'common.save' }) okButtonTitle!: String
  @Prop(Object) model!: any

  async submit () {
    await this.protect()
    const isValid = await (this.$refs.form as any).validate()
    if (isValid) {
      this.$emit('submit')
    } else {
      await this.unprotect()
    }
  }

  onClose (event?: BvModalEvent) {
    if (_.isNil(event) || event.trigger === 'cancel' || event.trigger === 'esc' || event.trigger === 'headerclose') {
      this.unprotect()
      this.$emit('close')
    }
  }

  @Watch('showModal', { immediate: true })
  onShowModalChange (showModal: boolean) {
    this.show = showModal
  }

  created () {
    eventBus.$on('submit', () => {
      if (this.show) {
        this.submit()
      }
    })
    eventBus.$on('close', () => {
      if (this.show) {
        this.onClose()
      }
    })
    // force close for direct change of route without closing window
    eventBus.$on('close-force', () => {
      if (this.show) {
        this.onClose()
      }
    })
  }
}
</script>
