import _ from 'lodash'
import { PaymentMethod } from '@/modules/invoice/type/PaymentMethod'
import BankAccount from '@/modules/bank/account/domain/bankAccount'
import Document from '@/modules/document/domain/document'
import { VatCalculationType } from '@/modules/configuration/type/VatCalculationType'

export default class InvoiceSetting {
  defaultPaymentMethod?: PaymentMethod
  defaultDueDateOffset?: number
  defaultCurrency?: string
  defaultBankAccount?: BankAccount | null
  logoDocument?: Document | null
  stampDocument?: Document | null
  vatCalculationType?: VatCalculationType

  constructor (data: any) {
    _.merge(this, data)

    this.defaultBankAccount = data.defaultBankAccount ? new BankAccount(data.defaultBankAccount) : null
    this.logoDocument = data.logoDocument ? new Document(data.logoDocument) : null
    this.stampDocument = data.stampDocument ? new Document(data.stampDocument) : null
  }
}
