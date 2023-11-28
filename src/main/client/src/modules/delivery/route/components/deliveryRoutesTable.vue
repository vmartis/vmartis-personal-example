<template>
  <div>
    <b-card>
      <div class="row">
        <div class="col-10 col-md-4">
          <select-field
            v-model="filter.regionId"
            field-id="region"
            label="region.label"
            :codelist="true"
            :clearable="false"
            :translate="false"
            :options="regions"></select-field>
        </div>
        <div class="col-2 pl-0">
          <b-button-group class="d-print-none btn-input-line">
            <b-button variant="warning" @click.stop="showDirection()" :title="'delivery-route.map.show' | translate" :disabled="!items || !items.length" v-b-tooltip>
              <i class="fas fa-route"></i>
            </b-button>
          </b-button-group>
        </div>
      </div>
    </b-card>

    <box>
      <template #body>
        <div class="row">
          <div class="col-sm-12">
            <div class="table-responsive">
              <table class="table no-padding table-hover table-striped table-sm" v-if="items.length">
                <thead>
                <tr class="bg-gray">
                  <th>#</th>
                  <th>{{ 'address.label' | translate }}</th>
                  <th>{{ 'partner.label' | translate }}</th>
                  <th></th>
                </tr>
                </thead>
                <tbody>
                <template v-for="(item, index) in items">
                  <tr v-bind:key="item.id">
                    <td>{{ index + 1 }}</td>
                    <td>
                      <span v-if="item.companyBranch"><strong>{{ item.companyBranch.name }}, </strong>{{ item.companyBranch.address.format(true) }}</span>
                      <span v-else-if="item.company"><strong>{{ 'company.address.label' | translate([item.company.name]) }}, </strong>{{ item.company.address.format(true) }}</span>
                    </td>
                    <td>
                      <span v-if="item.companyBranch">{{ item.companyBranch.company.name }}</span>
                      <span v-else-if="item.company">{{ item.company.name }}</span>
                    </td>
                    <td class="text-right text-nowrap">
                      <button class="btn btn-link btn-tool" @click.prevent="positionUp(item)"><i class="fas fa-arrow-up text-muted"></i></button>
                      <button class="btn btn-link btn-tool" @click.prevent="positionDown(item)"><i class="fas fa-arrow-down text-muted"></i></button>
                      <button class="btn btn-link btn-tool" @click.stop="showAddress(item)"><i class="fas fa-map-marker-alt text-danger"></i></button>
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
            <loading id="delivery-route"></loading>
            <no-records :data="items" loading-id="delivery-route"></no-records>
          </div>
        </div>
      </template>
    </box>
  </div>
</template>

<script lang="ts">
import _ from 'lodash'
import bookmarkableComponent from '@/modules/common/mixins/bookmarkableComponent'
import box from '@/modules/common/components/box.vue'
import Component, { mixins } from 'vue-class-component'
import DeliveryRoute from '@/modules/delivery/route/domain/deliveryRoute'
import entityTableMixin from '@/modules/common/mixins/entityTableMixin'
import { GOOGLE_MAP_DIRECTION_URL, GOOGLE_MAP_SEARCH_URL } from '@/config'
import loading from '@/modules/common/components/loading.vue'
import noRecords from '@/modules/common/components/noRecords.vue'
import selectField from '@/modules/common/components/form/selectField.vue'
import FilterData from '@/modules/common/mixins/filterData'
import { Getter } from 'vuex-class'
import CodeList from '@/modules/common/domain/codeList'
import EntityFetchParams from '@/modules/common/store/entityFetchParams'
import { Watch } from 'vue-property-decorator'

@Component({
  components: { box, loading, noRecords, selectField }
})
export default class DeliveryRoutesTable extends mixins(entityTableMixin, bookmarkableComponent) {
  entityModuleName = 'deliveryRoute'

  @Getter('validItems', { namespace: 'region' }) regions?: Array<CodeList>

  filter: FilterData = {
    regionId: null
  }

  @Watch('filter.regionId')
  onFilterRegionChange () {
    this.fetch()
  }

  showAddress (deliveryRoute: DeliveryRoute) {
    window.open(GOOGLE_MAP_SEARCH_URL + this.encodeDeliveryRouteAddress(deliveryRoute), '_blank')
  }

  showDirection () {
    const addresses = _(this.items)
      .map(this.encodeDeliveryRouteAddress)
      .join('/')
    window.open(GOOGLE_MAP_DIRECTION_URL + addresses, '_blank')
  }

  private encodeDeliveryRouteAddress (deliveryRoute: DeliveryRoute) {
    const parts = deliveryRoute.address!.format(true, false).replace(/[,]/g, '').split(/\s+/)
    return _(parts).map(encodeURIComponent).join('+')
  }

  async fetch () {
    if (this.filter.regionId) {
      await this.$store.dispatch(this.entityModuleName + '/' + this.itemsFetchAction, new EntityFetchParams(true, { 'region-id': this.filter.regionId }))
    }
  }

  initData () {
    this.$store.dispatch('deliveryRoute/clearAll')
    this.$store.dispatch('region/getAll')
  }
}
</script>
