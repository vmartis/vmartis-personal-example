import FilterData from '@/modules/common/mixins/filterData'
import SortData from '@/modules/common/mixins/sortData'

export default class PageData {
  filterData: FilterData
  sortData: SortData
  activeTab: number | null

  constructor (filterData?: FilterData, sortData?: SortData) {
    this.filterData = filterData || new FilterData()
    this.sortData = sortData || new SortData()
    this.activeTab = null
  }
}
