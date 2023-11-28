import Vue from 'vue'
import { API_ROOT } from '@/config'
import EntityResource from '@/modules/common/services/entityResource'

export default (Vue as any).resource(API_ROOT + 'configuration', {}) as EntityResource
