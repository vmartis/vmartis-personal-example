import notificationService from '@/modules/common/services/notificationService'
import documentService from '@/modules/document/services/documentService'
import Document from '@/modules/document/domain/document'
import Component, { mixins } from 'vue-class-component'
import { Prop } from 'vue-property-decorator'
import confirmMixin from '@/modules/common/mixins/confirmMixin'

@Component
export default class DocumentMixin extends mixins(confirmMixin) {
  @Prop({ type: Boolean, default: true }) deleteConfirm?: boolean

  fileIcon (fileName: string) {
    const ext = (fileName.split('.').pop() || '').toLowerCase()
    switch (ext) {
      case 'pdf':
        return 'fa-file-pdf'
      case 'xls':
      case 'xlsx':
      case 'csv':
        return 'fa-file-excel'
      case 'doc':
      case 'docx':
        return 'fa-file-word'
      case 'jpeg':
      case 'jpg':
      case 'png':
        return 'fa-file-image'
      default:
        return 'fa-file'
    }
  }

  download (document: Document) {
    documentService.download('api/document/' + document.id, document.fileName!)
  }

  async deleteDocument (document: Document) {
    if (this.deleteConfirm) {
      this.confirm('document.delete.confirmation', [document.fileName]).then(() => {
        if (this.doDelete(document)) {
          notificationService.success('document.delete.success')
        }
      })
    } else {
      this.doDelete(document)
    }
  }

  async doDelete (document: Document) {
    return this.$emit('delete-document', document)
  }
}
