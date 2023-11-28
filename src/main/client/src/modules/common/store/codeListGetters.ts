import _ from 'lodash'
import { GetterTree } from 'vuex'
import RootState from '@/store/rootState'
import EntityState from '@/modules/common/store/entityState'
import CodeList from '@/modules/common/domain/codeList'
import ValueOrderData from '@/modules/common/mixins/valueOrderData'

const getters: GetterTree<EntityState<CodeList>, RootState> = {
  validItems: (state) => {
    return state.items.filter(item => item.valid)
  },
  all: (state) => {
    return state.items
  },
  valueOrder: (state) : ValueOrderData => {
    return _(state.items).map(item => ({ [item.value!]: item.order! })).reduce(_.extend) || {} as ValueOrderData
  }
}

export default getters
