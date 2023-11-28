<template>
  <box>
    <template #header>
      <h5><i class="fas fa-map-marker pr-1"></i> {{ 'addresses.label' | translate }}</h5>
    </template>
    <template #tools>
      <b-dropdown variant="link" toggle-class="text-decoration-none" no-caret>
        <template v-slot:button-content>
          <i class="fas fa-bars text-muted"></i>
        </template>
        <b-dropdown-item-button @click="createShow"><i class="fas fa-plus text-success"></i> {{ 'address.add.label' | translate }}
        </b-dropdown-item-button>
        <b-dropdown-item-button @click="showInactivated = !showInactivated">
          <i class="fas fa-toggle-off"
             :class="{ 'fa-toggle-off': !showInactivated, 'fa-toggle-on text-success':  showInactivated}"></i>
          {{ 'common.inactive.show' | translate }}
        </b-dropdown-item-button>
      </b-dropdown>
    </template>
    <template #body>
      <partner-address-box></partner-address-box>
      <template v-for="branch in branches">
        <partner-branch-address-box :company-branch="branch" v-bind:key="branch.id"></partner-branch-address-box>
      </template>
      <loading id="company-branch"></loading>

      <company-branch-create-modal
        :show-modal="showCreateModal"
        :model="selectedItem"
        :show-inactivate="false"
        @close="createHide()"></company-branch-create-modal>
    </template>
  </box>
</template>
<script lang="ts">
import _ from 'lodash'
import Vue from 'vue'
import Component from 'vue-class-component'
import box from '@/modules/common/components/box.vue'
import { State } from 'vuex-class'
import Company from '@/modules/company/domain/company'
import partnerAddressBox from './partnerAddressBox.vue'
import partnerBranchAddressBox from './partnerBranchAddressBox.vue'
import EntityFetchParams from '@/modules/common/store/entityFetchParams'
import companyBranchCreateModal from '@/modules/company/components/companyBranchCreateModal.vue'
import { Watch } from 'vue-property-decorator'
import CompanyBranch from '@/modules/company/domain/companyBranch'
import Address from '@/modules/common/domain/address'
import loading from '@/modules/common/components/loading.vue'

@Component({
  components: { box, companyBranchCreateModal, loading, partnerAddressBox, partnerBranchAddressBox }
})
export default class PartnerAddressesBox extends Vue {
  showInactivated = false
  selectedItem?: CompanyBranch | null = null
  showCreateModal: boolean = false
  @State('selected', { namespace: 'company' }) partner!: Company

  get branches () {
    if (this.showInactivated) {
      const all = this.$store.getters['companyBranch/all']
      return _.orderBy(all, 'active', 'desc')
    } else {
      return this.$store.getters['companyBranch/active']
    }
  }

  createShow () {
    this.selectedItem = new CompanyBranch({ company: this.partner, address: new Address(), active: true })
    this.showCreateModal = true
  }

  createHide () {
    this.showCreateModal = false
  }

  @Watch('partner')
  onPartnerChanged (partner: Company) {
    if (partner) {
      this.$store.dispatch('companyBranch/getAll', new EntityFetchParams(true, { 'company-id': partner.id }))
    }
  }

  async created () {
    await this.$store.dispatch('companyBranch/clearAll')
  }
}
</script>
