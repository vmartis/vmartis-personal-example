<template>
  <div>
    <h5>{{ 'regular-sale.label' | translate }}</h5>
    <currency-period-filter-box :filter="filter" v-if="filter" @filter-changed="onFilterChanged"></currency-period-filter-box>
    <partner-order-items-table :orders="orders"></partner-order-items-table>
    <hr>
    <partner-order-statements-table :orders="orders" :order-statements="orderStatements"></partner-order-statements-table>
  </div>
</template>

<script lang="ts">
import _ from 'lodash'
import Component from 'vue-class-component'
import Company from '@/modules/company/domain/company'
import { CurrencyPeriodFilter } from '@/modules/company/domain/currencyPeriodFilter'
import currencyPeriodFilterBox from '@/modules/company/components/currencyPeriodFilterBox.vue'
import EntityFetchParams from '@/modules/common/store/entityFetchParams'
import EntityMap from '@/modules/common/domain/entityMap'
import { FORMAT_SYSTEM_DATE } from '@/utils'
import { Getter, State } from 'vuex-class'
import Item from '@/modules/item/domain/Item'
import moment from 'moment'
import Order from '@/modules/order/domain/order'
import OrderStatement from '@/modules/order/statement/domain/orderStatement'
import partnerOrderItemsTable from '@/modules/company/components/regularsale/partnerOrderItemsTable.vue'
import PartnerOrderStatementsTable from '@/modules/company/components/regularsale/partnerOrderStatementsTable.vue'
import { Prop } from 'vue-property-decorator'
import Range from '@/modules/common/components/form/range'
import Subject from '@/modules/subject/domain/subject'
import Vue from 'vue'

@Component({
  components: { PartnerOrderStatementsTable, partnerOrderItemsTable, currencyPeriodFilterBox }
})
export default class PartnerRegularSalePanel extends Vue {
  filter: CurrencyPeriodFilter | null = null
  orders: Order[] = []

  @Prop({ type: Object, required: true }) subject!: Subject
  @State('owned', { namespace: 'company' }) company!: Company
  @Getter('idMap', { namespace: 'item' }) itemsMap!: EntityMap<Item>
  @Getter('partnersIdMap', { namespace: 'company' }) partnersMap!: EntityMap<Company>
  @Getter('all', { namespace: 'orderStatement' }) orderStatements!: OrderStatement[]

  async onFilterChanged (filter: CurrencyPeriodFilter) {
    this.filter = filter
    await this.fetch()
  }

  async fetch () {
    await this.$store.dispatch('order/getAll', new EntityFetchParams(true, this.createFilter()))
    this.orders = await this.$store.dispatch('order/enrichAll', { itemsMap: this.itemsMap, partnersMap: this.partnersMap, company: this.company })
    await this.$store.dispatch('orderStatement/getAll', new EntityFetchParams(true, this.createFilter()))
  }

  createFilter () {
    const dateFrom = this.filter && this.filter.range && this.filter.range.from && this.filter.range.from.isValid()
      ? FORMAT_SYSTEM_DATE(this.filter.range.from) : null
    const dateTo = this.filter && this.filter.range && this.filter.range.to && this.filter.range.to.isValid()
      ? FORMAT_SYSTEM_DATE(this.filter.range.to) : null
    return _.pickBy({
      'date-from': dateFrom,
      'date-to': dateTo,
      currency: this.filter!.currency,
      'subscriber-id': this.subject.id
    }, _.identity)
  }

  async created () {
    await this.$store.dispatch('item/getAll')
    await this.$store.dispatch('company/getAllPartners')
    await this.$store.dispatch('company/getOwned')
    await this.$store.dispatch('order/clearAll')
    await this.$store.dispatch('orderStatement/clearAll')
    this.filter = new CurrencyPeriodFilter(this.company.defaultCurrency || null, new Range(moment().subtract(6, 'month').startOf('month')))
    await this.fetch()
  }
}
</script>
