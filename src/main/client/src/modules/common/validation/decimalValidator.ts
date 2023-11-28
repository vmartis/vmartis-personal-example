/**
 * Decimal2 validator for supporting comma "," as decimal
 */
import { ValidationRule } from 'vee-validate/dist/types/types'

const decimal2 = function (value: any, { decimals }: {decimals: any}) {
  const valueString = value + ''

  if (Array.isArray(valueString)) {
    return false
  }

  if (valueString === null || valueString === undefined || valueString === '') {
    return true
  }

  // if is 0.
  if (Number(decimals) === 0) {
    return /^-?\d*$/.test(valueString)
  }

  const regexPart = decimals === '*' ? '+' : ('{1,' + decimals + '}')
  const regex = new RegExp(('^-?\\d*([,.]\\d' + regexPart + ')?$'))

  if (!regex.test(valueString)) {
    return false
  }

  const parsedValue = parseFloat(valueString.replace(',', '.'))

  // eslint-disable-next-line
  return parsedValue === parsedValue
}

export default { validate: decimal2, params: ['decimals'] } as ValidationRule
