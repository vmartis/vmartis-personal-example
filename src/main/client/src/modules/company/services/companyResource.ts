import Vue from 'vue'
import { API_ROOT } from '@/config'
import { ResourceMethods } from 'vue-resource/types/vue_resource'

const contextPath = 'company'

export default ((<any>Vue).resource(API_ROOT + contextPath + '{/id}', {})) as ResourceMethods
