import _ from 'lodash'
import { GetterTree } from 'vuex'
import RootState from '@/store/rootState'
import CompanyState from '@/modules/company/store/companyState'
import EntityMap from '@/modules/common/domain/entityMap'
import { Entity } from '@/modules/common/domain/entity'

const getters: GetterTree<CompanyState, RootState> = {
  activePartners: (state) => {
    return _.filter((state.partners) || [], 'active')
  },
  allPartners: (state) => {
    return state.partners
  },
  partnersIdMap: (state, getters): EntityMap<Entity> => {
    return _.keyBy(getters.allPartners, 'id')
  }
}

export default getters
