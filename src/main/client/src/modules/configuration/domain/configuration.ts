import { Entity } from '@/modules/common/domain/entity'
import InvoiceSetting from '@/modules/configuration/domain/invoiceSetting'
import ApplicationSetting from '@/modules/configuration/domain/applicationSetting'

class Configuration extends Entity {
  invoice: InvoiceSetting
  application: ApplicationSetting

  constructor (data: any) {
    super(data)

    this.invoice = new InvoiceSetting(data.invoice)
    this.application = new ApplicationSetting(data.application)
  }
}

export default Configuration
