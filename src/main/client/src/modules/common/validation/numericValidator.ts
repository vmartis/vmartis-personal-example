const numericRegExp = /^[-]?[0-9]+$/

/**
 * Numeric2 validator supports also negative values.
 */
const numeric2 = function (value: any) {
  if (Array.isArray(value)) {
    return value.every(val => numericRegExp.test(String(val)))
  }

  return numericRegExp.test(String(value))
}

export default numeric2
