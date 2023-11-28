<template>
  <div>
    <h5>{{ 'orders.summary.by.item.label' | translate }}</h5>
    <div class="table-responsive">
      <table class="table no-padding table-hover table-striped table-sm" v-if="itemSums.length">
        <thead>
        <tr class="bg-gray">
          <th>{{ 'order.item.label' | translate }}</th>
          <th>{{ 'common.price' | translate }}</th>
          <th>{{ 'order.ordered.label' | translate }}</th>
          <th>{{ 'orders.delivered' | translate }}</th>
          <th>{{ 'order.returned.label' | translate }}</th>
          <th class="text-right">{{ 'order.total-without-vat.label' | translate }}</th>
        </tr>
        </thead>
        <tbody>
        <template v-for="(itemSum, index) in itemSums">
          <tr v-bind:key="index">
            <td>
              {{ itemSum.item.name }}
            </td>
            <td class="text-right">{{ itemSum.price | currency(currency) }}</td>
            <td class="text-right">{{ itemSum.ordered }}</td>
            <td class="text-right">{{ itemSum.delivered }}</td>
            <td class="text-right">{{ itemSum.returned }}</td>
            <td class="text-right">{{ itemSum.totalWithoutVat | currency(currency) }}</td>
          </tr>
        </template>
        </tbody>
        <tfoot>
        <tr>
          <th>{{ 'common.total' | translate }}</th>
          <th></th>
          <th class="text-right text-nowrap">{{ totalOrdered }}</th>
          <th class="text-right text-nowrap">{{ totalDelivered }}</th>
          <th class="text-right text-nowrap">{{ totalReturned }}</th>
          <th class="text-right text-nowrap">{{ totalWithoutVat | currency(currency) }}</th>
        </tr>
        </tfoot>
      </table>
    </div>
    <div class="row">
      <div class="col-12">
        <loading id="order"></loading>
        <no-records :data="itemSums" loading-id="order"></no-records>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import _ from 'lodash'
import { compareFunction } from '@/utils'
import Component from 'vue-class-component'
import Item from '@/modules/item/domain/Item'
import Loading from '../../../common/components/loading.vue'
import NoRecords from '../../../common/components/noRecords.vue'
import Order from '@/modules/order/domain/order'
import { Prop, Watch } from 'vue-property-decorator'
import Vue from 'vue'

class OrderItemSum {
  item: Item | null = null
  price: number | null = null
  ordered: number | null = null
  delivered: number | null = null
  returned: number | null = null
  totalWithoutVat: number | null = null
}

@Component({
  components: { NoRecords, Loading }
})
export default class PartnerMovementsCategoryTable extends Vue {
  itemSums: OrderItemSum[] | null = []

  @Prop({ type: Array, required: true }) orders!: Order[]

  @Watch('orders', { deep: true })
  onMovementsChane () {
    this.calcSums()
  }

  get currency () {
    const first = _.first(this.orders)
    return first ? first.currency : null
  }

  get totalOrdered () {
    return _.sumBy(this.itemSums, 'ordered') || 0
  }

  get totalDelivered () {
    return _.sumBy(this.itemSums, 'delivered') || 0
  }

  get totalReturned () {
    return _.sumBy(this.itemSums, 'returned') || 0
  }

  get totalWithoutVat () {
    return _.sumBy(this.itemSums, 'totalWithoutVat') || 0
  }

  private calcSums () {
    const sums = _(this.orders)
      .flatMap('items')
      .groupBy('item.id')
      .values()
      .map(orderItemsByItem => ({
        item: _.first(orderItemsByItem)!.item,
        price: _.round(_.meanBy(orderItemsByItem, 'itemPrice') || 0, 2),
        ordered: _.sumBy(orderItemsByItem, 'ordered') || 0,
        delivered: _.sumBy(orderItemsByItem, 'delivered') || 0,
        returned: _.sumBy(orderItemsByItem, 'returned') || 0,
        totalWithoutVat: _.sumBy(orderItemsByItem, 'totalWithoutVat')
      } as OrderItemSum))
      .value()
    this.itemSums = sums.sort(compareFunction('item.name'))
  }
}
</script>
