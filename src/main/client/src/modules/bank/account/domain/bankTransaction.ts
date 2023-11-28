import { Entity } from '@/modules/common/domain/entity'
import moment from 'moment'

class BankTransaction extends Entity {
  transactionId?: string
  date?: moment.Moment | null
  amount?: number
  currency?: string
  type?: string
  correspondingAccountNumber?: string
  correspondingAccountName?: string
  correspondingBankId?: string
  correspondingBankName?: string
  constantSymbol?: string
  variableSymbol?: string
  specificSymbol?: string
  userIdentification?: string
  message?: string
  submittedBy?: string
  detail?: string
  detail2?: string

  constructor (data: any) {
    super(data)
    this.transactionId = data.transactionId || null
    this.date = this.convertDate(data.date)
    this.amount = data.amount || null
    this.currency = data.currency || null
    this.type = data.type || null
    this.correspondingAccountNumber = data.correspondingAccountNumber || null
    this.correspondingAccountName = data.correspondingAccountName
    this.correspondingBankId = data.correspondingBankId
    this.correspondingBankName = data.correspondingBankName
    this.constantSymbol = data.constantSymbol
    this.variableSymbol = data.variableSymbol
    this.specificSymbol = data.specificSymbol
    this.userIdentification = data.userIdentification
    this.message = data.message
    this.submittedBy = data.submittedBy
    this.detail = data.detail
    this.detail2 = data.detail2
  }

  formatCorrespondingAccount () {
    if (!this.correspondingAccountNumber) {
      return null
    } else if (!this.correspondingBankId) {
      return this.correspondingAccountNumber
    } else {
      return `${this.correspondingAccountNumber} / ${this.correspondingBankId}`
    }
  }
}

export default BankTransaction
