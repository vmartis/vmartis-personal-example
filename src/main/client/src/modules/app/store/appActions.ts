import {
  APP_LOADING_DATA_DISABLE,
  APP_LOADING_DATA_ENABLE,
  APP_PAGE_DATA_SAVE,
  APP_SUBMIT_PROTECTION_DISABLE,
  APP_SUBMIT_PROTECTION_ENABLE,
  APP_UPDATE_PAGE_GROUP
} from './mutationTypes'
import PageData from '@/modules/app/store/pageData'

export default {
  async protect ({ commit }: { commit: Function }) {
    await commit(APP_SUBMIT_PROTECTION_ENABLE)
  },
  async unprotect ({ commit }: { commit: Function }) {
    await commit(APP_SUBMIT_PROTECTION_DISABLE)
  },
  async loadingDataEnable ({ commit }: { commit: Function }, id: string) {
    await commit(APP_LOADING_DATA_ENABLE, id)
  },
  async loadingDataDisable ({ commit }: { commit: Function }, id: string) {
    await commit(APP_LOADING_DATA_DISABLE, id)
  },
  async savePageData ({ commit }: { commit: Function }, { id, data }: { id: string, data: PageData }) {
    await commit(APP_PAGE_DATA_SAVE, { id, data })
  },
  async updatePageGroup ({ commit }: { commit: Function }, pageGroup: { id: string, data: PageData }) {
    await commit(APP_UPDATE_PAGE_GROUP, pageGroup)
  }
}
