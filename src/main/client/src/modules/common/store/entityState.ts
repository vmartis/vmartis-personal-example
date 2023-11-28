import { Entity } from '@/modules/common/domain/entity'

export default class EntityState<T extends Entity> {
  items: Array<T> = []
  latestItems: Array<T> = []
  selectedItems: Array<T> = []
  selected: T | null = null
  parameters: any
  parametersLatest: any
}
