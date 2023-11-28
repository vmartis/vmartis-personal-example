import Component from 'vue-class-component'
import Vue from 'vue'
import { State } from 'vuex-class'
import PageDataSet from '@/modules/app/store/pageDataSet'
import PageData from '@/modules/app/store/pageData'
import FilterData from '@/modules/common/mixins/filterData'
import SortData from '@/modules/common/mixins/sortData'

/**
 * Mixin for page data cache. It store and restore filter/sort/tab of the page.
 */
@Component
export default class PageDataMixin extends Vue {
  pageDataId?: string | null

  @State('pageDataSet', { namespace: 'app' }) pageDataSet!: PageDataSet

  /**
   * Get page data from storage
   */
  get pageData () : PageData | null {
    return this.pageDataId ? this.pageDataSet[this.pageDataId] : null
  }

  storePageData (filterData: FilterData, sortData: SortData, activeTab: number | null) {
    if (this.pageDataId) {
      const newPageData = this.pageData || new PageData()
      newPageData.filterData = filterData
      newPageData.sortData = sortData
      newPageData.activeTab = activeTab
      this.$store.dispatch('app/savePageData', { id: this.pageDataId, data: newPageData })
    }
  }
}
