import Component, { mixins } from 'vue-class-component'
import bookmarkableComponent from '@/modules/common/mixins/bookmarkableComponent'

@Component
export default class TabsMixin extends mixins(bookmarkableComponent) {
  clearFilterOnTabChange = false
  activeTab (tabId: number) {
    return this.activeTabIndex === tabId
  }

  tabChanged (newTabIndex: number) {
    if (this.clearFilterOnTabChange) {
      Object.keys(this.filter).forEach(key => {
        this.filter[key] = null
      })
    }
    this.activeTabIndex = newTabIndex
    this.updateQuery()
    this.storePageData(this.filter, this.sortData, this.activeTabIndex)
  }
}
