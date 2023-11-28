<template>
  <box :collapsible="collapsible" :collapsed="collapsed">
    <template #header>
      <h5><i class="fas fa-user-friends pr-1"></i> {{ 'contacts.label' | translate }}</h5>
    </template>
    <template #tools v-if="editable">
      <b-dropdown variant="link" toggle-class="text-decoration-none" no-caret>
        <template v-slot:button-content>
          <i class="fas fa-bars text-muted"></i>
        </template>
        <b-dropdown-item-button @click="createShow"><i class="fas fa-plus text-success"></i> {{ 'contact.add.label' | translate }}
        </b-dropdown-item-button>
        <b-dropdown-item-button @click="showInactivated = !showInactivated">
          <i class="fas fa-toggle-off"
             :class="{ 'fa-toggle-off': !showInactivated, 'fa-toggle-on text-success':  showInactivated}"></i>
          {{ 'common.inactive.show' | translate }}
        </b-dropdown-item-button>
      </b-dropdown>
    </template>
    <template #body>
      <template v-for="(contact, index) in contacts">
        <contact-box :contact="contact" v-bind:key="contact.id" :mt="index > 0" :editable="editable"></contact-box>
      </template>
      <loading id="contact"></loading>
      <no-records :data="contacts" loading-id="company"></no-records>

      <contact-create-modal
        :show-modal="showCreateModal"
        :model="selectedItem"
        @close="createHide()"></contact-create-modal>
    </template>
  </box>
</template>
<script lang="ts">
import Vue from 'vue'
import Component from 'vue-class-component'
import box from '@/modules/common/components/box.vue'
import contactCreateModal from '@/modules/contact/components/contactCreateModal.vue'
import Contact from '@/modules/contact/domain/contact'
import _ from 'lodash'
import loading from '@/modules/common/components/loading.vue'
import contactBox from '@/modules/contact/components/contactBox.vue'
import { Prop } from 'vue-property-decorator'
import { State } from 'vuex-class'
import Company from '@/modules/company/domain/company'
import NoRecords from '@/modules/common/components/noRecords.vue'

@Component({
  components: { NoRecords, box, contactBox, contactCreateModal, loading }
})
export default class PartnerContactsBox extends Vue {
  showInactivated = false
  selectedItem?: Contact | null = null
  showCreateModal: boolean = false

  @Prop({ type: Boolean, default: false }) collapsible!: boolean
  @Prop({ type: Boolean, default: false }) collapsed!: boolean
  @Prop({ type: Boolean, default: true }) editable!: boolean

  @State('selected', { namespace: 'company' }) partner?: Company

  get contacts () {
    if (this.showInactivated) {
      const all = this.$store.getters['contact/all']
      return _.orderBy(all, 'active', 'desc')
    } else {
      return this.$store.getters['contact/active']
    }
  }

  createShow () {
    this.selectedItem = new Contact({ subject: this.partner, active: true })
    this.showCreateModal = true
  }

  createHide () {
    this.showCreateModal = false
  }
}
</script>
