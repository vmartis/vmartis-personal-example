import _ from 'lodash'
import Component, { mixins } from 'vue-class-component'
import FilterData from '@/modules/common/mixins/filterData'
import { FORMAT_SYSTEM_DATE } from '@/utils'
import moment from 'moment'
import pageDataMixin from '@/modules/common/mixins/pageDataMixin'
import Range from '../components/form/range'
import SortData from '@/modules/common/mixins/sortData'
import { Watch } from 'vue-property-decorator'

@Component
export default class BookmarkableComponent extends mixins(pageDataMixin) {
  public sortData = new SortData()
  activeTabIndex: number | null = null
  filter = new FilterData()

  updateQuery () {
    const newQuery: any = {}
    // remove all nulls and undefined and add prefix 'o_';
    _(this.sortData).pickBy((value) => !_.isNil(value)).forEach((value: any, key) => {
      newQuery['o_' + key] = encodeURI(value)
    })
    // remove all nulls and undefined and add prefix 'f_';
    _(this.filter).pickBy((value) => !_.isNil(value)).forEach((value: any, key) => {
      if (_.isString(value)) {
        newQuery['fs_' + key] = encodeURI(value)
      } else if (_.isNumber(value)) {
        newQuery['fn_' + key] = encodeURI(value + '')
      } else if (_.isBoolean(value)) {
        newQuery['fb_' + key] = encodeURI(value + '')
      } else if (moment.isMoment(value)) {
        newQuery['fd_' + key] = FORMAT_SYSTEM_DATE(value)
      } else if (value instanceof Range) {
        if (value.from && value.from.isValid()) {
          newQuery['frf_' + key] = FORMAT_SYSTEM_DATE(value.from)
        }
        if (value.to && value.to.isValid()) {
          newQuery['frt_' + key] = FORMAT_SYSTEM_DATE(value.to)
        }
      } else if (_.isArray(value) && value.length > 0) {
        newQuery['fa_' + key] = encodeURI(value.toString())
      }
    })
    if (!_.isNil(this.activeTabIndex)) {
      newQuery.tab = this.activeTabIndex
    }
    this.$router.push({ query: newQuery }).catch(reason => {
      // do nothing
    })
  }

  restoreFilter () {
    const query = this.$route.query
    _(query).forEach((value: any, key: string) => {
      if (key === 'o_by') {
        this.sortData.by = decodeURI(value)
      } else if (key === 'o_asc') {
        this.sortData.asc = JSON.parse(decodeURI(value))
      } else if (key.startsWith('fs_')) {
        this.filter[key.substring(3)] = decodeURI(value)
      } else if (key.startsWith('fn_')) {
        this.filter[key.substring(3)] = Number(decodeURI(value))
      } else if (key.startsWith('fb_')) {
        this.filter[key.substring(3)] = JSON.parse(decodeURI(value))
      } else if (key.startsWith('fd_')) {
        this.filter[key.substring(3)] = moment(decodeURI(value))
      } else if (key.startsWith('fa_')) {
        this.filter[key.substring(3)] = _.map(value.split(','), item => _.isNaN(Number(item)) ? item : Number(item))
      } else if (key.startsWith('frf_')) {
        const propName = key.substring(4)
        if (!this.filter[propName]) {
          this.filter[propName] = new Range()
        }
        this.filter[propName].from = moment(decodeURI(value))
      } else if (key.startsWith('frt_')) {
        const propName = key.substring(4)
        if (!this.filter[propName]) {
          this.filter[propName] = new Range()
        }
        this.filter[propName].to = moment(decodeURI(value))
      } else if (key === 'tab') {
        this.activeTabIndex = Number(decodeURI(value))
      }
    })
    this.storePageData(this.filter, this.sortData, this.activeTabIndex)
  }

  // method to be overridden
  defaultFilter () {
  }

  // method to be overridden
  initData () {
  }

  // method to be overridden
  afterSetup () {
  }

  setupFilter () {
    if (_.isEmpty(this.$route.query)) {
      // restore data from session if are available
      if (this.pageData) {
        this.filter = this.pageData.filterData
        this.sortData = this.pageData.sortData
        this.activeTabIndex = this.pageData.activeTab
      } else {
        this.defaultFilter()
      }
      this.updateQuery()
    } else {
      this.restoreFilter()
    }
  }

  @Watch('filter', { deep: true })
  onFilterChange () {
    this.updateQuery()
    this.storePageData(this.filter, this.sortData, this.activeTabIndex)
  }

  @Watch('sortData', { deep: true })
  onSortDataChange () {
    this.updateQuery()
    this.storePageData(this.filter, this.sortData, this.activeTabIndex)
  }

  async created () {
    await this.initData()
    this.setupFilter()
    this.afterSetup()
  }
}
