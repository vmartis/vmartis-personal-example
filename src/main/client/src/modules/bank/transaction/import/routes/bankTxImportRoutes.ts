import bankTxImportPage from '@/modules/bank/transaction/import/components/bankTxImportPage.vue'

export default [
  {
    path: '/bank-transaction-import',
    component: bankTxImportPage,
    meta: {
      requiresLoggedIn: true,
      pageGroup: 'finance'
    }
  }
]
