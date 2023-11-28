import { enumToStaticValue } from '@/utils'

export enum VatCalculationType {
  SUM = 'SUM',
  ITEM = 'ITEM'
}

export const vatCalculationTypes = enumToStaticValue(VatCalculationType, 'vat-calculation')
