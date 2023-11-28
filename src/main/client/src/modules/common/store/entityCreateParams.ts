import { Entity } from '@/modules/common/domain/entity'

export default class EntityCreateParams<T extends Entity> {
  entity: T | Array<T>
  doFetchAfter: boolean
  doFetchLatestAfter: boolean

  constructor (entity: T | Array<T>, doFetchAfter: boolean = true, doFetchLatestAfter: boolean = false) {
    this.entity = entity
    this.doFetchAfter = doFetchAfter
    this.doFetchLatestAfter = doFetchLatestAfter
  }
}
