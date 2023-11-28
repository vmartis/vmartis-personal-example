<template>
  <div>
    <bank-account-create-modal
      :show-modal="showCreateModal"
      :model="selectedItem"
      :show-inactivate="true"
      @close="createHide()"></bank-account-create-modal>
    <div class="row">
      <div class="col-12">
        <h5 class="box-title">{{ 'bank-accounts.label' | translate }}</h5>
      </div>
    </div>
    <div class="row">
      <div class="col-2">
        <div class="form-group">
          <button class="btn btn-success" type="button" @click="createShow()"><i class="fa fa-plus"></i></button>
        </div>
      </div>
      <div class="col-10">
        <form class="form-inline float-right">
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
              <th>{{ 'common.name' | translate }}</th>
              <th>{{ 'bank-account.number.label' | translate }}</th>
              <th>{{ 'currency.label' | translate }}</th>
              <th></th>
            </tr>
            </thead>
            <tbody>
            <template v-for="item in items">
              <tr :class="{inactive : !item.active}" v-bind:key="item.id">
                <td>{{ item.name }}</td>
                <td>{{ item.formatId }}</td>
                <td>{{ item.currency | codeListValue(currencies) }}</td>
                <td class="text-right text-nowrap">
                  <button class="btn btn-link btn-tool" @click.prevent="editItem(item)"><i class="fas fa-pencil-alt text-muted" :title="'common.edit' | translate" v-b-tooltip.bottom></i></button>
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
        <loading id="bank-account"></loading>
        <no-records :data="items" loading-id="bank-account"></no-records>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import bankAccountCreateModal from '@/modules/bank/account/components/bankAccountCreateModal.vue'
import Component, { mixins } from 'vue-class-component'
import noRecords from '@/modules/common/components/noRecords.vue'
import loading from '@/modules/common/components/loading.vue'
import entityTableMixin from '@/modules/common/mixins/entityTableMixin'
import EntityFetchParams from '@/modules/common/store/entityFetchParams'
import { Prop } from 'vue-property-decorator'
import BankAccount from '@/modules/bank/account/domain/bankAccount'
import Subject from '@/modules/subject/domain/subject'
import { State } from 'vuex-class'
import Currency from '@/modules/currency/domain/currency'

@Component({
  components: { bankAccountCreateModal, loading, noRecords }
})
export default class BankAccountsTable extends mixins(entityTableMixin) {
  entityModuleName = 'bankAccount'

  @Prop({ type: Object, required: true }) subject!: Subject
  @State('items', { namespace: 'currency' }) currencies? : Currency[]

  async created () {
    const parameters: any = { subjectId: this.subject.id }
    this.fetchActionParams = new EntityFetchParams(true, parameters)
    this.newItemTemplateDefault = () => new BankAccount({ subject: this.subject })
  }

  async beforeFetch () {
    await this.$store.dispatch('currency/getAll')
    await this.$store.dispatch('bankAccount/clearAll')
  }
}
</script>
