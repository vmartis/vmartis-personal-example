<template>
  <b-card>
    <div class="row">
      <div class="col-12">
        <form class="form-inline float-right">
          <div>
            <label>
              <input v-focus type="text" :placeholder="'common.search' | translate"
                     class="form-control mb-2 mr-sm-2"
                     v-model="query">
            </label>
          </div>
        </form>
      </div>
    </div>

    <div class="row">
      <div class="col-12">
        <div class="table-responsive">
          <table class="table no-padding table-hover table-striped table-sm" v-if="items.length">
            <thead>
            <tr class="bg-gray">
              <th>{{ 'common.name' | translate }}</th>
              <th>{{ 'partner.label' | translate }}</th>
              <th>{{ 'contact.phone-number.label' | translate }}</th>
              <th>{{ 'common.email' | translate }}</th>
              <th></th>
            </tr>
            </thead>
            <tbody>
            <template v-for="item in items">
              <tr :class="{inactive : !item.active}" v-bind:key="item.id" role="button" @click.stop="detail(item.subject.id)">
                <td>
                  <div>{{ item.name }}</div>
                  <small class="text-muted">{{ item.description }}</small></td>
                <td>{{ item.subject.label }}</td>
                <td>{{ item.phoneNumber }}</td>
                <td><a :href="'mailto:' + item.email">{{ item.email }}</a></td>
                <td class="text-right text-nowrap">
                  <button class="btn btn-link btn-tool" type="button" @click.stop="createShow(item)"><i class="fas fa-comments text-success" :title="'record.create.label' | translate" v-b-tooltip></i></button>
                  <button class="btn btn-link btn-tool" type="button"><i class="fas fa-search text-muted" :title="'common.detail' | translate" v-b-tooltip></i></button>
                </td>
              </tr>
            </template>
            </tbody>
          </table>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-sm-12">
        <loading id="contact"></loading>
        <no-records :data="items" loading-id="contact"></no-records>
        <subject-record-create-modal
          :show-modal="showCreateModal"
          :model="selectedItem"
          :do-fetch-after="false"
          @close="createHide">
        </subject-record-create-modal>
      </div>
    </div>
  </b-card>
</template>
<script lang="ts">
import box from '@/modules/common/components/box.vue'
import Component, { mixins } from 'vue-class-component'
import noRecords from '@/modules/common/components/noRecords.vue'
import loading from '@/modules/common/components/loading.vue'
import entityTableMixin from '@/modules/common/mixins/entityTableMixin'
import subjectRecordCreateModal from '@/modules/record/components/subjectRecordCreateModal.vue'
import SubjectRecord from '@/modules/record/domain/subjectRecord'

@Component({
  components: { box, loading, noRecords, subjectRecordCreateModal }
})
export default class ContactTable extends mixins(entityTableMixin) {
  entityModuleName = 'contact'

  detail (id: bigint) {
    this.$router.push({ name: 'partnerDetail', params: { partnerId: id + '' } })
  }

  created () {
    this.newItemTemplateDefault = (data: any) => new SubjectRecord({ subject: data.subject, contact: data })
  }

  async beforeFetch () {
    await this.$store.dispatch('contact/clearAll')
  }
}
</script>
