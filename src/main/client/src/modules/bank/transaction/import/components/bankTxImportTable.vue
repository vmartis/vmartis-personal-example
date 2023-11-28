<template>
  <box>
    <template #header>
      <document-upload @document-uploaded="documentUploaded($event)" allowed-types="xml"></document-upload>
    </template>
    <template #body>
      <div class="row">
        <div class="col-sm-12 table-responsive">
          <table class="table no-padding table-hover table-striped table-sm" v-if="items.length">
          <thead>
          <tr class="bg-gray">
            <th>{{ 'common.date.from' | translate }}</th>
            <th>{{ 'common.date.to' | translate }}</th>
            <th class="text-center">{{ 'money-movement.import.number' | translate }}</th>
            <th>{{ 'common.file' | translate }}</th>
          </tr>
          </thead>
          <tbody>
          <template v-for="item in items">
            <tr v-bind:key="item.id">
              <td>{{ item.dateFrom | date }}</td>
              <td>{{ item.dateTo | date }}</td>
              <td class="text-center">
                <span :class="{'text-success text-bold' : item.successCount}">{{ item.successCount }} </span>
                  /
                <span :class="{'text-danger text-bold' : item.failedCount}"> {{ item.failedCount }}</span>
              </td>
              <td><document-field :document="item.document" :editable="false" field-id="document"></document-field></td>
            </tr>
          </template>
          </tbody>
          </table>
        </div>
      </div>
      <div class="row">
        <div class="col-sm-12">
          <loading id="bank-tx-import"></loading>
          <no-records :data="items" loading-id="bank-tx-import"></no-records>
        </div>
      </div>
    </template>
  </box>
</template>
<script lang="ts">
import Component, { mixins } from 'vue-class-component'
import BankTxImport from '@/modules/bank/transaction/import/domain/bankTxImport'
import box from '@/modules/common/components/box.vue'
import documentUpload from '@/modules/document/components/documentUpload.vue'
import Document from '@/modules/document/domain/document'
import documentField from '@/modules/common/components/form/documentField.vue'
import loading from '@/modules/common/components/loading.vue'
import noRecords from '@/modules/common/components/noRecords.vue'
import notificationService from '@/modules/common/services/notificationService'
import EntityCreateParams from '@/modules/common/store/entityCreateParams'
import entityTableMixin from '@/modules/common/mixins/entityTableMixin'

@Component({
  components: { box, documentUpload, documentField, loading, noRecords }
})
export default class BankTxImportPage extends mixins(entityTableMixin) {
  entityModuleName = 'bankTxImport'
  activeSupport = false

  documentUploaded (document: Document) {
    const bankTxImport = new BankTxImport({ document: document })
    this.$store.dispatch('bankTxImport/create', new EntityCreateParams(bankTxImport, true)).then((result) => {
      if (result) {
        notificationService.success('bank-tx-import.upload.success')
        this.$emit('close')
      }
    })
  }
}
</script>
