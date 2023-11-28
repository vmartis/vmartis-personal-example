<template>
  <div>
    <validation-provider :name="fieldId" :rules="validation" v-slot="{ errors }" slim mode="lazy">
      <template v-if="document">
        <img :src="imageUrl" v-if="image && preview" :height="previewHeight" :width="previewWidth" @click.prevent="!disableDownload && download(document)" :role="disableDownload ? '' : 'button'">
        <button class="btn btn-link p-0" @click.prevent="download(document)" v-else>
          <i :class="[fileIcon(document.fileName)]" class="far" :title="document.fileName"></i> {{ document.fileName }}
        </button>
        <button class="btn btn-link p-0 pl-2 pt-1" v-show="editable" @click.prevent="deleteDocument(document)" :title="'common.remove' | translate" v-b-tooltip>
          <i class="fa fa-times text-danger"></i>
        </button>
      </template>
      <input type="hidden" v-model="document">
      <div v-show="!document" class="alert" :class=" { 'alert-danger': errors.length, 'alert-warning': !errors.length }" role="alert">
        {{ image ? 'document.empty.image' : 'document.empty' | translate }}
      </div>
    </validation-provider>
  </div>
</template>
<script lang="ts">

import Component, { mixins } from 'vue-class-component'
import Document from '@/modules/document/domain/document'
import documentMixin from '@/modules/common/components/form/documentMixin'
import { Prop, Watch } from 'vue-property-decorator'
import { ValidationProvider } from 'vee-validate'
import documentService from '@/modules/document/services/documentService'

@Component({
  components: { ValidationProvider }
})
export default class DocumentField extends mixins(documentMixin) {
  localBlob : Blob | null = null
  imageUrl: string | null = null

  @Prop(Document) document?: Document
  @Prop({ type: Boolean, default: true }) editable!: boolean
  @Prop({ type: String, required: true }) fieldId?: string
  @Prop({ type: Boolean, default: false }) required!: boolean
  @Prop({ type: Boolean, default: false }) image!: boolean
  @Prop({ type: Boolean, default: false }) preview!: boolean
  @Prop({ type: String, default: '100px' }) previewWidth!: string
  @Prop({ type: String, default: 'auto' }) previewHeight!: string
  @Prop({ type: Boolean, default: false }) disableDownload!: boolean

  get validation () {
    return this.required ? 'required' : ''
  }

  @Watch('document', { immediate: true })
  onDocumentChange () {
    this.preparePreview()
  }

  private async preparePreview () {
    if (this.preview && this.document && this.document.id) {
      this.localBlob = await documentService.downloadBlob('api/document/' + this.document.id)
      const urlCreator = window.URL
      this.imageUrl = urlCreator.createObjectURL(this.localBlob)
    }
  }
}
</script>
