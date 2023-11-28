import _ from 'lodash'
import { ENRICHED, Entity } from '@/modules/common/domain/entity'
import EntityState from '@/modules/common/store/entityState'
import { GetterTree } from 'vuex'
import RootState from '@/store/rootState'
import EntityMap from '@/modules/common/domain/entityMap'

const getters: GetterTree<EntityState<Entity>, RootState> = {
  active: (state) => {
    return _.filter((state.items) || [], 'active')
  },
  all: (state) => {
    return state.items || []
  },
  allEnriched: (state, getters) => {
    return _.filter(getters.all, ENRICHED)
  },
  idMap: (state): EntityMap<Entity> => {
    return _.keyBy(state.items, 'id')
  },
  first: (state) => {
    return _.first(state.items)
  }
}

export const createGetters = <E extends Entity, ES extends EntityState<E>> () => {
  return {
    ...getters
  } as GetterTree<ES, RootState>
}

export default getters
