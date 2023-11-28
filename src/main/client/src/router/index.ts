import Vue from 'vue'
import Router from 'vue-router'
import appRoutes from '@/modules/app/routes/appRoutes'
import bankTxImport from '@/modules/bank/transaction/import/routes/bankTxImportRoutes'
import companyRoutes from '@/modules/company/routes/companyRoutes'
import contactRoutes from '@/modules/contact/routes/contactRoutes'
import financeRoutes from '@/modules/finance/routes/financeRoutes'
import inventoryRoutes from '@/modules/inventory/routes/inventoryRoutes'
import invoiceRoutes from '@/modules/invoice/routes/invoiceRoutes'
import itemRoutes from '@/modules/item/routes/itemRoutes'
import settingRoutes from '@/modules/setting/router/settingRoutes'
import moneyBoxRoutes from '@/modules/money/box/routes/moneyBoxRoutes'
import orderRoutes from '@/modules/order/routes/orderRoutes'
import orderStatementRoutes from '@/modules/order/statement/routes/orderStatementRoutes'
import stockItemRoutes from '@/modules/stock/item/routes/stockItemRoutes'
import stockMovementsRoutes from '@/modules/stock/movement/routes/stockMovementsRoutes'
import subjectRecordRoutes from '@/modules/record/routes/subjectRecordRoutes'
import takingRoutes from '@/modules/sale/routes/takingsRoutes'
import usersRoutes from '@/modules/user/routes/userRoutes'
import workerRoutes from '@/modules/worker/routes/workerRoutes'
import workingActivityRoutes from '@/modules/workingactivity/routes/workingActivityRoutes'
import workRecordRoutes from '@/modules/work/record/routes/workRecordRoutes'

Vue.use(Router)

const router = new Router({
  mode: 'hash',
  routes: [
    ...appRoutes,
    ...bankTxImport,
    ...companyRoutes,
    ...contactRoutes,
    ...financeRoutes,
    ...inventoryRoutes,
    ...invoiceRoutes,
    ...itemRoutes,
    ...moneyBoxRoutes,
    ...orderRoutes,
    ...orderStatementRoutes,
    ...settingRoutes,
    ...stockItemRoutes,
    ...stockMovementsRoutes,
    ...subjectRecordRoutes,
    ...takingRoutes,
    ...usersRoutes,
    ...workerRoutes,
    ...workingActivityRoutes,
    ...workRecordRoutes
  ]
})

export default router
