<template>
  <div>
    <h5>{{ 'money-movements.label' | translate }}</h5>
    <currency-period-filter-box :filter="filter" v-if="filter" @filter-changed="onFilterChanged"></currency-period-filter-box>
    <partner-movements-category-table :movements="movements"></partner-movements-category-table>
    <hr>
    <partner-movements-table :movements="movements"></partner-movements-table>
  </div>
</template>

<script lang="ts">
import _ from 'lodash'
import Component from 'vue-class-component'
import Company from '@/modules/company/domain/company'
import currencyPeriodFilterBox from '@/modules/company/components/currencyPeriodFilterBox.vue'
import { CurrencyPeriodFilter } from '@/modules/company/domain/currencyPeriodFilter'
import EntityFetchParams from '@/modules/common/store/entityFetchParams'
import { FORMAT_SYSTEM_DATE } from '@/utils'
import { Getter, State } from 'vuex-class'
import moment from 'moment'
import MoneyMovement from '@/modules/money/movement/domain/moneyMovement'
import partnerMovementsCategoryTable from '@/modules/company/components/movement/partnerMovementsCategoryTable.vue'
import { Prop } from 'vue-property-decorator'
import Range from '@/modules/common/components/form/range'
import Subject from '@/modules/subject/domain/subject'
import Vue from 'vue'
import PartnerMovementsTable from '@/modules/company/components/movement/partnerMovementsTable.vue'

@Component({
  components: { PartnerMovementsTable, partnerMovementsCategoryTable, currencyPeriodFilterBox }
})
export default class PartnerMoneyMovementPanel extends Vue {
  filter: CurrencyPeriodFilter | null = null

  @Prop({ type: Object, required: true }) subject!: Subject
  @Getter('active', { namespace: 'moneyMovement' }) movements!: MoneyMovement[]
  @State('owned', { namespace: 'company' }) company! : Company

  async onFilterChanged (filter: CurrencyPeriodFilter) {
    this.filter = filter
    await this.fetch()
  }

  async fetch () {
    await this.$store.dispatch('moneyMovement/getAll', new EntityFetchParams(true, this.createFilter()))
  }

  createFilter () {
    const dateFrom = this.filter && this.filter.range && this.filter.range.from && this.filter.range.from.isValid()
      ? FORMAT_SYSTEM_DATE(this.filter.range.from) : null
    const dateTo = this.filter && this.filter.range && this.filter.range.to && this.filter.range.to.isValid()
      ? FORMAT_SYSTEM_DATE(this.filter.range.to) : null
    return _.pickBy({
      dateFrom,
      dateTo,
      currency: this.filter!.currency,
      subjectId: this.subject.id
    }, _.identity)
  }

  async created () {
    await this.$store.dispatch('moneyMovement/clearAll')
    await this.$store.dispatch('company/getOwned')
    this.filter = new CurrencyPeriodFilter(this.company.defaultCurrency || null, new Range(moment().subtract(6, 'month').startOf('month')))
    await this.fetch()
  }
}
</script>
