<template>
  <span class="document-upload">
    <div v-show="$refs.upload && $refs.upload.dropActive" class="drop-active">
      <h3>{{ 'document.upload.drop-zone.label' | translate }}</h3>
    </div>
    <button v-if="!uploadVisible" class="btn btn-success" type="button" @click.prevent="showUpload()"><i class="fa fa-plus" aria-hidden="true"></i> {{ 'common.add' | translate }}</button>
    <span v-if="uploadVisible">
            <file-upload
              ref="upload"
              v-model="files"
              dropzone="dropZone"
              :headers="headers"
              :drop="true"
              :drop-directory="true"
              @input-file="inputFile"
              :post-action="actionUrl"
              :extensions="allowedTypes"
              :size="maxSize">
              <button @click.prevent="$refs.fileLabel.click()" class="btn btn-primary mb-2">{{ 'document.upload.choose.file' | translate }}</button>
              <label for="file" ref="fileLabel"></label>
            </file-upload>
            <button class="btn btn-success mb-2" v-show="!$refs.upload || !$refs.upload.active" @click.prevent="upload()" type="button">{{ 'document.upload.load.file' | translate }}</button>
            <button class="btn btn-danger mb-2" v-show="$refs.upload && $refs.upload.active" @click.prevent="stop()" type="button">{{ 'common.stop' | translate }}</button>
            <button class="btn btn-default mb-2" @click.prevent="cancel()">{{ 'common.cancel' | translate }}</button>
            <span v-if="files && files.length">{{ files[0].name }} ({{ files[0].size | fileSize }})</span>
            <b-spinner variant="primary" label="Spinning" v-if="$refs.upload && $refs.upload.active"></b-spinner>
    </span>
  </span>

</template>
<script lang="ts">
import { API_ROOT, FILE_MAX_SIZE, FILE_SUPPORTED_EXTENSIONS } from '@/config'
import eventBus from '@/modules/common/services/eventBus'
import fileUpload from 'vue-upload-component'
import notificationService from '@/modules/common/services/notificationService'
import Document from '@/modules/document/domain/document'
import Component from 'vue-class-component'
import Vue from 'vue'
import { State } from 'vuex-class'
import { Prop } from 'vue-property-decorator'

@Component({
  components: { fileUpload }
})
export default class DocumentUpload extends Vue {
  uploadVisible = false
  files = []
  actionUrl = API_ROOT + 'document'

  @State('accessToken', { namespace: 'auth' }) accessToken!: string
  @Prop({ type: Boolean, default: true }) showFiles!: boolean
  @Prop({ type: String, default: FILE_SUPPORTED_EXTENSIONS }) allowedTypes?: string
  @Prop({ type: Number, default: FILE_MAX_SIZE }) maxSize?: number

  get headers () {
    return { Authorization: 'Bearer ' + this.accessToken }
  }

  showUpload () {
    // send event about activating
    eventBus.$emit('file-upload-show')
    this.uploadVisible = true
  }

  upload () {
    (this.$refs.upload as any).active = true
  }

  stop () {
    (this.$refs.upload as any).active = false
  }

  cancel () {
    this.files.length = 0
    this.hideUpload()
  }

  hideUpload () {
    this.uploadVisible = false
  }

  inputFile (newFile: any, oldFile: any) {
    if (newFile && oldFile && !newFile.active && oldFile.active) {
      // Get response data
      if (newFile && newFile.error) {
        let arg = null
        switch (newFile.error) {
          case 'extension':
            arg = this.allowedTypes || FILE_SUPPORTED_EXTENSIONS
            break
          case 'size':
            arg = this.$options.filters!.fileSize(this.maxSize)
            break
        }
        notificationService.error('error.upload.' + newFile.error, [arg])

        this.files.length = 0
      } else if (oldFile.xhr) {
        //  Get the response status code
        this.$emit('document-uploaded', new Document(newFile.response))
        this.cancel()
      }
    }
  }

  created () {
    // hide if some file upload is activated
    eventBus.$on('file-upload-show', () => {
      this.hideUpload()
    })
  }
}
</script>
