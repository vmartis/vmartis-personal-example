<template>
  <div class="border border-gray p-1 mt-3" :class="{inactive: !companyBranch.active}">
    <div class="row">
      <div class="col-12">
        <strong>{{ companyBranch.name }}</strong>
      </div>
    </div>
    <div class="row">
      <div class="col-md-12 formatted-text pr-5">{{ companyBranch.address.format(true) }}</div>
    </div>
    <div class="row">
      <div class="col-10">
        <i class="fas fa-truck pr-2" v-if="companyBranch.forOrder" v-b-tooltip :title="'company.for-order.info' | translate"></i>
        <b-badge variant="secondary" :style="`background-color: ${companyBranch.address.region.color}`" v-if="companyBranch.address.region">{{ companyBranch.address.region.label }}</b-badge>
      </div>
    </div>
    <div class="row">
      <div class="col-12">
        <button class="btn btn-link bottom-right mr-1" type="button" @click="updateShow"
                :title="'common.edit' | translate" v-b-tooltip.bottom>
          <i class="fas fa-pencil-alt text-muted"></i></button>
      </div>
    </div>
    <company-branch-create-modal
      :show-modal="showCreateModal"
      :model="selectedItem"
      :show-inactivate="false"
      @close="updatedHide"></company-branch-create-modal>
  </div>
</template>
<script lang="ts">
import Component from 'vue-class-component'
import CompanyBranch from '@/modules/company/domain/companyBranch'
import companyBranchCreateModal from '@/modules/company/components/companyBranchCreateModal.vue'
import { Prop } from 'vue-property-decorator'
import Vue from 'vue'

@Component({
  components: { companyBranchCreateModal }
})
export default class PartnerAddressesBox extends Vue {
  selectedItem: CompanyBranch | null = null
  showCreateModal: boolean = false

  @Prop({ type: String }) label?: string
  @Prop({ type: CompanyBranch, required: true }) companyBranch!: CompanyBranch

  updateShow () {
    this.selectedItem = this.companyBranch
    this.showCreateModal = true
  }

  updatedHide () {
    this.showCreateModal = false
    this.selectedItem = null // need to be here to again set fresh copy of model in createModal
  }
}
</script>
