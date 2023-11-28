<template>
  <div v-if="item">
    <form-modal
      v-if="item"
      :show-modal="show"
      :model="item"
      title="configuration.invoice.edit.label"
      footer-class="justify-content-right"
      @submit="onSubmit"
      @close="onClose">

      <div class="row form-row">
        <div class="col-md-8">
          <select-field
            :options="paymentMethods"
            v-model="item.invoice.defaultPaymentMethod"
            :codelist="true"
            field-id="defaultPaymentMethod"
            label="payment-method.label"></select-field>
        </div>
        <div class="col-md-4">
          <number-field
            v-model="item.invoice.defaultDueDateOffset"
            label="invoice.due.date.days.label"
            field-id="defaultDueDateOffset"
            :min="0"
            :step="10"
            validation="numeric|min_value:0"></number-field>
        </div>
      </div>
      <div class="row form-row">
        <div class="col-md-8">
          <select-field
            :options="bankAccounts"
            v-model="item.invoice.defaultBankAccount"
            :translate="false"
            field-id="defaultBankAccount"
            label="bank-account.label.short"></select-field>
        </div>
        <div class="col-md-4">
          <select-field
            :options="currencies"
            v-model="item.invoice.defaultCurrency"
            :codelist="true"
            :translate="false"
            field-id="defaultCurrency"
            label="currency.label"></select-field>
        </div>
      </div>
      <div class="row form-row">
        <div class="col-md-4">
          <div class="form-group">
            <label class="col-form-label">{{ 'configuration.invoice.logo.label' | translate }}</label>
            <div>
              <document-field :document="item.invoice.logoDocument" :editable="true" class="mb-3"
                              field-id="logoDocument" :image="true" :delete-confirm="false"
                              @delete-document="removeLogoDocument"/>
              <document-upload @document-uploaded="logoDocumentUploaded($event)" :allowed-types="imageFileTypes"
                               :max-size="imageMaxSize"></document-upload>
            </div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="form-group">
            <label class="col-form-label">{{ 'configuration.invoice.stamp.label' | translate }}</label>
            <div>
              <document-field :document="item.invoice.stampDocument" :editable="true" class="mb-3"
                              field-id="stampDocument" :image="true" :delete-confirm="false"
                              @delete-document="removeStampDocument"/>
              <document-upload @document-uploaded="stampDocumentUploaded($event)" :allowed-types="imageFileTypes"
                               :max-size="imageMaxSize"></document-upload>
            </div>
          </div>
        </div>
        <div class="col-md-4">
          <select-field
            :options="vatCalculationTypes"
            :clearable="false"
            :codelist="true"
            v-model="item.invoice.vatCalculationType"
            field-id="vatCalculationType"
            label="configuration.invoice.vat.calculation-type.label"></select-field>
        </div>
      </div>
    </form-modal>
  </div>
</template>

<script lang="ts">
import BankAccount from '@/modules/bank/account/domain/bankAccount'
import Component, { mixins } from 'vue-class-component'
import Configuration from '@/modules/configuration/domain/configuration'
import createModal from '@/modules/common/components/createModal'
import Currency from '@/modules/currency/domain/currency'
import Document from '@/modules/document/domain/document'
import DocumentField from '@/modules/common/components/form/documentField.vue'
import DocumentUpload from '@/modules/document/components/documentUpload.vue'
import EntityFetchParams from '@/modules/common/store/entityFetchParams'
import formModal from '@/modules/common/components/formModal.vue'
import { Getter } from 'vuex-class'
import checkboxField from '@/modules/common/components/form/checkboxField.vue'
import NumberField from '@/modules/common/components/form/numberField.vue'
import paymentMethods from '@/modules/common/values/paymentMethods'
import selectField from '@/modules/common/components/form/selectField.vue'
import textField from '@/modules/common/components/form/textField.vue'
import { IMAGE_FILE_MAX_SIZE, IMAGE_FILE_SUPPORTED_EXTENSIONS } from '@/config'
import { vatCalculationTypes } from '@/modules/configuration/type/VatCalculationType'
import { Watch } from 'vue-property-decorator'

@Component({
  components: { DocumentUpload, DocumentField, NumberField, selectField, textField, checkboxField, formModal }
})
export default class InvoiceConfigCreateModal extends mixins<createModal<Configuration>>(createModal) {
  moduleName = 'configuration'
  paymentMethods = paymentMethods
  imageFileTypes = IMAGE_FILE_SUPPORTED_EXTENSIONS
  imageMaxSize = IMAGE_FILE_MAX_SIZE
  vatCalculationTypes = vatCalculationTypes

  @Getter('active', { namespace: 'bankAccount' }) bankAccounts?: Array<BankAccount>
  @Getter('validItems', { namespace: 'currency' }) currencies!: Array<Currency>

  @Watch('item.invoice.defaultBankAccount', { deep: true, immediate: false })
  onBankAccountChange (bankAccount: BankAccount) {
    if (this.modelInitialized && bankAccount) {
      this.item!.invoice.defaultCurrency = bankAccount.currency
    }
  }

  logoDocumentUploaded (document: Document) {
    this.item!.invoice.logoDocument = document
  }

  stampDocumentUploaded (document: Document) {
    this.item!.invoice.stampDocument = document
  }

  removeLogoDocument () {
    this.item!.invoice.logoDocument = null
  }

  removeStampDocument () {
    this.item!.invoice.stampDocument = null
  }

  created () {
    this.$store.dispatch('currency/getAll')
    this.$store.dispatch('bankAccount/getAll', new EntityFetchParams(true, { ownershipped: true }))
  }
}
</script>
