<template>
  <page title="money-movement.import.label" :bread-crumb-data="breadCrumbData">
    <bank-tx-import-table></bank-tx-import-table>
  </page>
</template>
<script lang="ts">
import BankTxImportTable from '@/modules/bank/transaction/import/components/bankTxImportTable.vue'
import BreadCrumbData from '@/modules/common/components/breadcrumb/breadcrumbData'
import Component from 'vue-class-component'
import Page from '@/modules/common/components/page.vue'
import Vue from 'vue'

@Component({
  components: { BankTxImportTable, Page }
})
export default class BankTxImportPage extends Vue {
  breadCrumbData = new BreadCrumbData([{ textKey: 'finance.label' }, { textKey: 'money-movement.import.label.short' }])
}
</script>
