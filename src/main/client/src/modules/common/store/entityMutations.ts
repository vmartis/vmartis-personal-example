import { MutationTree } from 'vuex'
import {
  CLEAR_ALL,
  CLEAR_LATEST,
  CLEAR_SELECTED,
  CLEAR_SELECTED_ITEMS, ENRICH, ENRICH_ALL,
  LIST,
  LIST_LATEST,
  SELECT,
  SELECT_ITEMS
} from './entityMutationTypes'
import EntityState from './entityState'
import { Entity } from '@/modules/common/domain/entity'

const mutations: MutationTree<EntityState<Entity>> = {
  [LIST] (state, action: any) {
    state.items = action.items
    state.parameters = action.parameters
  },
  [LIST_LATEST] (state, action) {
    state.latestItems = action.items
    state.parametersLatest = action.parameters
  },
  [SELECT] (state, action: any) {
    state.selected = action.item
  },
  [SELECT_ITEMS] (state, action: any) {
    state.selectedItems = action.items || []
  },
  [CLEAR_ALL] (state) {
    state.items = []
    state.parameters = null
  },
  [CLEAR_LATEST] (state) {
    state.latestItems = []
    state.parameters = null
  },
  [CLEAR_SELECTED] (state) {
    state.selected = null
  },
  [CLEAR_SELECTED_ITEMS] (state) {
    state.selectedItems = []
  },
  [ENRICH] (state, action) {
    if (state.selected) {
      state.selected.enrich(action.data)
    }
  },
  [ENRICH_ALL] (state, action) {
    state.items.forEach(item => item.enrich(action.data))
  }
}

export const createMutations = <E extends Entity, ES extends EntityState<E>> () => {
  return {
    ...mutations
  } as MutationTree<ES>
}

export default mutations
