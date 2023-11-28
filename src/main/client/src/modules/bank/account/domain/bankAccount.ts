import { Entity } from '@/modules/common/domain/entity'
import Subject from '@/modules/subject/domain/subject'

class BankAccount extends Entity {
  subject?: Subject | null
  name?: string
  accountId?: string
  bankId?: string
  currency?: string
  iban?: string
  bic?: string
  active?: boolean
  formatId?: string
  labelDescription?: string

  constructor (data: any) {
    super(data, 'id', 'name')
    this.name = data.name || null
    this.accountId = data.accountId || null
    this.bankId = data.bankId || null
    this.currency = data.currency || null
    this.iban = data.iban || null
    this.bic = data.bic || null
    this.active = data.active

    this.formatId = `${this.accountId} / ${this.bankId}`

    // do not set label description if is same as name
    if (this.name !== this.formatId) {
      this.labelDescription = `${this.formatId}`
    }
  }
}

export default BankAccount
