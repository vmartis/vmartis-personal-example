<template>
  <box>
    <template #header v-if="company">
      <h5 class="box-title">{{ company.name }}</h5>
    </template>
    <template #body>
      <div class="row" v-if="company">
        <div class="col-md-2">
          <dl>
            <dt>{{ 'company.company-id.label' | translate }}</dt>
            <dd>{{ company.companyId }}</dd>
          </dl>
        </div>
        <div class="col-md-2">
          <dl>
            <dt>{{ 'company.vat-id.label' | translate }}</dt>
            <dd v-if="company.vatId">{{ company.vatId }}</dd>
            <dd v-else>{{ 'company.vat-payer.not.label' | translate }}</dd>
          </dl>
        </div>
        <div class="col-md-2">
          <dl>
            <dt>{{ 'company.vat-id-local.label' | translate }}</dt>
            <dd>{{ company.vatIdLocal | optional }}</dd>
          </dl>
        </div>
        <div class="col-md-3">
          <dl>
            <dt>{{ 'company.address.label.short' | translate }}</dt>
            <dd>{{ company.address.format() }}</dd>
          </dl>
        </div>
        <div class="col-md-3">
          <dl>
            <dt>{{ 'company.default-currency.label' | translate }}</dt>
            <dd>{{ company.defaultCurrency | codeListValue(currencies) }}</dd>
          </dl>
        </div>
          <button class="btn btn-link bottom-right" type="button" @click="updateShow()" :title="'common.edit' | translate" v-b-tooltip.bottom>
            <i class="fas fa-pencil-alt text-muted"></i>
          </button>
      </div>
      <company-create-modal
        :show-modal="showCreateModal"
        :model="selectedItem"
        :show-inactivate="false"
        :show-region="false"
        :show-categories="false"
        :show-for-order="false"
        @close="createHide()"></company-create-modal>
      <button v-if="!company" class="btn btn-success" type="button" @click="createShow()" :title="'common.add' | translate">
        <i class="fa fa-plus"></i></button>
      <loading id="company"></loading>
    </template>
  </box>
</template>
<script lang="ts">

import Address from '@/modules/common/domain/address'
import box from '@/modules/common/components/box.vue'
import Company from '@/modules/company/domain/company'
import companyCreateModal from '@/modules/company/components/companyCreateModal.vue'
import { CompanyRole } from '@/modules/company/type/companyRole'
import Component from 'vue-class-component'
import Currency from '@/modules/currency/domain/currency'
import loading from '@/modules/common/components/loading.vue'
import page from '@/modules/common/components/page.vue'
import { State } from 'vuex-class'
import Vue from 'vue'

@Component({
  components: { page, box, companyCreateModal, loading }
})
export default class CompanyOwnedDetail extends Vue {
  selectedItem?: Company | null = null
  showCreateModal: boolean = false
  @State('owned', { namespace: 'company' }) company? : Company
  @State('items', { namespace: 'currency' }) currencies? : Currency[]

  createShow () {
    this.selectedItem = new Company({ role: CompanyRole.OWNED, address: new Address(), active: true })
    this.showCreateModal = true
  }

  updateShow () {
    this.selectedItem = this.company
    this.showCreateModal = true
  }

  createHide () {
    this.showCreateModal = false
  }

  async created () {
    await this.$store.dispatch('currency/getAll')
    await this.$store.dispatch('company/getOwned')
  }
}
</script>
