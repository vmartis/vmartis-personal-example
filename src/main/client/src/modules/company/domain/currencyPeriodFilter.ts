import Range from '@/modules/common/components/form/range'

export class CurrencyPeriodFilter {
  currency: string | null = null
  range: Range | null = null

  constructor (currency: string | null, range: Range | null) {
    this.currency = currency
    this.range = range
  }
}
