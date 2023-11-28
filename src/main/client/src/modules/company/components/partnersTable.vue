<template>
  <b-card>
    <company-create-modal
      :show-modal="showCreateModal"
      :model="selectedItem"
      :show-default-currency="false"
      :show-vat-local="false"
      @close="createHide()"></company-create-modal>
    <div class="row">
      <div class="col-2">
        <div class="form-group">
          <button class="btn btn-success" type="button" @click="createShow()"><i class="fa fa-plus"></i></button>
        </div>
      </div>
      <div class="col-10">
        <form class="form-inline float-right">
          <div>
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
      <div class="col-12">
        <div class="table-responsive">
          <table class="table no-padding table-hover table-striped table-sm" v-if="items.length">
            <thead>
            <tr class="bg-gray">
              <th>{{ 'partner.label' | translate }}</th>
              <th>{{ 'company.address.label.short' | translate }}</th>
              <th>{{ 'region.label' | translate }}</th>
              <th>{{ 'common.category' | translate }}</th>
              <th></th>
            </tr>
            </thead>
            <tbody>
            <template v-for="item in items">
              <tr :class="{inactive : !item.active}" v-bind:key="item.id" @click.prevent="detail(item.id)"
                  role="button">
                <td>{{ item.name }}</td>
                <td>{{ item.address.format() }}</td>
                <td>
                  <b-badge v-if="item.address.region"
                           :style="`background-color: ${item.address.region.color}`">{{ item.address.region.label }}
                  </b-badge>
                </td>
                <td>
                  <b-badge v-bind:key="category.id" v-for="category in item.categories" class="mr-1"
                           :style="`background-color: ${category.color}`">{{ category.label }}
                  </b-badge>
                </td>
                <td class="text-right text-nowrap">
                  <button class="btn btn-link btn-tool" @click.stop="detail(item.id)" :title="'common.detail' | translate" v-b-tooltip>
                    <i class="fas fa-search text-muted"></i>
                  </button>
                </td>
              </tr>
            </template>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </b-card>
</template>
<script lang="ts">
import Component, { mixins } from 'vue-class-component'
import box from '@/modules/common/components/box.vue'
import loading from '@/modules/common/components/loading.vue'
import noRecords from '@/modules/common/components/noRecords.vue'
import entityTableMixin from '@/modules/common/mixins/entityTableMixin'
import companyCreateModal from '@/modules/company/components/companyCreateModal.vue'
import { State } from 'vuex-class'
import CodeList from '@/modules/common/domain/codeList'
import Company from '@/modules/company/domain/company'
import { CompanyRole } from '@/modules/company/type/companyRole'
import Address from '@/modules/common/domain/address'

@Component({
  components: { box, loading, noRecords, companyCreateModal }
})
export default class PartnersTable extends mixins(entityTableMixin) {
  entityModuleName = 'company'
  itemsGetter = 'allPartners'
  itemsActiveGetter = 'activePartners'
  itemsFetchAction = 'getAllPartners'
  @State('items', { namespace: 'region' }) regions!: Array<CodeList>

  createShow () {
    this.selectedItem = new Company({ role: CompanyRole.PARTNER, address: new Address(), active: true })
    this.showCreateModal = true
  }

  detail (id: bigint) {
    this.$router.push({ name: 'partnerDetail', params: { partnerId: id + '' } })
  }

  createHide () {
    this.showCreateModal = false
  }

  created () {
    this.$store.dispatch('region/getAll')
  }
}
</script>
