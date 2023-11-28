<template>
  <page title="partner.profile.label" :bread-crumb-data="breadCrumbData">
    <div class="row">
      <div class="col-md-4">
        <partner-info-box v-if="partnerId"></partner-info-box>
        <partner-addresses-box></partner-addresses-box>
        <partner-contacts-box v-if="partner"></partner-contacts-box>
      </div>
      <div class="col-md-8">
        <b-card no-body header-text-variant="transparent">
          <b-tabs lazy card>
            <b-tab>
              <template v-slot:title>
                <i class="fas fa-comments" v-b-tooltip.hover :title="'records.label' | translate"></i>
              </template>
              <subject-records-timeline :subject="partner" v-if="partner"></subject-records-timeline>
            </b-tab>
            <b-tab v-if="hasPermission('FINANCE_MONEY_BOX')">
              <template v-slot:title>
                <i class="fas fa-dollar-sign" v-b-tooltip.hover :title="'money-movements.label' | translate"></i>
              </template>
              <partner-money-movement-panel :subject="partner"></partner-money-movement-panel>
            </b-tab>
            <b-tab>
              <template v-slot:title>
                <i class="fas fa-retweet" v-b-tooltip.hover :title="'regular-sale.label' | translate"></i>
              </template>
                <partner-regular-sale-panel :subject="partner"></partner-regular-sale-panel>
            </b-tab>
            <b-tab v-if="hasPermission('INVOICE')">
              <template v-slot:title>
                <i class="fas fa-file-alt" v-b-tooltip.hover  :title="'invoicing.label' | translate"></i>
              </template>
              <partner-invoices-panel :subject="partner"></partner-invoices-panel>
            </b-tab>
            <b-tab>
              <template v-slot:title>
                <i class="fas fa-cog" v-b-tooltip.hover :title="'setting.label' | translate"></i>
              </template>
              <company-sale-settings-box class="mb-2"></company-sale-settings-box>
              <bank-accounts-table :subject="partner" v-if="partner"></bank-accounts-table>
              <company-price-list-table :company-id="partnerIdNumber"></company-price-list-table>
              <company-order-item-template-table :company-id="partnerIdNumber" v-if="partner"></company-order-item-template-table>
            </b-tab>
          </b-tabs>
        </b-card>
      </div>
    </div>
  </page>
</template>
<script lang="ts">
import bankAccountsTable from '@/modules/bank/account/components/bankAccountsTable.vue'
import BreadCrumbData from '@/modules/common/components/breadcrumb/breadcrumbData'
import Company from '@/modules/company/domain/company'
import companyOrderItemTemplateTable from '@/modules/company/components/companyOrderItemTemplateTable.vue'
import companyPriceListTable from '@/modules/company/components/companyPriceListTable.vue'
import companySaleSettingsBox from '@/modules/company/components/companySaleSettingsBox.vue'
import Component from 'vue-class-component'
import EntityFetchParams from '@/modules/common/store/entityFetchParams'
import { Getter, State } from 'vuex-class'
import page from '@/modules/common/components/page.vue'
import partnerInfoBox from './partnerInfoBox.vue'
import partnerAddressesBox from './partnerAdressesBox.vue'
import partnerContactsBox from './partnerContactsBox.vue'
import partnerMoneyMovementPanel from '@/modules/company/components/movement/partnerMoneyMovementPanel.vue'
import partnerRegularSalePanel from '@/modules/company/components/regularsale/partnerRegularSalePanel.vue'
import { Prop } from 'vue-property-decorator'
import subjectRecordsTimeline from '@/modules/record/components/subjectRecordsTimeline.vue'
import Vue from 'vue'
import partnerInvoicesPanel from '@/modules/company/components/invoice/partnerInvoicesPanel.vue'

@Component({
  components: {
    companyOrderItemTemplateTable,
    bankAccountsTable,
    companyPriceListTable,
    companySaleSettingsBox,
    page,
    partnerAddressesBox,
    partnerContactsBox,
    partnerInfoBox,
    partnerInvoicesPanel,
    partnerMoneyMovementPanel,
    partnerRegularSalePanel,
    subjectRecordsTimeline
  }
})
export default class PartnerDetailPage extends Vue {
  @Prop({ type: String, required: true }) partnerId!: string
  @State('selected', { namespace: 'company' }) partner!: Company
  @Getter('hasPermission', { namespace: 'auth' }) hasPermission!: (permission: string) => boolean

  // need to cast to BigInt
  get partnerIdNumber () {
    return parseInt(this.partnerId)
  }

  get breadCrumbData () {
    return this.partner ? new BreadCrumbData([{ textKey: 'contacts.label' }, {
      textKey: 'partners.label',
      routeName: 'partners'
    }, { text: this.partner.name }], 'partners') : null
  }

  async created () {
    await this.$store.dispatch('company/select', this.partnerId)
    await this.$store.dispatch('contact/getAll', new EntityFetchParams(true, { 'subject-id': this.partnerId }))
  }
}
</script>
