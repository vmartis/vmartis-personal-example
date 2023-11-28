<template>
  <b-card>
    <template #header>
      <h5 v-if="partner" :class="{ inactive: !partner.active }" :title="(!partner.active ? 'common.inactive.label' : null) | translate" v-b-tooltip>{{ partner.name }}</h5>
    </template>
    <company-create-modal
      :show-modal="showCreateModal"
      :model="selectedPartner"
      :show-default-currency="false"
      :show-vat-local="false"
      :show-address="false"
      :show-for-order="false"
      :show-separator="false"
      @close="createHide()"></company-create-modal>
    <div v-if="partner">
      <div class="row">
        <div class="col-md-12" v-if="partner.web">
          <p>
            <i class="fas fa-globe-africa text-muted pr-1"></i> <a :href="partner.web | link" target="_blank" rel="external">{{ partner.web | url }}</a>
          </p>
        </div>
      </div>
      <div class="row">
        <div class="col-md-6">
          <dl>
            <dt>{{ 'company.company-id.label' | translate }}</dt>
            <dd>{{ partner.companyId }}</dd>
          </dl>
        </div>
        <div class="col-md-6">
          <dl>
            <dt>{{ 'company.vat-id.label' | translate }}</dt>
            <dd v-if="partner.vatId">{{ partner.vatId }}</dd>
            <dd v-else>{{ 'company.vat-payer.not.label' | translate }}</dd>
          </dl>
        </div>
      </div>
      <div class="row">
        <div class="col-md-10">
          <b-badge v-bind:key="category.id" v-for="category in partner.categories" class="mr-1" :style="`background-color: ${category.color}`">{{ category.label }}</b-badge>
        </div>
        <div class="col-md-2"></div>
      </div>
      <button class="btn btn-link bottom-right" type="button" @click="updateShow()" :title="'common.edit' | translate" v-b-tooltip.bottom><i class="fas fa-pencil-alt text-muted"></i></button>
    </div>
    <div v-else>
      <loading id="company"></loading>
    </div>
  </b-card>
</template>
<script lang="ts">

import Company from '@/modules/company/domain/company'
import companyCreateModal from '@/modules/company/components/companyCreateModal.vue'
import Component from 'vue-class-component'
import loading from '@/modules/common/components/loading.vue'
import { State } from 'vuex-class'
import Vue from 'vue'

@Component({
  components: { companyCreateModal, loading }
})
export default class PartnerInfo extends Vue {
  showCreateModal: boolean = false
  selectedPartner?: Company | null = null

  @State('selected', { namespace: 'company' }) partner!: Company

  updateShow () {
    this.selectedPartner = this.partner
    this.showCreateModal = true
  }

  createHide () {
    this.selectedPartner = null
    this.showCreateModal = false
  }
}
</script>
