<template>
  <div>
    <h5>{{ 'money-movements.summary.by.category.label' | translate }}</h5>
    <div class="table-responsive">
      <table class="table no-padding table-hover table-striped table-sm" v-if="movementSums && movementSums.length">
        <thead>
        <tr class="bg-gray">
          <th></th>
          <th>{{ 'money-movement.category.label' | translate }}</th>
          <th class="text-right">{{ 'common.total' | translate }}</th>
          <th class="text-right">{{ 'finance.tax.base.label' | translate }}</th>
          <th class="text-right">{{ 'finance.vat.label' | translate }}</th>
        </tr>
        </thead>
        <tbody>
        <template v-for="(movementSum, index) in movementSums">
          <tr v-bind:key="index">
            <td>
              <i :title="movementSum.type | codeListValue(moneyMovementTypes) | translate" v-b-tooltip
              :class="{'fa-arrow-down text-danger': movementSum.type === 'OUTCOME', 'fa-arrow-up text-success': movementSum.type === 'INCOME'}" class="fa">
              </i>
            </td>
            <td>{{ movementSum.categoryLabel }}</td>
            <td class="text-right" :class="{ 'text-danger': movementSum.type === 'OUTCOME', 'text-success': movementSum.type === 'INCOME' }">{{ movementSum.totalAmount | currency(movementSum.currency) }}</td>
            <td class="text-right">{{ movementSum.totalWithoutVat | currency(movementSum.currency) }}</td>
            <td class="text-right">{{ movementSum.totalVat | currency(movementSum.currency) }}</td>
          </tr>
        </template>
        </tbody>
        <tfoot>
        <tr>
          <th></th>
          <th>{{ 'common.total' | translate }}</th>
          <th class="text-right text-nowrap">{{ totalAmount | currency(currency) }}</th>
          <th class="text-right text-nowrap">{{ totalWithoutVat | currency(currency) }}</th>
          <th class="text-right text-nowrap">{{ totalVat | currency(currency) }}</th>
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
import { compareFunction } from '@/utils'
import Component from 'vue-class-component'
import i18n from '@/i18n'
import Labeled from '@/modules/common/values/labeled'
import Loading from '../../../common/components/loading.vue'
import MoneyMovement from '@/modules/money/movement/domain/moneyMovement'
import { MoneyMovementType } from '@/modules/money/movement/type/moneyMovementType'
import moneyMovementTypes from '@/modules/money/movement/type/moneyMovementTypes'
import NoRecords from '../../../common/components/noRecords.vue'
import { Prop, Watch } from 'vue-property-decorator'
import Vue from 'vue'

class MovementSum {
  type?: MoneyMovementType | null = null
  categoryLabel?: string | null = null
  currency?: string | null = null
  totalAmount?: number | null = null
  totalVat?: number | null = null
  totalWithoutVat?: number | null = null
}

@Component({
  components: { NoRecords, Loading }
})
export default class PartnerMovementsCategoryTable extends Vue {
  moneyMovementTypes = moneyMovementTypes
  movementSums: MovementSum[] | null = null
  notDefined = {
    value: 0,
    label: i18n.message('common.not-defined')
  } as Labeled

  @Prop({ type: Array, required: true }) movements!: MoneyMovement[]

  @Watch('movements', { deep: true })
  onMovementsChane () {
    this.movementSums = []
    const outcomeMovements = _.filter(this.movements, { type: MoneyMovementType.OUTCOME })
    const incomeMovements = _.filter(this.movements, { type: MoneyMovementType.INCOME })

    this.movementSums = [...this.calcSums(outcomeMovements), ...this.calcSums(incomeMovements)]
  }

  get currency () {
    const first = _.first(this.movements)
    return first ? first.moneyBox!.currency : null
  }

  get totalAmount () {
    return _.sumBy(this.movementSums, 'totalAmount') || 0
  }

  get totalWithoutVat () {
    return _.sumBy(this.movementSums, 'totalWithoutVat') || 0
  }

  get totalVat () {
    return _.sumBy(this.movementSums, 'totalVat') || 0
  }

  private calcSums (movements: MoneyMovement[]): MovementSum[] {
    let withCategory = _(movements)
      .filter(movement => !_.isNil(movement.category))
      .groupBy('category.id')
      .values()
      .map(movementsByCategory => ({
        type: _.first(movementsByCategory)!.type,
        categoryLabel: _.first(movementsByCategory)!.category!.label,
        currency: _.first(movementsByCategory)!.moneyBox!.currency!,
        totalAmount: _.sumBy(movementsByCategory, 'totalAmount'),
        totalVat: _.sumBy(movementsByCategory, 'totalVat'),
        totalWithoutVat: _.sumBy(movementsByCategory, 'totalWithoutVat')
      } as MovementSum))
      .value()
    withCategory = withCategory.sort(compareFunction('categoryLabel'))

    const withoutCategory = _.filter(movements, movement => _.isNil(movement.category))
    if (withoutCategory.length > 0) {
      return [...withCategory, {
        type: _.first(withoutCategory)!.type,
        categoryLabel: this.notDefined.label,
        currency: _.first(withoutCategory)!.moneyBox!.currency!,
        totalAmount: _.sumBy(withoutCategory, 'totalAmount'),
        totalVat: _.sumBy(withoutCategory, 'totalVat'),
        totalWithoutVat: _.sumBy(withoutCategory, 'totalWithoutVat')
      }]
    } else {
      return withCategory
    }
  }
}
</script>
