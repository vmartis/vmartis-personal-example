import Vue from 'vue'
import { API_ROOT } from '@/config'

const context = 'document'

export default (<any>Vue).resource(API_ROOT + context + '/{id}', {})
