import _ from 'lodash'
import moment, { isMoment } from 'moment'
import numeral from 'numeral'
import StaticValue from '@/modules/common/values/staticValue'
import Labeled from '@/modules/common/values/labeled'
import i18n from '@/i18n/i18n'

export const DATE_PATTERN = 'DD.MM.YYYY'

export const FORMAT_TABLE_DATE_LABEL = (value?: moment.Moment) => {
  return value ? value.format('MMMM (YYYY)') : null
}

export const FORMAT_DATE_PERIOD = (value?: moment.Moment) => {
  return value ? value.format('YYYY-MM') : null
}

export const FORMAT_DATE_WEEK_OF_YEAR = (value?: moment.Moment) => {
  return value ? value.format('W') : null
}

export const FORMAT_DATE_PERIOD_SLASH = (value?: moment.Moment) => {
  return value ? value.format('MM/YYYY') : null
}

export const FORMAT_DATE = (value?: moment.Moment) => {
  return value ? value.format(DATE_PATTERN) : null
}

export const FORMAT_DATE_DAY = (value?: moment.Moment) => {
  return value ? value.format('dddd') : null
}

export const FORMAT_DATE_TIME = (value?: moment.Moment) => {
  return value ? moment(value).format('DD.MM.YYYY HH:mm') : null
}

export const FORMAT_SYSTEM_DATE_TIME = (value?: moment.Moment) => {
  return value ? moment(value).format('YYYY-MM-DD HH:mm:ss') : null
}

export const FORMAT_TIME = (value?: moment.Moment) => {
  return value ? moment(value).format('HH:mm') : null
}

export const FORMAT_DURATION_TIME = (value?: moment.Duration) => {
  return value ? value.format('HH:mm', { trim: false }) : null
}

export const FORMAT_SYSTEM_DATE = (value?: moment.Moment | null) => {
  return value ? moment(value).format('YYYY-MM-DD') : null
}

export const FORMAT_CURRENCY = (value: number, currency: string, currencies: Array<Labeled>) => {
  const currencyItem = currency ? _.find(currencies, { value: currency }) : null
  return numeral(value).format('0,0.00') + (currencyItem ? (' ' + currencyItem.label) : '')
}

export const FORMAT_NUMBER = (value: number, precision: number = 0, forcePrecision = false) => {
  if (_.isUndefined(value) || value === null) {
    return null
  } else {
    const format = forcePrecision ? '0,0.' + _.repeat('0', precision || 0) + '' : '0,0[.][' + _.repeat('0', precision || 0) + ']'
    return numeral(value).format(format)
  }
}

export const normalizeFilename = (value?: string) => {
  let normalizedValue = (value || '').toLowerCase().normalize('NFD').replace(/[^\w\s-.,]/g, '').replace(/[,.\s]+/g, '-')
  // remove double dash
  while (normalizedValue.match(/.*[-][-].*/g)) {
    normalizedValue = normalizedValue.replace(/[-][-]/g, '-')
  }
  return normalizedValue
}

export const normalize = (value?: string) => {
  return (value || '').toLowerCase().normalize('NFD').replace(/[^\w]/g, '')
}

/**
 * Compares two string involving ascendant characters.
 * @param sortBy property to be compared
 * @returns function to be used in lodash sortBy
 */
export const localeCompare = (sortBy: string | ((item: any) => string)) => {
  if (_.isString(sortBy)) {
    return (a: any, b: any) => {
      const propertyA = _.property(sortBy)(a) as string
      const propertyB = _.property(sortBy)(b) as string
      return propertyA.localeCompare(propertyB, i18n.locale, { numeric: false })
    }
  } else {
    return (a: any, b: any) => sortBy(a).localeCompare(sortBy(b)!, i18n.locale, { numeric: false })
  }
}

/**
 * Compare function which support localCompare of strings.
 * @param sortBy property to be compared
 * @returns function to be used in lodash sortBy
 */
export const compareFunction = (sortBy: string) => {
  return (a: any, b: any) => {
    const propertyA = _.property(sortBy)(a)
    const propertyB = _.property(sortBy)(b)
    if (_.isNil(propertyB)) {
      return 1
    } else if (_.isNil(propertyA)) {
      return -1
    } else if (_.isString(propertyA) && _.isString(propertyB)) {
      return propertyA.localeCompare(propertyB, i18n.locale, { numeric: false })
    } else if (_.isNumber(propertyA) && _.isNumber(propertyB)) {
      const numA = propertyA as number
      const numB = propertyB as number
      return numA === numB ? 0 : (numA > numB ? 1 : -1)
    } else if (isMoment(propertyA) && isMoment(propertyB)) {
      const momentA = propertyA as moment.Moment
      const momentB = propertyB as moment.Moment
      return momentA.isSame(momentB) ? 0 : (momentA.isAfter(momentB) ? 1 : -1)
    } else {
      return 1
    }
  }
}

export const DOWNLOAD_BLOB = (blob: any, fileName: string) => {
  const filename = fileName
  const result = window.document.createElement('a')
  result.href = window.URL.createObjectURL(blob)
  result.download = filename
  result.click()
}

export const enumToStaticValue = (sourceEnum: any, enumName: string) => {
  return _(sourceEnum).keys().map(enumValue => new StaticValue(enumValue, `value.${enumName}.label.${enumValue}`)).value()
}

/**
 * Formats file size. !!!! Taken from vee-validate library !!!!
 *
 * @param {Number|String} size
 */
export const FORMAT_FILE_SIZE = (size: number): string => {
  const units = ['Byte', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB']
  const threshold = 1024
  size = Number(size) * threshold
  const i = size === 0 ? 0 : Math.floor(Math.log(size) / Math.log(threshold))
  // @ts-ignore
  return `${(size / Math.pow(threshold, i)).toFixed(2) * 1} ${units[i]}`
}
