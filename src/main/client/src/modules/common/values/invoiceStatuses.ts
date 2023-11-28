import { enumToStaticValue } from '@/utils'
import { InvoiceStatus } from '@/modules/invoice/type/InvoiceStatus'

export default enumToStaticValue(InvoiceStatus, 'invoice-status')
