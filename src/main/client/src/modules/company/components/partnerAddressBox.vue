<template>
  <div>
    <company-create-modal
      :show-modal="showCreateModal"
      :model="selectedPartner"
      :show-name="false"
      :show-company-id="false"
      :show-vat-id="false"
      :show-vat-local="false"
      :show-web="false"
      :show-categories="false"
      :show-default-currency="false"
      :show-inactivate="false"
      :show-separator="false"
      @close="createHide()"></company-create-modal>
    <div class="border border-gray p-1" v-if="partner">
      <div class="row">
        <div class="col-12">
          <strong>{{ 'company.address.label.short' | translate }}</strong>
        </div>
      </div>
      <div class="row">
        <div class="col-md-12 formatted-text pr-5">{{ partner.address.format(true) }}</div>
      </div>
      <div class="row">
        <div class="col-10">
          <i class="fas fa-truck pr-2" v-if="partner.forOrder" v-b-tooltip
             :title="'company.for-order.info' | translate"></i>
          <b-badge variant="secondary" :style="`background-color: ${partner.address.region.color}`"
                   v-if="partner.address.region">{{ partner.address.region.label }}
          </b-badge>
        </div>
      </div>
      <div class="row">
        <div class="col-12">
          <button class="btn btn-link bottom-right mr-1" type="button"
                  @click="updateShow()"
                  :title="'common.edit' | translate" v-b-tooltip.bottom>
            <i class="fas fa-pencil-alt text-muted"></i></button>
        </div>
      </div>
    </div>
    <loading id="company"></loading>
  </div>
</template>
<script lang="ts">
import Component from 'vue-class-component'
import Company from '../../company/domain/company'
import companyCreateModal from '@/modules/company/components/companyCreateModal.vue'
import loading from '@/modules/common/components/loading.vue'
import { State } from 'vuex-class'
import Vue from 'vue'

@Component({
  components: { companyCreateModal, loading }
})
export default class PartnerAddressesBox extends Vue {
  showCreateModal: boolean = false
  selectedPartner?: Company | null = null
  @State('selected', { namespace: 'company' }) partner!: Company

  updateShow () {
    this.selectedPartner = this.partner
    this.showCreateModal = true
  }

  createHide () {
    this.showCreateModal = false
    this.selectedPartner = null // need to be here to again set fresh copy of model in createModal
  }
}
</script>
