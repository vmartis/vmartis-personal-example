<template>
  <div class="row justify-content-end" v-if="filterModel">
    <div class="col-md-4">
      <select-field
        :options="currencies"
        v-model="filterModel.currency"
        :codelist="true"
        :translate="false"
        :clearable="false"
        field-id="currency"
        label="currency.label"></select-field>
    </div>
    <div class="col-md-4">
      <date-range-field v-model="filterModel.range" field-id="range" label="common.range.label" :right="true" @validate="fetch()" :clearable="false"></date-range-field>
    </div>
  </div>
</template>

<script lang="ts">
import _ from 'lodash'
import Component from 'vue-class-component'
import Currency from '@/modules/currency/domain/currency'
import { CurrencyPeriodFilter } from '@/modules/company/domain/currencyPeriodFilter'
import dateRangeField from '@/modules/common/components/form/dateRangeField.vue'
import selectField from '@/modules/common/components/form/selectField.vue'
import { Prop, Watch } from 'vue-property-decorator'
import { State } from 'vuex-class'
import Vue from 'vue'

@Component({
  components: { dateRangeField, selectField }
})
export default class CurrencyPeriodFilterBox extends Vue {
  filterModel: CurrencyPeriodFilter | null = null

  @State('items', { namespace: 'currency' }) currencies? : Currency[]
  @Prop({ type: CurrencyPeriodFilter, required: true }) filter!: CurrencyPeriodFilter

  @Watch('filterModel.currency')
  onFilterModelChange (newCurrency?: string, oldCurrency?: string) {
    if (newCurrency && oldCurrency && newCurrency !== oldCurrency) {
      this.fetch()
    }
  }

  private fetch () {
    this.$emit('filter-changed', _.cloneDeep(this.filterModel))
  }

  async created () {
    await this.$store.dispatch('currency/getAll')
    this.filterModel = _.cloneDeep(this.filter)
  }
}
</script>
