<template>
  <div>
    <h5>{{ 'order-statements.label.short' | translate }}</h5>
    <div class="row">
      <div class="col">
        <p>
          <span>{{ 'order.statements.not.billed.label' | translate }}: </span><strong>{{ totalNotBilled | currency(currency) }}</strong>
        </p>
      </div>
    </div>
    <div class="table-responsive">
      <table class="table no-padding table-hover table-striped table-sm" v-if="orderStatements.length">
        <thead>
        <tr class="bg-gray">
          <th>{{ 'order-statement.label.short' | translate }}</th>
          <th>{{ 'common.date' | translate }}</th>
          <th class="text-right">{{ 'order-statement.total.price.label' | translate }}</th>
          <th class="text-right">{{ 'order-statement.total.invoiced.label' | translate }}</th>
          <th></th>
          <th></th>
        </tr>
        </thead>
        <tbody>
        <template v-for="orderStatement in orderStatements">
          <tr v-bind:key="orderStatement.id" role="button" @click.stop="detail(orderStatement.id)">
            <td>
              {{ orderStatement.formattedNumber }}
            </td>
            <td>{{ orderStatement.date | date }}</td>
            <td class="text-right">{{ orderStatement.totalWithoutVat | currency(orderStatement.currency) }}</td>
            <td class="text-right">{{ orderStatement.totalInvoiced | currency(orderStatement.currency) }}</td>
            <td><i v-if="orderStatement.note" class="fas fa-info-circle text-blue" :title="orderStatement.note" v-b-tooltip.left></i></td>
            <td>
              <i class="fas fa-search text-muted" :title="'common.detail' | translate" v-b-tooltip.bottom></i>
            </td>
          </tr>
        </template>
        </tbody>
      </table>
    </div>
    <div class="row">
      <div class="col-12">
        <loading id="order-statement"></loading>
        <no-records :data="orderStatements" loading-id="order-statement"></no-records>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import _ from 'lodash'
import Component from 'vue-class-component'
import loading from '../../../common/components/loading.vue'
import noRecords from '../../../common/components/noRecords.vue'
import Order from '@/modules/order/domain/order'
import OrderStatement from '@/modules/order/statement/domain/orderStatement'
import { Prop } from 'vue-property-decorator'
import staticField from '@/modules/common/components/form/staticField.vue'
import Vue from 'vue'

@Component({
  components: { staticField, noRecords, loading }
})
export default class PartnerMovementsCategoryTable extends Vue {
  @Prop({ type: Array, required: true }) orders!: Order[]
  @Prop({ type: Array, required: true }) orderStatements!: OrderStatement[]

  get currency () {
    const first = _.first(this.orders)
    return first ? first.currency : null
  }

  get totalNotBilled () {
    return _(this.orders).filter(order => order.delivered && !order.billed).sumBy('totalWithoutVat') || 0
  }

  detail (id: number) {
    this.$router.push({ name: 'orderStatementDetail', params: { orderStatementId: id + '' } })
  }
}
</script>
