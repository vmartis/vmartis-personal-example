/**
 * Interceptor for optimization of CREATE/UPDATE entity. It ensures not sending whole associated entity properties to
 * BE, just id and @type attribute.
 */

import _ from 'lodash'
import moment from 'moment'
import { HttpOptions } from 'vue-resource/types/vue_resource'
import { Entity, RELATIONS } from '@/modules/common/domain/entity'

const IGNORED_PROPS = ['_enriched']

export default () => {
  function optimizeArray (arr: Array<any>) {
    const result: Array<any> = []
    arr.forEach(item => {
      if (_.isArray(item)) {
        result.push(optimizeArray(item))
      } else if (_.isObject(item)) {
        result.push(optimizeProp(item))
      } else {
        result.push(item)
      }
    })
    return result
  }

  function optimizeObject (obj: any) {
    const result: any = {}
    _.mapKeys(obj, (value, key) => {
      // do not optimize, if property is marked as relation
      if (obj[RELATIONS] && obj[RELATIONS].indexOf(key) >= 0) {
        result[key] = value
      } else if (!IGNORED_PROPS.includes(key)) {
        result[key] = optimizeProp(value)
      }
    })
    return result
  }

  function optimizeProp (prop: any) {
    if (prop instanceof Entity) {
      return _.pick(prop, ['id', '@type'])
    } else if (_.isArray(prop)) {
      return optimizeArray(prop)
    } else if (moment.isMoment(prop)) {
      return prop
    } else if (_.isObjectLike(prop)) {
      return optimizeObject(prop)
    } else {
      return prop
    }
  }

  return (request: HttpOptions) => {
    if (request.method === 'POST' || request.method === 'PUT') {
      if (_.isArray(request.body)) {
        request.body = optimizeArray(request.body)
      } else {
        request.body = optimizeObject(request.body)
      }
    }
  }
}
