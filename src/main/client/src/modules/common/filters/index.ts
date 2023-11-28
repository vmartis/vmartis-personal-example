import codeListValueFilter from './codeListValueFilter'
import codeListPropertyFilter from './codeListPropertyFilter'
import dateFilter from './dateFilter'
import dateDayFilter from './dateDayFilter'
import currencyFilter from './currencyFilter'
import timeFilter from './timeFilter'
import dateMonthFilter from './dateMonthFilter'
import dateTimeFilter from './dateTimeFilter'
import durationTimeFilter from './durationTimeFilter'
import fileSizeFilter from './fileSizeFilter'
import linkFilter from '@/modules/common/filters/linkFilter'
import numberFilter from '@/modules/common/filters/numberFilter'
import optionalFilter from '@/modules/common/filters/optionalFilter'
import translate from './translateFilter'
import truncateFilter from './truncateFilter'
import urlFilter from './urlFilter'
import Vue from 'vue'

export default () => {
  Vue.filter('codeListValue', codeListValueFilter)
  Vue.filter('codeListProp', codeListPropertyFilter)
  Vue.filter('currency', currencyFilter)
  Vue.filter('date', dateFilter)
  Vue.filter('dateDay', dateDayFilter)
  Vue.filter('dateTime', dateTimeFilter)
  Vue.filter('dateMonth', dateMonthFilter)
  Vue.filter('durationTime', durationTimeFilter)
  Vue.filter('fileSize', fileSizeFilter)
  Vue.filter('number', numberFilter)
  Vue.filter('time', timeFilter)
  Vue.filter('translate', translate)
  Vue.filter('truncate', truncateFilter)
  Vue.filter('url', urlFilter)
  Vue.filter('link', linkFilter)
  Vue.filter('optional', optionalFilter)
}
