<template>
  <div>
  <order-item-template-create-modal
    :show-modal="showCreateModal"
    :model="selectedItem"
    @close="createHide()"></order-item-template-create-modal>
    <div class="row">
      <div class="col-12">
        <h5 class="box-title">{{ 'order-item-template.label' | translate }}</h5>
      </div>
    </div>
    <div class="row">
      <div class="col-2">
        <div class="form-group">
          <button class="btn btn-success" type="button" @click="createShow()"><i class="fa fa-plus"></i></button>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-sm-12">
        <div class="table-responsive">
          <table class="table no-padding table-hover table-striped table-sm" v-if="items.length">
            <thead>
            <tr class="bg-gray">
              <th>{{ 'common.item' | translate }}</th>
              <th></th>
            </tr>
            </thead>
            <tbody>
            <template v-for="item in items">
              <tr v-bind:key="item.id">
                <td>{{ item.item.name }}</td>
                <td class="text-right text-nowrap">
                  <button class="btn btn-link btn-tool" @click.prevent="editItem(item)"><i class="fas fa-pencil-alt text-muted" :title="'common.edit' | translate" v-b-tooltip.bottom></i></button>
                  <button class="btn btn-link btn-tool" @click.prevent="positionUp(item)"><i class="fas fa-arrow-up text-muted"></i></button>
                  <button class="btn btn-link btn-tool" @click.prevent="positionDown(item)"><i class="fas fa-arrow-down text-muted"></i></button>
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
        <loading id="order-item-template"></loading>
        <no-records :data="items" loading-id="order-item-template"></no-records>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import Company from '@/modules/company/domain/company'
import Component, { mixins } from 'vue-class-component'
import entityTableMixin from '@/modules/common/mixins/entityTableMixin'
import EntityFetchParams from '@/modules/common/store/entityFetchParams'
import loading from '@/modules/common/components/loading.vue'
import noRecords from '@/modules/common/components/noRecords.vue'
import OrderItemTemplate from '@/modules/order/domain/orderItemTemplate'
import orderItemTemplateCreateModal from '@/modules/order/components/orderItemTemplateCreateModal.vue'
import { Prop } from 'vue-property-decorator'

@Component({
  components: { loading, noRecords, orderItemTemplateCreateModal }
})
export default class CompanyOrderItemTemplateTable extends mixins(entityTableMixin) {
  entityModuleName = 'orderItemTemplate'
  activeSupport = false

  @Prop({ required: false }) companyId!: number

  async created () {
    const parameters: any = { 'company-id': this.companyId }
    this.fetchActionParams = new EntityFetchParams(true, parameters)
    this.newItemTemplateDefault = () => new OrderItemTemplate({ company: new Company({ id: this.companyId }) })
  }

  async beforeFetch () {
    await this.$store.dispatch('orderItemTemplate/clearAll')
  }
}
</script>
