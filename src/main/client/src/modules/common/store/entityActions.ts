import _ from 'lodash'
import * as types from './entityMutationTypes'
import { ActionTree } from 'vuex'
import RootState from '@/store/rootState'
import EntityService from '@/modules/common/services/entityService'
import EntityState from '@/modules/common/store/entityState'
import UpdatePosition from '@/modules/common/domain/updatePosition'
import EntityFetchParams from '@/modules/common/store/entityFetchParams'
import EntityCreateParams from '@/modules/common/store/entityCreateParams'
import { Entity } from '@/modules/common/domain/entity'
import EntityDeleteParams from '@/modules/common/store/entityDeleteParams'

export default (service: EntityService<any>, loadingId: string) => {
  const actions: ActionTree<EntityState<any>, RootState> = {
    async getAll ({ state, dispatch, commit }, entityFetchParams: EntityFetchParams) {
      if (!state.items || state.items.length === 0 || (entityFetchParams && entityFetchParams.force)) {
        await dispatch('app/loadingDataEnable', loadingId, { root: true })
        await dispatch('clearAll')
        const fetchParams = entityFetchParams ? entityFetchParams.parameters : null
        const entities = await service.findAll(fetchParams)
        await commit(types.LIST, {
          items: entities,
          parameters: fetchParams
        })
        dispatch('app/loadingDataDisable', loadingId, { root: true })
      }
    },
    async getLatest ({ state, dispatch, commit }, entityFetchParams: EntityFetchParams) {
      if (!state.latestItems || state.latestItems.length === 0 || (entityFetchParams && entityFetchParams.force)) {
        dispatch('app/loadingDataEnable', loadingId + '-latest', { root: true })
        const fetchParams = entityFetchParams ? entityFetchParams.parameters : null
        const entities = await service.findAll(fetchParams)
        await commit(types.LIST_LATEST, {
          items: entities,
          parameters: fetchParams
        })
        dispatch('app/loadingDataDisable', loadingId + '-latest', { root: true })
      }
    },
    async select ({ dispatch, commit }, id) {
      await commit(types.SELECT, {
        item: null
      })
      dispatch('app/loadingDataEnable', loadingId + '-select', { root: true })
      const entity = await service.find(id)
      await commit(types.SELECT, {
        item: entity
      })
      dispatch('app/loadingDataDisable', loadingId + '-select', { root: true })
      return entity
    },
    async selectItems ({ dispatch, commit }, items) {
      await commit(types.SELECT_ITEMS, {
        items
      })
    },
    async refresh ({ state, dispatch }) {
      await dispatch('getAll', new EntityFetchParams(true, state.parameters))
    },
    async refreshSelected ({ state, dispatch }) {
      if (state.selected) {
        await dispatch('select', state.selected.id)
      }
    },
    async refreshLatest ({ state, dispatch }) {
      await dispatch('getLatest', new EntityFetchParams(true, state.parametersLatest))
    },
    async create ({ dispatch, commit }, entityCreateParams: EntityCreateParams<Entity>) {
      const newEntity = await service.create(entityCreateParams.entity)
      if (newEntity) {
        if (entityCreateParams.doFetchAfter) {
          await dispatch('refresh')
        }
        if (entityCreateParams.doFetchLatestAfter) {
          await dispatch('refreshLatest')
        }
        return newEntity
      } else {
        return false
      }
    },
    /**
     * Update action support single and multiple upload
     */
    async update ({ dispatch, commit }, entityCreateParams: EntityCreateParams<Entity>) {
      const isBulk = _.isArray(entityCreateParams.entity)
      const updatedEntity = isBulk ? await service.updateBulk(entityCreateParams.entity as Array<Entity>) : await service.update(entityCreateParams.entity)
      if (updatedEntity || isBulk) {
        if (entityCreateParams.doFetchAfter) {
          await dispatch('refresh')
          await dispatch('refreshSelected')
        }
        if (entityCreateParams.doFetchLatestAfter) {
          await dispatch('refreshLatest')
        }
        return updatedEntity || isBulk
      } else {
        return false
      }
    },
    async delete ({ dispatch, commit }, entityDeleteParams: EntityDeleteParams) {
      const response = await service.delete(entityDeleteParams.id)
      if (response.ok) {
        if (entityDeleteParams.doFetchAfter) {
          await dispatch('refresh')
        }
        if (entityDeleteParams.doFetchLatestAfter) {
          await dispatch('refreshLatest')
        }
        return true
      } else {
        return false
      }
    },
    async enrich ({ state, commit }, data: any) {
      await commit(types.ENRICH, {
        data
      })
      return state.selected
    },
    async enrichAll ({ state, commit }, data: any) {
      await commit(types.ENRICH_ALL, {
        data
      })
      return state.items
    },
    async clearAll ({ commit }) {
      await commit(types.CLEAR_ALL)
    },
    async clearLatest ({ commit }) {
      await commit(types.CLEAR_LATEST)
    },
    async clearSelected ({ commit }) {
      await commit(types.CLEAR_SELECTED)
    },
    async clearSelectedItems ({ commit }) {
      await commit(types.CLEAR_SELECTED_ITEMS)
    },
    async updatePosition ({ dispatch, state, commit }, data: UpdatePosition) {
      const updated = await service.updatePosition(data.id, data.position)
      if (updated) {
        await dispatch('refresh')
        return true
      } else {
        return false
      }
    }
  }

  return actions
}
