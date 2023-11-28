<template>
  <div>
    <div class="row">
      <div class="col-auto">
        <dl>
          <dt>{{ 'invoice.due.date.label' | translate }}</dt>
          <dd v-if="subject.saleSettings.dueDateOffset">{{ subject.saleSettings.dueDateOffset | number }} {{ 'common.days' | translate }}</dd>
          <dd v-else>-</dd>
        </dl>
      </div>
      <div class="col-auto">
        <dl>
          <dt>{{ 'invoice.due.date.mean' | translate }}</dt>
          <dd v-if="invoices.length">{{ meanDueDate | number }} {{ 'common.days' | translate }}</dd>
          <dd v-else>-</dd>
        </dl>
      </div>
    </div>
    <div class="table-responsive">
      <table class="table no-padding table-hover table-striped table-sm" v-if="invoices.length">
        <thead>
        <tr class="bg-gray">
          <th>{{ 'common.number' | translate }}</th>
          <th>{{ 'invoice.due.date.label' | translate }}</th>
          <th class="text-right">{{ 'common.total' | translate }}</th>
          <th class="text-right">{{ 'invoice.unpaid.amount.label' | translate }}</th>
          <th></th>
        </tr>
        </thead>
        <tbody>
        <template v-for="invoice in invoices">
          <tr v-bind:key="invoice.id" role="button" @click.stop="detail(invoice)">
            <td>{{ invoice.number }}</td>
            <td>{{ invoice.dueDate | date }}</td>
            <td class="text-right text-nowrap">{{ invoice.totalAmount | currency(invoice.currency) }}</td>
            <td class="text-right text-nowrap" :class="{'text-success': invoice.unpaidAmount === 0, 'text-danger': invoice.unpaidAmount !== 0 && invoice.overdue,}">{{ invoice.unpaidAmount | currency(invoice.currency) }}</td>
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
        <loading id="invoice"></loading>
        <no-records :data="invoices" loading-id="invoice"></no-records>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import _ from 'lodash'
import Company from '@/modules/company/domain/company'
import Component from 'vue-class-component'
import { CurrencyPeriodFilter } from '@/modules/company/domain/currencyPeriodFilter'
import EntityFetchParams from '@/modules/common/store/entityFetchParams'
import { FORMAT_SYSTEM_DATE } from '@/utils'
import { Getter } from 'vuex-class'
import Invoice from '@/modules/invoice/domain/invoice'
import loading from '../../../common/components/loading.vue'
import noRecords from '../../../common/components/noRecords.vue'
import { Prop, Watch } from 'vue-property-decorator'
import Vue from 'vue'

@Component({
  components: { noRecords, loading }
})
export default class PartnerInvoicesTable extends Vue {
  @Prop({ type: CurrencyPeriodFilter, required: true }) filter!: CurrencyPeriodFilter
  @Prop({ type: Object, required: true }) subject!: Company

  @Getter('all', { namespace: 'invoice' }) invoices!: Invoice[]

  @Watch('filter', { deep: true, immediate: true })
  onFilterChange () {
    this.fetch()
  }

  get meanDueDate () {
    return _(this.invoices).filter('paid').meanBy('paidDays')
  }

  detail (invoice: Invoice) {
    this.$router.push({ name: 'invoiceDetailOutcome', params: { id: invoice.id + '' } })
  }

  async onFilterChanged (filter: CurrencyPeriodFilter) {
    this.filter = filter
    await this.fetch()
  }

  async fetch () {
    await this.$store.dispatch('invoice/getAll', new EntityFetchParams(true, this.createFilter()))
  }

  createFilter () {
    const dateFrom = this.filter && this.filter.range && this.filter.range.from && this.filter.range.from.isValid()
      ? FORMAT_SYSTEM_DATE(this.filter.range.from) : null
    const dateTo = this.filter && this.filter.range && this.filter.range.to && this.filter.range.to.isValid()
      ? FORMAT_SYSTEM_DATE(this.filter.range.to) : null
    return _.pickBy({
      dateOfIssueFrom: dateFrom,
      dateOfIssueTo: dateTo,
      type: 'OUTCOME',
      currency: this.filter!.currency,
      'subscriber-id': this.subject.id
    }, _.identity)
  }

  async created () {
    await this.$store.dispatch('invoice/clearAll')
  }
}
</script>
