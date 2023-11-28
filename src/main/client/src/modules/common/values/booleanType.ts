import { enumToStaticValue } from '@/utils'

export enum BooleanType {
  YES = 'YES',
  NO = 'NO'
}

export const booleanTypes = enumToStaticValue(BooleanType, 'boolean-type')
