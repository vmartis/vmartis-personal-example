import { Entity } from '@/modules/common/domain/entity'

export default interface EntityMap<E extends Entity> {
  [key: string]: E
}
