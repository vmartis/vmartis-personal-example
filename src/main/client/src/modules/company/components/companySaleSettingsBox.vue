<template>
  <div>
    <company-sale-settings-create-modal
      :show-modal="showCreateModal"
      :model="selectedItem"
      @close="createHide()">
    </company-sale-settings-create-modal>
    <loading id="company"></loading>
    <div class="row" v-if="company">
      <div class="col-md-4">
        <dl>
          <dt>{{ 'company.sale-settings.payment-type.label' | translate }}</dt>
          <dd>{{ company.saleSettings.paymentType ? company.saleSettings.paymentType.label : '' }}</dd>
        </dl>
      </div>
      <div class="col-md-3">
        <dl>
          <dt>{{ 'company.sale-settings.delivery-note-enabled.label' | translate }}</dt>
          <dd>
            <boolean-icon v-if="company.saleSettings" :model="company.saleSettings.deliveryNoteEnabled"></boolean-icon>
          </dd>
        </dl>
      </div>
      <div class="col-md-3">
        <dl>
          <dt>{{ 'company.sale-settings.due-date-offset.label' | translate }}</dt>
          <dd v-if="company.saleSettings.dueDateOffset">{{ company.saleSettings.dueDateOffset }} {{ 'common.days' |translate }}</dd>
        </dl>
      </div>
      <div class="col-md-2">
        <button class="btn btn-link bottom-right" type="button" @click="updateShow()"
                :title="'common.edit' | translate" v-b-tooltip.bottom>
          <i class="fas fa-pencil-alt text-muted"></i></button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import booleanIcon from '@/modules/common/components/icon/booleanIcon.vue'
import Company from '@/modules/company/domain/company'
import companySaleSettingsCreateModal from '@/modules/company/components/companySaleSettingsCreateModal.vue'
import Component from 'vue-class-component'
import loading from '@/modules/common/components/loading.vue'
import { State } from 'vuex-class'
import Vue from 'vue'

@Component({
  components: { booleanIcon, companySaleSettingsCreateModal, loading }
})
export default class CompanySaleSettingsBox extends Vue {
  showCreateModal: boolean = false
  selectedItem?: any | null = null

  @State('selected', { namespace: 'company' }) company!: Company

  updateShow () {
    this.selectedItem = this.company
    this.showCreateModal = true
  }

  createHide () {
    this.selectedItem = null
    this.showCreateModal = false
  }
}
</script>
