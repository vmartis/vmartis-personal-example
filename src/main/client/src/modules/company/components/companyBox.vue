<template>
  <b-card :collapsible="collapsible" :collapsed="collapsed">
    <template #header>
      <h5>{{ company.name }}</h5>
    </template>
    <div class="row">
      <div class="col-md-12">
        <dl>
          <dt>{{ 'company.address.label.short' | translate }}</dt>
          <dd>{{ company.address.format() }}</dd>
        </dl>
      </div>
    </div>
    <div class="row">
      <div class="col-md-6">
        <dl>
          <dt>{{ 'company.company-id.label' | translate }}</dt>
          <dd>{{ company.companyId }}</dd>
        </dl>
      </div>
      <div class="col-md-6">
        <dl>
          <dt>{{ 'company.vat-id.label' | translate }}</dt>
          <dd v-if="company.vatId">{{ company.vatId }}</dd>
          <dd v-else>{{ 'company.vat-payer.not.label' | translate }}</dd>
        </dl>
      </div>
    </div>
    <slot name="footer"></slot>
  </b-card>
</template>
<script lang="ts">
import box from '@/modules/common/components/box.vue'
import Company from '../domain/company'
import Vue from 'vue'
import Component from 'vue-class-component'
import { Prop } from 'vue-property-decorator'

@Component({
  components: { box }
})
export default class CompanyBox extends Vue {
  @Prop({ type: Company, required: true }) company!: Company
  @Prop({ type: Boolean, default: false }) collapsible!: boolean
  @Prop({ type: Boolean, default: false }) collapsed!: boolean
}
</script>
