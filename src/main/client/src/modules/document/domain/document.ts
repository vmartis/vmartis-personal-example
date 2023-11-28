import { Entity } from '@/modules/common/domain/entity'

class Document extends Entity {
  fileName?: string
  mimeType?: string
}

export default Document
