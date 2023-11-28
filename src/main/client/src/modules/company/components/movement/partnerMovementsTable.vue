<template>
  <div>
    <h5>{{ 'money-movements.summary.label' | translate }}</h5>
    <div class="table-responsive">
      <table class="table no-padding table-hover table-striped table-sm" v-if="movements && movements.length">
        <thead>
        <tr class="bg-gray">
          <th></th>
          <th>{{ 'common.date' | translate }}</th>
          <th class="text-right">{{ 'common.total' | translate }}</th>
          <th class="text-right">{{ 'finance.tax.base.label' | translate }}</th>
          <th class="text-right">{{ 'finance.vat.label' | translate }}</th>
        </tr>
        </thead>
        <tbody>
        <template v-for="(movement, index) in movements">
          <tr v-bind:key="index">
            <td>
              <i :title="movement.type | codeListValue(moneyMovementTypes) | translate" v-b-tooltip
                 :class="{'fa-arrow-down text-danger': movement.type === 'OUTCOME', 'fa-arrow-up text-success': movement.type === 'INCOME'}"
                 class="fa">
              </i>
            </td>
            <td>{{ movement.date | date }}</td>
            <td class="text-right"
                :class="{ 'text-danger': movement.type === 'OUTCOME', 'text-success': movement.type === 'INCOME' }">{{ movement.totalAmount | currency(movement.currency) }}
            </td>
            <td class="text-right">{{ movement.totalWithoutVat | currency(movement.currency) }}</td>
            <td class="text-right">{{ movement.totalVat | currency(movement.currency) }}</td>
          </tr>
        </template>
        </tbody>
        <tfoot>
        <tr>
          <th></th>
          <th>{{ 'money-movements.total.fiscal.label' | translate }}</th>
          <th class="text-right text-nowrap">{{ totalAmountFiscal | currency(currency) }}</th>
          <th class="text-right text-nowrap">{{ totalWithoutVatFiscal | currency(currency) }}</th>
          <th class="text-right text-nowrap">{{ totalVatFiscal | currency(currency) }}</th>
        </tr>
        <tr>
          <th></th>
          <th>{{ 'money-movements.total.not.fiscal.label' | translate }}</th>
          <th class="text-right text-nowrap">{{ totalAmountNotFiscal | currency(currency) }}</th>
          <th class="text-right text-nowrap"> -</th>
          <th class="text-right text-nowrap"> -</th>
        </tr>
        </tfoot>
      </table>
    </div>
    <div class="row">
      <div class="col-12">
        <loading id="money-movement"></loading>
        <no-records :data="movements" loading-id="money-movement"></no-records>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import _ from 'lodash'
import Component from 'vue-class-component'
import Loading from '../../../common/components/loading.vue'
import MoneyMovement from '@/modules/money/movement/domain/moneyMovement'
import moneyMovementTypes from '@/modules/money/movement/type/moneyMovementTypes'
import NoRecords from '../../../common/components/noRecords.vue'
import { Prop } from 'vue-property-decorator'
import Vue from 'vue'

@Component({
  components: { NoRecords, Loading }
})
export default class PartnerMovementsTable extends Vue {
  moneyMovementTypes = moneyMovementTypes

  @Prop({ type: Array, required: true }) movements!: MoneyMovement[]

  get currency () {
    const first = _.first(this.movements)
    return first ? first.moneyBox!.currency : null
  }

  get totalAmountFiscal () {
    return _(this.movements).filter('fiscal').sumBy('totalAmount') || 0
  }

  get totalWithoutVatFiscal () {
    return _(this.movements).filter('fiscal').sumBy('totalWithoutVat') || 0
  }

  get totalVatFiscal () {
    return _(this.movements).filter('fiscal').sumBy('totalVat') || 0
  }

  get totalAmountNotFiscal () {
    return _(this.movements).filter(movement => !movement.fiscal).sumBy('totalAmount') || 0
  }
}
</script>
