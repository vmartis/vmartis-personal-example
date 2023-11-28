import _ from 'lodash'
import { Entity } from '@/modules/common/domain/entity'
import EntityResource from '@/modules/common/services/entityResource'

export default class EntityService<T extends Entity> {
  resource: EntityResource
  ConstructorFn: new (data: any) => T

  constructor (repository: EntityResource, constructorFn: new (data: any) => T) {
    this.resource = repository
    this.ConstructorFn = constructorFn
  }

  async find (id: string) {
    const response = await this.resource.get({ id })
    if (response.ok) {
      return this.createItem(response.data)
    } else {
      return null
    }
  }

  async findAll (queryParams: any) {
    const response = await this.resource.query(queryParams)
    if (response.ok) {
      return response.data.map((itemData: any) => this.createItem(itemData))
    } else {
      return null
    }
  }

  async create (item: T) {
    const response = await this.resource.save({}, _.omitBy(item, _.isNil))
    if (response.ok) {
      return this.createItem(response.data || {})
    } else {
      return null
    }
  }

  async update (item: T) {
    const response = await this.resource.update({}, _.omitBy(item, _.isNil))
    if (response.ok) {
      return this.createItem(response.data)
    } else {
      return null
    }
  }

  async updateBulk (items: Array<T>) {
    await this.resource.updateBulk({}, _.map(items, item => _.omitBy(item, _.isNil)))
  }

  async delete (id: any) {
    return this.resource.delete({ id })
  }

  async updatePosition (id: bigint, position: bigint) {
    const response = await this.resource.updatePosition({ id, position }, {})
    return response.ok
  }

  createItem (data: any) {
    return new this.ConstructorFn(data)
  }
}
