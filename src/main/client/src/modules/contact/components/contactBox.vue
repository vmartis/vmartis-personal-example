<template>
  <div class="border border-gray p-1" :class="{inactive: !contact.active, 'mt-3': mt}">
    <div class="row">
      <div class="col-12">
        <div class="row">
          <div class="col">{{ contact.name }}</div>
          <div class="col mr-1 text-right"><i v-if="contact.note" class="fas fa-info-circle text-blue" v-b-tooltip :title="contact.note"></i></div>
        </div>
        <div>
          <small class="text-muted" v-if="contact.position">{{ contact.position }}</small>
        </div>
      </div>
    </div>
    <div class="row" v-if="contact.phoneNumber">
      <div class="col-md-12"><i class="fas fa-phone-alt pr-2"></i> {{ contact.phoneNumber }}</div>
    </div>
    <div class="row" v-if="contact.email">
      <div class="col-md-12"><i class="fas fa-at pr-2"></i><a :href="'mailto:' + contact.email">{{ contact.email }}</a></div>
    </div>
    <div class="row" v-if="editable">
      <div class="col-12">
        <button class="btn btn-link bottom-right mr-1" type="button" @click="updateShow" :title="'common.edit' | translate" v-b-tooltip.bottom>
          <i class="fas fa-pencil-alt text-muted"></i>
        </button>
      </div>
    </div>
    <contact-create-modal
      :show-modal="showCreateModal"
      :model="selectedItem"
      :show-inactivate="false"
      @close="updatedHide"></contact-create-modal>
  </div>
</template>
<script lang="ts">
import Component from 'vue-class-component'
import { Prop } from 'vue-property-decorator'
import Contact from '@/modules/contact/domain/contact'
import contactCreateModal from '@/modules/contact/components/contactCreateModal.vue'
import Vue from 'vue'

@Component({
  components: { contactCreateModal }
})
export default class PartnerAddressesBox extends Vue {
  selectedItem?: Contact | null = null
  showCreateModal: boolean = false

  @Prop({ type: Boolean, required: false, default: true }) mt?: boolean
  @Prop({ type: Contact, required: true }) contact!: Contact
  @Prop({ type: Boolean, default: true }) editable!: boolean

  updateShow () {
    this.selectedItem = this.contact
    this.showCreateModal = true
  }

  updatedHide () {
    this.showCreateModal = false
    this.selectedItem = null // need to be here to again set fresh copy of model in createModal
  }
}
</script>
