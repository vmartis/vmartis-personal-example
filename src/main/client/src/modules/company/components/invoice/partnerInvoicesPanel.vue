<template>
  <div>
    <h5>{{ 'invoicing.label' | translate }}</h5>
    <currency-period-filter-box :filter="filter" v-if="filter"
                                @filter-changed="onFilterChanged"></currency-period-filter-box>
    <partner-invoices-table :filter="filter" v-if="filter" :subject="subject"></partner-invoices-table>
  </div>
</template>

<script lang="ts">
import Component from 'vue-class-component'
import Company from '@/modules/company/domain/company'
import currencyPeriodFilterBox from '@/modules/company/components/currencyPeriodFilterBox.vue'
import { CurrencyPeriodFilter } from '@/modules/company/domain/currencyPeriodFilter'
import { State } from 'vuex-class'
import moment from 'moment'
import { Prop } from 'vue-property-decorator'
import Range from '@/modules/common/components/form/range'
import Subject from '@/modules/subject/domain/subject'
import Vue from 'vue'
import partnerInvoicesTable from '@/modules/company/components/invoice/partnerInvoicesTable.vue'

@Component({
  components: { currencyPeriodFilterBox, partnerInvoicesTable }
})
export default class PartnerInvoicesPanel extends Vue {
  filter: CurrencyPeriodFilter | null = null

  @Prop({ type: Object, required: true }) subject!: Subject
  @State('owned', { namespace: 'company' }) company!: Company

  async onFilterChanged (filter: CurrencyPeriodFilter) {
    this.filter = filter
  }

  async created () {
    this.filter = new CurrencyPeriodFilter(this.company.defaultCurrency || null, new Range(moment().subtract(6, 'month').startOf('month')))
  }
}
</script>
