<template>
  <div>
    <slot name="modal" v-bind:showCreateModal="showCreateModal" v-bind:selectedItem="selectedItem" v-bind:createHide="createHide">
      <code-list-modal
        :show-modal="showCreateModal"
        :model="selectedItem"
        :module-name="moduleName"
        :colorable="colorable"
        :labelMinLength="labelMinLength"
        @close="createHide()"></code-list-modal>
    </slot>
    <div class="row">
      <div class="col-2">
        <div class="form-group">
          <button class="btn btn-success" type="button" @click="createShow()"><i
            class="fa fa-plus"></i></button>
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
        <slot name="table" v-bind:items="items" v-bind:editItem="editItem" v-bind:positionUp="positionUp" v-bind:positionDown="positionDown">
          <div class="table-responsive">
            <table class="table no-padding table-hover table-striped table-sm" v-if="items.length">
              <thead>
              <tr class="bg-gray">
                <th>#</th>
                <th>{{ 'common.name' | translate }}</th>
                <th v-if="colorable">{{ 'common.color' | translate }}</th>
                <th></th>
              </tr>
              </thead>
              <tbody>
              <template v-for="(item, index) in items">
                <tr :class="{inactive : !item.valid}" v-bind:key="item.id">
                  <td>{{ index + 1 }}</td>
                  <td>{{ item.label }}</td>
                  <td v-if="colorable">
                    <b-badge :style="`background-color: ${item.color}`" size="">{{ item.label }}</b-badge>
                  </td>
                  <td class="text-right text-nowrap">
                    <button class="btn btn-link btn-tool" @click.prevent="editItem(item)" :title="'common.edit' | translate" v-b-tooltip.bottom>
                      <i class="fas fa-pencil-alt text-muted"></i>
                    </button>
                    <button class="btn btn-link btn-tool" @click.prevent="positionUp(item)" v-if="item.valid" :title="'common.move.up' | translate" v-b-tooltip.bottom>
                      <i class="fas fa-arrow-up text-muted"></i>
                    </button>
                    <button class="btn btn-link btn-tool" @click.prevent="positionDown(item)" v-if="item.valid" :title="'common.move.down' | translate" v-b-tooltip.bottom>
                      <i class="fas fa-arrow-down text-muted"></i>
                    </button>
                  </td>
                </tr>
              </template>
              </tbody>
            </table>
          </div>
        </slot>
      </div>
    </div>
    <div class="row">
      <div class="col-sm-12">
        <loading :id="loadingId"></loading>
        <no-records :data="items" :loading-id="loadingId"></no-records>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import codeListModal from '@/modules/common/components/codeListCreateModalGeneric.vue'
import Component, { mixins } from 'vue-class-component'
import EntityFetchParams from '@/modules/common/store/entityFetchParams'
import entityTableMixin from '../mixins/entityTableMixin'
import loading from './loading.vue'
import noRecords from './noRecords.vue'
import { Prop, Watch } from 'vue-property-decorator'

@Component({
  components: { loading, noRecords, codeListModal }
})
export default class CodeListTable extends mixins(entityTableMixin) {
  itemsActiveGetter = 'validItems'
  fetchActionParams = new EntityFetchParams(true)
  entityModuleName?: string

  @Prop({ type: String, required: true }) moduleName!: string
  @Prop({ type: String, required: true }) loadingId?: string
  @Prop({ type: Boolean, default: false }) colorable?: boolean
  @Prop({ type: Number, default: 2 }) labelMinLength!: boolean

  @Watch('moduleName', { immediate: true })
  async onModuleNameChanged (moduleName: string) {
    this.entityModuleName = moduleName
    await this.$store.dispatch(this.entityModuleName + '/clearAll')
  }
}
</script>
