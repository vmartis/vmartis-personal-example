/**
 * minValue validator for supporting comma "," as decimal
 */
const minValue = function (value: any, ref: any) {
  const min = ref[0]
  const valueString = value + ''

  if (Array.isArray(valueString) || valueString === null || valueString === undefined || valueString === '') {
    return false
  }

  return Number(valueString.replace(',', '.')) >= min
}

export default minValue
