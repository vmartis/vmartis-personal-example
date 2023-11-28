import {
  APP_LOADING_DATA_DISABLE,
  APP_LOADING_DATA_ENABLE,
  APP_PAGE_DATA_SAVE,
  APP_SUBMIT_PROTECTION_DISABLE,
  APP_SUBMIT_PROTECTION_ENABLE, APP_UPDATE_PAGE_GROUP
} from './mutationTypes'
import _ from 'lodash'
import { MutationTree } from 'vuex'
import AppState from '@/modules/app/store/AppState'
import PageData from '@/modules/app/store/pageData'

const mutations: MutationTree<AppState> = {
  async [APP_SUBMIT_PROTECTION_ENABLE] (state: any) {
    state.submitProtection = true
  },
  async [APP_SUBMIT_PROTECTION_DISABLE] (state: any) {
    state.submitProtection = false
  },
  async [APP_LOADING_DATA_ENABLE] (state, id: string) {
    state.loadingData.push(id)
  },
  async [APP_LOADING_DATA_DISABLE] (state, id: string) {
    state.loadingData = _.without(state.loadingData, id)
  },
  async [APP_PAGE_DATA_SAVE] (state, { id, data }: { id: string, data: PageData }) {
    state.pageDataSet[id] = data
  },
  async [APP_UPDATE_PAGE_GROUP] (state, pageGroup) {
    state.pageGroup = pageGroup || null
  }
}

export default mutations
