<template>
  <b-card>
    <template #header>
      <h5>{{ 'configuration.invoice.label' | translate }}</h5>
    </template>
    <invoice-config-create-modal
      :show-modal="showCreateModal"
      :model="selectedItem"
      :show-inactivate="false"
      @close="createHide()">
    </invoice-config-create-modal>
    <div class="row">
      <div class="col-md-3">
        <dl>
          <dt>{{ 'payment-method.label' | translate }}</dt>
          <dd v-if="configuration">{{ configuration.invoice.defaultPaymentMethod | codeListValue(paymentMethods, true) }}
          <dd v-else>-</dd>
        </dl>
      </div>
      <div class="col-md-3">
        <dl>
          <dt>{{ 'invoice.due.date.days.label' | translate }}</dt>
          <dd v-if="configuration">{{ configuration.invoice.defaultDueDateOffset }}
          <dd v-else>-</dd>
        </dl>
      </div>
      <div class="col-md-3">
        <dl>
          <dt>{{ 'currency.label' | translate }}</dt>
          <dd v-if="configuration">{{ configuration.invoice.defaultCurrency | codeListValue(currencies) }}
          <dd v-else>-</dd>
        </dl>
      </div>
      <div class="col-md-3">
        <dl>
          <dt>{{ 'bank-account.label.short' | translate }}</dt>
          <dd v-if="configuration && configuration.invoice.defaultBankAccount">{{ configuration.invoice.defaultBankAccount.name }}
          <dd v-else>-</dd>
        </dl>
      </div>
    </div>
    <div class="row">
      <div class="col-md-3">
        <dl>
          <dt>{{ 'configuration.invoice.logo.label' | translate }}</dt>
          <dd><document-field v-if="configuration" :document="configuration.invoice.logoDocument" :editable="false" field-id="document" :image="true" :preview="true"/></dd>
        </dl>
      </div>
      <div class="col-md-3">
        <dl>
          <dt>{{ 'configuration.invoice.stamp.label' | translate }}</dt>
          <dd><document-field  v-if="configuration" :document="configuration.invoice.stampDocument" :editable="false" field-id="document" :image="true" :preview="true"/></dd>
        </dl>
      </div>
      <div class="col-md-3">
        <dl>
          <dt>{{ 'configuration.invoice.vat.calculation-type.label' | translate }}</dt>
          <dd> {{ configuration.invoice.vatCalculationType | codeListValue(vatCalculationTypes, true) }}</dd>
        </dl>
      </div>
    </div>
    <button class="btn btn-link bottom-right" type="button" @click="createShow()" :title="'common.edit' | translate" v-b-tooltip.bottom>
      <i class="fas fa-pencil-alt text-muted"></i>
    </button>
  </b-card>
</template>

<script lang="ts">
import _ from 'lodash'
import box from '@/modules/common/components/box.vue'
import Component from 'vue-class-component'
import Configuration from '@/modules/configuration/domain/configuration'
import Currency from '@/modules/currency/domain/currency'
import documentField from '@/modules/common/components/form/documentField.vue'
import { Getter, State } from 'vuex-class'
import invoiceConfigCreateModal from '@/modules/configuration/components/invoiceConfigCreateModal.vue'
import InvoiceSetting from '@/modules/configuration/domain/invoiceSetting'
import paymentMethods from '@/modules/common/values/paymentMethods'
import selectField from '@/modules/common/components/form/selectField.vue'
import { vatCalculationTypes } from '@/modules/configuration/type/VatCalculationType'
import Vue from 'vue'

@Component({
  components: { box, documentField, selectField, invoiceConfigCreateModal }
})
export default class InvoiceConfigInfoBox extends Vue {
  paymentMethods = paymentMethods
  vatCalculationTypes = vatCalculationTypes
  selectedItem?: Configuration | null = null
  showCreateModal: boolean = false

  @State('items', { namespace: 'currency' }) currencies?: Currency[]
  @Getter('first', { namespace: 'configuration' }) configuration?: Configuration

  createShow () {
    this.selectedItem = _.cloneDeep(this.configuration) || new Configuration({ invoice: new InvoiceSetting({}) })
    this.showCreateModal = true
  }

  createHide () {
    this.showCreateModal = false
  }

  created () {
    this.$store.dispatch('currency/getAll')
  }
}
</script>
