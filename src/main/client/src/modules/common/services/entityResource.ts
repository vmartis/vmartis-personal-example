import { HttpResponse, ResourceMethods } from 'vue-resource/types/vue_resource'
import UpdatePosition from '@/modules/common/domain/updatePosition'

export default interface EntityResource extends ResourceMethods {
  updatePosition (data: UpdatePosition, content: any): HttpResponse

  updateBulk (params: any, data: Array<any>): void
}
