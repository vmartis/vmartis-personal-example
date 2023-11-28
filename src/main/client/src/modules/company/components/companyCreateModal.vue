<template>
  <div v-if="item">
    <form-modal
      v-if="item"
      :show-modal="show"
      :model="item"
      :title="isEdit() ? 'company.edit.label' : 'company.create.label'"
      :footer-class="isEdit() && showInactivate ? 'justify-content-between' : ''"
      @submit="onSubmit"
      @close="onClose">
      <div class="row form-row">
        <div class="col-12">
          <text-field field-id="name"
                      v-if="showName"
                      label="common.name"
                      placeholder="common.name.placeholder"
                      v-model="item.name"
                      :maxlength="100"
                      validation="required|min:3|max:100"></text-field>
        </div>
      </div>
      <div class="row form-row">
        <div class="col-6">
          <text-field field-id="companyId"
                      v-if="showCompanyId"
                      label="company.company-id.label"
                      placeholder="company.company-id.placeholder"
                      v-model="item.companyId"
                      :maxlength="8"
                      validation="required|digits:8"></text-field>
        </div>
        <div class="col-6">
          <text-field field-id="vatId"
                      v-if="showVatId"
                      v-model="item.vatId"
                      placeholder="company.vat-id.placeholder"
                      :maxlength="12"
                      :show-label="true"
                      :validation-obj="{required: vatPayer}"
                      :show-input="vatPayer">
            <template #label>
              <input type="checkbox" v-model="vatPayer"/> {{ 'company.vat-payer.label' | translate }}
            </template>
          </text-field>
        </div>
      </div>
      <div class="row form-row">
        <div class="col-6">
          <text-field v-if="showVatLocal"
                      field-id="vatIdLocal"
                      v-model="item.vatIdLocal"
                      label="company.vat-id-local.label"
                      placeholder="company.vat-id-local.placeholder"
                      :maxlength="12"
                      validation="required|max:12">
          </text-field>
        </div>
        <div class="col-md-6">
          <select-field
            v-if="showDefaultCurrency"
            :options="currencies"
            v-model="item.defaultCurrency"
            :translate="false"
            :codelist="true"
            field-id="defaultCurrency"
            label="company.default-currency.label"
            validation="required"></select-field>
        </div>
      </div>
      <div class="row form-row">
        <div class="col-md-12">
          <text-field field-id="web"
                      v-if="showWeb"
                      label="company.web.label"
                      placeholder="company.web.placeholder"
                      v-model="item.web"
                      :maxlength="100"
                      validation="max:100"></text-field>
        </div>
      </div>
      <div class="row form-row">
        <div class="col-md-12">
          <select-field
            v-if="showCategories"
            :options="categories"
            v-model="item.categories"
            :multiple="true"
            :translate="false"
            field-id="categories"
            label="common.category"></select-field>
        </div>
      </div>

      <hr v-if="showSeparator">

      <address-form :address="item.address" :show-region="showRegion" v-if="showAddress"></address-form>

      <div class="row form-row" v-if="showForOrder">
        <div class="col-12 mt-2">
          <checkbox-field
            field-id="forOrder"
            label="company.for-order.label"
            :toggle="true"
            v-model="item.forOrder">
          </checkbox-field>
        </div>
      </div>

      <template #footer v-if="isEdit() && showInactivate">
        <checkbox-field
          class="float-left"
          field-id="valid"
          label="common.inactive.label"
          v-model="item.active"
          :reverse="true">
        </checkbox-field>
      </template>
    </form-modal>
  </div>
</template>

<script lang="ts">
import addressForm from '@/modules/common/components/addressForm.vue'
import CodeList from '@/modules/common/domain/codeList'
import Company from '@/modules/company/domain/company'
import Component, { mixins } from 'vue-class-component'
import createModal from '@/modules/common/components/createModal'
import Currency from '@/modules/currency/domain/currency'
import formModal from '@/modules/common/components/formModal.vue'
import { Getter, State } from 'vuex-class'
import checkboxField from '@/modules/common/components/form/checkboxField.vue'
import textField from '@/modules/common/components/form/textField.vue'
import selectField from '@/modules/common/components/form/selectField.vue'
import { Prop } from 'vue-property-decorator'

@Component({
  components: { selectField, textField, checkboxField, formModal, addressForm }
})
export default class CompanyCreateModal extends mixins<createModal<Company>>(createModal) {
  moduleName = 'company'
  vatPayer = false
  @Prop({ type: Boolean, default: true }) showInactivate!: boolean
  @Prop({ type: Boolean, default: true }) showName!: boolean
  @Prop({ type: Boolean, default: true }) showCompanyId!: boolean
  @Prop({ type: Boolean, default: true }) showVatId!: boolean
  @Prop({ type: Boolean, default: true }) showWeb!: boolean
  @Prop({ type: Boolean, default: true }) showDefaultCurrency!: boolean
  @Prop({ type: Boolean, default: true }) showRegion!: boolean
  @Prop({ type: Boolean, default: true }) showCategories!: boolean
  @Prop({ type: Boolean, default: true }) showVatLocal!: boolean
  @Prop({ type: Boolean, default: true }) showAddress!: boolean
  @Prop({ type: Boolean, default: true }) showForOrder!: boolean
  @Prop({ type: Boolean, default: true }) showSeparator!: boolean

  @State('items', { namespace: 'subjectCategory' }) categories!: Array<CodeList>
  @Getter('validItems', { namespace: 'currency' }) currencies!: Array<Currency>

  beforeSave () {
    if (!this.vatPayer) {
      this.item!.vatId = null
    }
  }

  afterSuccessSave (item: Company) {
    if (!this.isEdit()) {
      this.$router.push({ name: 'partnerDetail', params: { partnerId: item.id + '' } })
    }
  }

  afterModelSet () {
    if (this.item) {
      this.vatPayer = !!this.item.vatId
    }
  }

  async created () {
    await this.$store.dispatch('currency/getAll')
    await this.$store.dispatch('subjectCategory/getAll')
  }
}
</script>
