<template>
  <div>
    <company-price-list-create-modal
      :show-modal="showCreateModal"
      :model="selectedItem"
      @close="createHide()"></company-price-list-create-modal>
    <div class="row">
      <div class="col-12">
        <h5 class="box-title">{{ 'price-list.individual.label' | translate }}</h5>
      </div>
    </div>
    <div class="row justify-content-between mb-2">
      <div class="col-md-6">
        <button class="btn btn-success" type="button" @click="createShow()"><i class="fa fa-plus"></i></button>
      </div>
      <div class="col-md-6 col-lg-5">
        <form class="form-inline float-right">
          <div class="checkbox">
            <label class="ml-2">
              <input type="checkbox" v-model="showAll" class="mr-1"> {{ 'common.show.all' | translate }}
            </label>
          </div>
        </form>
      </div>
    </div>
    <div class="row">
      <div class="col-sm-12">
        <div class="table-responsive">
          <table class="table no-padding table-hover table-striped table-sm" v-if="items.length">
            <thead>
            <tr class="bg-gray">
              <th>{{ 'price-list.item.label' | translate }}</th>
              <th class="text-right">{{ 'common.price.unit' | translate }}</th>
              <th class="text-right">{{ 'finance.vat-rate.label' | translate }}</th>
              <th class="text-right">{{ 'finance.price.with.vat' | translate }}</th>
              <th></th>
            </tr>
            </thead>
            <tbody>
            <template v-for="item in priceListItems">
              <tr v-bind:key="item.id">
                <td>
                  <div>{{ item.priceList.item.name }}</div>
                  <small class="text-muted">{{ 'common.from' | translate }} {{ item.priceList.validFrom | date }}</small></td>
                <td class="text-right">{{ item.priceListItem.price | currency(item.priceListItem.currency) }}</td>
                <td class="text-right">{{ item.priceListItem.vatRate + ' %' }}</td>
                <td class="text-right">{{ item.priceListItem.totalPrice | currency(item.priceListItem.currency) }}</td>
                <td class="text-right text-nowrap">
                  <button class="btn btn-link btn-tool" @click.prevent="editItem(item.priceList)"><i class="fas fa-pencil-alt text-muted" :title="'common.edit' | translate"></i></button>
                </td>
              </tr>
            </template>
            </tbody>
          </table>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-sm-12">
        <loading id="price-list"></loading>
        <no-records :data="items" loading-id="price-list"></no-records>
      </div>
    </div>
  </div>
</template>

<script lang="ts">

import _ from 'lodash'
import box from '@/modules/common/components/box.vue'
import booleanIcon from '@/modules/common/components/icon/booleanIcon.vue'
import Component, { mixins } from 'vue-class-component'
import companyPriceListCreateModal from '@/modules/company/components/companyPriceListCreateModal.vue'
import entityTableMixin from '@/modules/common/mixins/entityTableMixin'
import itemCreateModal from '@/modules/item/components/itemCreateModal.vue'
import loading from '@/modules/common/components/loading.vue'
import moment from 'moment'
import noRecords from '@/modules/common/components/noRecords.vue'
import selectField from '@/modules/common/components/form/selectField.vue'
import { Prop } from 'vue-property-decorator'
import PriceList from '@/modules/pricelist/domain/priceList'
import priceListService from '@/modules/pricelist/service/priceListService'
import { Getter } from 'vuex-class'
import Currency from '@/modules/currency/domain/currency'
import PriceListItem from '@/modules/pricelist/domain/priceListItem'
import EntityFetchParams from '@/modules/common/store/entityFetchParams'

@Component({
  components: { booleanIcon, box, companyPriceListCreateModal, itemCreateModal, loading, noRecords, selectField }
})
export default class ItemsTable extends mixins(entityTableMixin) {
  entityModuleName = 'priceList'
  activeSupport = false
  showAll = false

  @Prop({ required: false }) companyId!: number

  @Getter('validItems', { namespace: 'currency' }) currencyList!: Array<Currency>

  get priceListItems (): Array<any> {
    let priceLists: Array<PriceList>
    if (this.showAll) {
      priceLists = _.sortBy(this.items as Array<PriceList>, 'item.name')
    } else {
      priceLists = _((this.items as Array<PriceList>))
        .groupBy('item.id')
        .values()
        .map(priceLists => _.find(priceLists, priceList => priceList.validInDate(moment()))) // sorted by date from BE, so just take valid
        .sortBy('item.name')
        .value() as Array<PriceList>
    }
    return _.flatMap(priceLists, (priceList: PriceList) =>
      _.map(priceList.items as Array<PriceListItem>, (priceListItem: PriceListItem) => ({ priceList, priceListItem })))
  }

  created () {
    this.$store.dispatch('priceList/clearAll')
    this.$store.dispatch('currency/getAll')
    this.fetchActionParams = new EntityFetchParams(true, { 'include-global': false, 'company-id': this.companyId })
    this.newItemTemplateDefault = () => priceListService.newPriceList([], null, this.companyId)
  }
}
</script>
