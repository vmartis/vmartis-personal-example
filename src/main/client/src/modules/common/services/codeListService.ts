import CodeList from '@/modules/common/domain/codeList'
import EntityResource from '@/modules/common/services/entityResource'

export default class CodeListService<T extends CodeList> {
  resource: EntityResource

  constructor (repository: EntityResource) {
    this.resource = repository
  }

  async findAll () {
    const response = await this.resource.query()
    if (response.ok) {
      return response.data.map((itemData: any) => this.createItem(itemData))
    } else {
      return null
    }
  }

  async create (item: T) {
    const response = await this.resource.save({}, item)
    if (response.ok) {
      return this.createItem(response.data)
    } else {
      return null
    }
  }

  async update (item: T) {
    const response = await this.resource.update({}, item)
    if (response.ok) {
      return this.createItem(response.data)
    } else {
      return null
    }
  }

  async delete (id: bigint) {
    return this.resource.delete({ id })
  }

  async updatePosition (id: bigint, position: bigint) {
    const response = await this.resource.updatePosition({ id, position }, {})
    return response.ok
  }

  createItem (data: any) {
    return new CodeList(data)
  }
}
