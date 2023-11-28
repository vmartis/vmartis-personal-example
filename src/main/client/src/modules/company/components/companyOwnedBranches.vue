<template>
  <box>
    <template #header>
      <h5 class="box-title">{{ 'company-branches.label' | translate }}</h5>
    </template>
    <template #body>
      <company-branch-create-modal
        :show-modal="showCreateModal"
        :model="selectedItem"
        :show-inactivate="false"
        :show-for-order="false"
        @close="createHide()"></company-branch-create-modal>
      <div class="row">
        <div class="col-2">
          <div class="form-group">
            <button class="btn btn-success" type="button" @click="createShow()" :disabled="addDisabled"><i class="fa fa-plus"></i></button>
          </div>
        </div>
        <div class="col-10">
          <form class="form-inline float-right">
            <div v-if="showQueryFilter">
              <label>
                <input v-focus type="text" :placeholder="'common.search' | translate" class="form-control mb-2 mr-sm-2"
                       v-model="query">
              </label>
            </div>
            <div class="checkbox">
              <label class="ml-2">
                <input type="checkbox" v-model="showInactive" class="mr-1"> {{ 'common.inactive.show' | translate }}
              </label>
            </div>
          </form>
        </div>
      </div>

      <div class="row">
        <div class="col-sm-12">
          <div class="table-responsive">
            <table class="table no-padding table-hover table-striped table-sm" v-if="items.length">
              <thead>
              <tr class="bg-gray">
                <th>#</th>
                <th>{{ 'common.name' | translate }}</th>
                <th>{{ 'address.label' | translate }}</th>
                <th></th>
              </tr>
              </thead>
              <tbody>
              <template v-for="(item, index) in items">
                <tr :class="{inactive : !item.active}" v-bind:key="item.id">
                  <td>{{ index + 1 }}</td>
                  <td>{{ item.name }}</td>
                  <td>{{ item.address.format() }}</td>
                  <td class="text-right text-nowrap">
                    <button class="btn btn-link btn-tool" @click.prevent="editItem(item)" :title="'common.edit' | translate" v-b-tooltip.bottom>
                      <i class="fas fa-pencil-alt text-muted"></i></button>
                    <button class="btn btn-link btn-tool" @click.prevent="positionUp(item)" v-if="item.active" :title="'common.move.up' | translate" v-b-tooltip.bottom>
                      <i class="fas fa-arrow-up text-muted"></i>
                    </button>
                    <button class="btn btn-link btn-tool" @click.prevent="positionDown(item)" v-if="item.active" :title="'common.move.down' | translate" v-b-tooltip.bottom>
                      <i class="fas fa-arrow-down text-muted"></i>
                    </button>
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
          <loading id="company-branch"></loading>
          <no-records :data="items" loading-id="company-branch"></no-records>
        </div>
      </div>
    </template>
  </box>
</template>
<script lang="ts">
import box from '@/modules/common/components/box.vue'
import Component, { mixins } from 'vue-class-component'
import noRecords from '@/modules/common/components/noRecords.vue'
import loading from '@/modules/common/components/loading.vue'
import entityTableMixin from '@/modules/common/mixins/entityTableMixin'
import EntityFetchParams from '@/modules/common/store/entityFetchParams'
import companyBranchCreateModal from '@/modules/company/components/companyBranchCreateModal.vue'
import Company from '@/modules/company/domain/company'
import CompanyBranch from '@/modules/company/domain/companyBranch'
import Address from '@/modules/common/domain/address'
import { Watch } from 'vue-property-decorator'
import { State } from 'vuex-class'

@Component({
  components: { companyBranchCreateModal, loading, noRecords, box }
})
export default class CompanyOwnedBranches extends mixins(entityTableMixin) {
  entityModuleName = 'companyBranch'
  doFetch = false
  addDisabled = true

  @State('owned', { namespace: 'company' }) company? : Company

  async fetch () {
    // do nothing, fetch is doing in watcher
  }

  @Watch('company', { immediate: true })
  onCompanyChanged (company: Company) {
    if (company) {
      this.$store.dispatch('companyBranch/getAll', new EntityFetchParams(true, { 'company-id': company.id }))
      this.addDisabled = false
    }
  }

  created () {
    this.newItemTemplateDefault = () => new CompanyBranch({ company: this.company, address: new Address(), active: true })
  }
}
</script>
