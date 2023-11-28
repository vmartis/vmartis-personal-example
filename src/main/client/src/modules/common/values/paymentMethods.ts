import { enumToStaticValue } from '@/utils'
import { PaymentMethod } from '@/modules/invoice/type/PaymentMethod'

export default enumToStaticValue(PaymentMethod, 'payment-method')
