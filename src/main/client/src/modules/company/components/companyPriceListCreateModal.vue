<template>
  <div v-if="item">
    <form-modal
      v-if="item"
      :show-modal="show"
      :model="item"
      :title="isEdit() ? 'price-list.edit.label' : 'price-list.create.label'"
      :footer-class="isEdit() ? 'justify-content-between' : ''"
      @submit="onSubmit"
      @close="onClose">
      <div class="row form-row">
        <div class="col-md-5">
          <date-time-field
            v-model="item.validFrom"
            field-id="validFrom"
            label="common.valid-from.label"
            :show-time="false"
            :clearable="false"
            validation="required">
          </date-time-field>
        </div>
        <div class="col-md-7">
          <select-field v-if="itemEditable"
            :options="itemList"
            v-model="item.item"
            :translate="false"
            field-id="item"
            label="price-list.item.label"
            validation="required">
          </select-field>
          <select-field v-else-if="companyEditable"
            :options="partners"
            v-model="item.company"
            :translate="false"
            :clearable="false"
            field-id="item"
            label="partner.label"
            validation="required">
          </select-field>
        </div>
      </div>
      <div class="row form-row" v-for="(priceListItem, index) in item.items" v-bind:key="index">
        <div class="col-3">
          <select-field
            :options="currencies"
            v-model="priceListItem.currency"
            :codelist="true"
            :translate="false"
            :clearable="false"
            :field-id="'currency-' + index"
            label="currency.label"
            label-prop="value"
            :show-label="index === 0"
            validation="required"></select-field>
        </div>
        <div class="col">
          <number-field
            v-model="priceListItem.price"
            label="common.price.unit"
            :show-label="index === 0"
            :field-id="'price-' + index"
            :step="1"
            validation="required|decimal:2|max_value:99999999.99|min_value:0">
          </number-field>
        </div>
        <div class="col">
          <select-field
            :options="vatRates"
            v-model="priceListItem.vatRate"
            :clearable="false"
            :codelist="true"
            :translate="false"
            :field-id="'item-vat-rate-' + index"
            label="finance.vat.label"
            :show-label="index === 0"
            validation="required">
          </select-field>
        </div>
        <div class="col pl-0">
          <static-field label="finance.price.with.vat"
                        class="text-right"
                        :show-label="index === 0"
                        label-class="required text-right"
                        content-class="text-right">
            {{ priceListItem.totalPrice | currency(priceListItem.currency) }}
          </static-field>
        </div>
        <div class="col-auto buttons">
          <a role="button" class="link-input-line" :class="{'no-label' : index > 0}" v-show="item.items.length > 1" @click.prevent="deletePriceListItem(priceListItem)"><i class="fa fa-2x fa-times text-danger"></i></a>
        </div>
      </div>
      <div class="row form-row">
        <div class="col-md-12">
          <button class="btn btn-success" type="button" @click.prevent="addItem()"><i class="fa fa-plus"></i></button>
        </div>
      </div>
      <template #footer v-if="isEdit()">
        <button class="btn btn-outline-danger" @click.stop="deleteItem()">{{ 'common.delete' | translate }}</button>
      </template>
    </form-modal>
  </div>
</template>

<script lang="ts">
import _ from 'lodash'
import Company from '@/modules/company/domain/company'
import Component, { mixins } from 'vue-class-component'
import createModal from '@/modules/common/components/createModal'
import Currency from '@/modules/currency/domain/currency'
import dateTimeField from '@/modules/common/components/form/dateTimeField.vue'
import formModal from '@/modules/common/components/formModal.vue'
import { Getter, State } from 'vuex-class'
import Item from '@/modules/item/domain/Item'
import notificationService from '@/modules/common/services/notificationService'
import numberField from '@/modules/common/components/form/numberField.vue'
import { Prop } from 'vue-property-decorator'
import PriceList from '@/modules/pricelist/domain/priceList'
import PriceListItem from '@/modules/pricelist/domain/priceListItem'
import selectField from '@/modules/common/components/form/selectField.vue'
import staticField from '@/modules/common/components/form/staticField.vue'

@Component({
  components: { dateTimeField, formModal, numberField, selectField, staticField }
})
export default class PriceListCreateModal extends mixins<createModal<PriceList>>(createModal) {
  moduleName = 'priceList'

  @State('owned', { namespace: 'company' }) company?: Company
  @Getter('activePartners', { namespace: 'company' }) partners?: Company[]
  @Getter('validItems', { namespace: 'currency' }) currencies!: Currency[]
  @Getter('activeForPurchase', { namespace: 'item' }) itemList!: Item[]

  @Prop({ type: String, required: false, default: '' }) editCurrency?: string
  @Prop({ type: Boolean, default: false }) companyEditable!: boolean
  @Prop({ type: Boolean, default: true }) itemEditable!: boolean

  get vatRates () {
    const list = this.item!.validFrom
      ? this.$store.getters['vatRate/validItems'](this.item!.validFrom, this.company!.address!.country)
      : []
    return _.unionBy(list, 'rate')
  }

  validate () {
    const usedCurrencyCodes = _(this.item!.items).map('currency').uniq().value()
    if (usedCurrencyCodes.length < this.item!.items!.length) {
      notificationService.error('error.priceList.currency.duplicate')
      return false
    }
    return true
  }

  deletePriceListItem (item: PriceListItem) {
    this.item!.items = _.without(this.item!.items, item)
  }

  addItem () {
    this.item!.items = [...this.item!.items, new PriceListItem({})]
  }

  async created () {
    await this.$store.dispatch('currency/getAll')
    await this.$store.dispatch('vatRate/getAll')
    await this.$store.dispatch('item/getAll')
    await this.$store.dispatch('company/getOwned')
    await this.$store.dispatch('company/getAllPartners')
  }
}
</script>
