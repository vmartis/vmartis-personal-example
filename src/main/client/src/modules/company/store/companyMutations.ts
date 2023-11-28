import { MutationTree } from 'vuex'
import { CLEAR_SELECTED, OWNED_GET, PARTNER_LIST, SELECT } from './companyMutationTypes'
import CompanyState from './companyState'
import { CLEAR_ALL } from '@/modules/common/store/entityMutationTypes'

const mutations: MutationTree<CompanyState> = {
  [SELECT] (state, action) {
    state.selected = action.item
  },
  [PARTNER_LIST] (state, action: any) {
    state.partners = action.items
  },
  [OWNED_GET] (state, action: any) {
    state.owned = action.item
  },
  [CLEAR_ALL] (state) {
    state.partners = []
  },
  [CLEAR_SELECTED] (state) {
    state.selected = null
  }
}

export default mutations
