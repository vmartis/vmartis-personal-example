import { Entity } from '@/modules/common/domain/entity'
import { Moment } from 'moment'
import { BankTxImportStatus } from '@/modules/bank/transaction/import/type/bankTxImportStatus'
import { BankTxImportType } from '@/modules/bank/transaction/import/type/bankTxImportType'
import BankAccount from '@/modules/bank/account/domain/bankAccount'
import Document from '@/modules/document/domain/document'

class BankTxImport extends Entity {
  dateFrom?: Moment | null
  dateTo?: Moment | null
  status?: BankTxImportStatus
  type?: BankTxImportType
  successCount?: bigint
  failedCount?: bigint
  account?: BankAccount | null
  document?: Document | null

  constructor (data: any) {
    super(data)

    this.dateFrom = this.convertDate(data.dateFrom)
    this.dateTo = this.convertDate(data.dateTo)
    this.account = data.account ? new BankAccount(data.account) : null
    this.document = data.document ? new Document(data.document) : null
  }
}

export default BankTxImport
