import Vue from 'vue'
import { API_ROOT } from '@/config'
import EntityResource from '@/modules/common/services/entityResource'

const contextPath = 'company-branch'

const customActions = {
  updatePosition: { method: 'PUT', url: API_ROOT + contextPath + '/{id}/position/{position}' }
}

// @ts-ignore
export default (Vue.resource(API_ROOT + contextPath + '{/id}', {}, customActions)) as EntityResource
