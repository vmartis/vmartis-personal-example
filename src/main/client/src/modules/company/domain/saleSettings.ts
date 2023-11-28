import _ from 'lodash'
import PaymentType from '@/modules/paymenttype/domain/paymentType'

class SaleSettings {
  paymentType?: PaymentType | null
  dueDateOffset?: number | null
  deliveryNoteEnabled?: boolean | null

  constructor (data: any) {
    _.merge(this, data)
    this.paymentType = data.paymentType ? new PaymentType(data.paymentType) : null
  }
}

export default SaleSettings
