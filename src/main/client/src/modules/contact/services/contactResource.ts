import Vue from 'vue'
import { API_ROOT } from '@/config'
import EntityResource from '@/modules/common/services/entityResource'

const contextPath = 'contact'

// @ts-ignore
export default (Vue.resource(API_ROOT + contextPath + '{/id}', {})) as EntityResource
