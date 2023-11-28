import Vue from 'vue'
import { DOWNLOAD_BLOB } from '@/utils'
import { HttpResponse } from 'vue-resource/types/vue_resource'
import documentResource from '@/modules/document/services/documentResource'

export default {
  async downloadBlob (url: string) {
    const response = await (<any>Vue).http.get(url, { responseType: 'blob' })
    return response.blob()// resolve to Blob
  },
  download (url: string, fileName: string) {
    (<any>Vue).http.get(url, { responseType: 'blob' })
      // resolve to Blob
      .then((response: HttpResponse) => response.blob())
      .then((data: Blob) => DOWNLOAD_BLOB(data, fileName))
  },
  async generateDocument (id: number, templateId: number, fileName: string) {
    await documentResource.generateDocument({ id, templateId })
      .then((response: HttpResponse) => response.blob())
      .then((data: Blob) => DOWNLOAD_BLOB(data, fileName))
  },
  async generatePdfDocument (id: number, templateId: number, fileName: string) {
    await documentResource.generatePdfDocument({ id, templateId })
      .then((response: HttpResponse) => response.blob())
      .then((data: Blob) => DOWNLOAD_BLOB(data, fileName))
  },
  async generateMultiPdfDocument (ids: Array<number>, templateId: number, fileName: string) {
    await documentResource.generateMultiPdfDocument({}, { ids, templateId })
      .then((response: HttpResponse) => response.blob())
      .then((data: Blob) => DOWNLOAD_BLOB(data, fileName))
  }
}
