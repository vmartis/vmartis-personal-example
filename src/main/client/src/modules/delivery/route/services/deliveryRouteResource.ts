import Vue from 'vue'
import { API_ROOT } from '@/config'
import EntityResource from '@/modules/common/services/entityResource'

const contextPath = 'delivery-route'

const customActions = {
  updatePosition: { method: 'PUT', url: API_ROOT + contextPath + '/{id}/position/{position}' }
}

export default (<any>Vue).resource(API_ROOT + contextPath + '{/id}', {}, customActions) as EntityResource
