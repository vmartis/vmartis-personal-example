<template>
  <aside class="main-sidebar sidebar-dark-primary elevation-4">
    <router-link class="brand-link" to="/">
      <div class="logo">
        <div class="logo-lg">
          <img src="./../../../../img/logo-white.svg" class="sidebar-logo"/>
        </div>
        <div class="logo-mini">
          <img src="./../../../../img/q-logo-white.svg" class="sidebar-logo"/>
        </div>
      </div>
    </router-link>
    <div class="sidebar">
      <div class="user-panel mt-3 pb-3 mb-3 text-center" v-if="company">
        {{ company.name }}
      </div>
      <nav class="mt-2">
        <ul class="nav nav-pills nav-sidebar flex-column" data-widget="treeview" role="menu" data-accordion="true" ref="sidebar">
          <company-submenu></company-submenu>
          <work-record-submenu></work-record-submenu>
          <purchase-submenu></purchase-submenu>
          <sale-submenu></sale-submenu>
          <regular-sale-submenu></regular-sale-submenu>
          <finance-submenu></finance-submenu>
          <stock-submenu></stock-submenu>
          <settings-submenu></settings-submenu>
        </ul>
      </nav>
    </div>
  </aside>
</template>
<script lang="ts">

import Company from '@/modules/company/domain/company'
import companySubmenu from '@/modules/app/components/sidebar/companySubmenu.vue'
import Component from 'vue-class-component'
import financeSubmenu from '@/modules/app/components/sidebar/financeSubmenu.vue'
import { State } from 'vuex-class'
import purchaseSubmenu from '@/modules/app/components/sidebar/purchaseSubmenu.vue'
import regularSaleSubmenu from '@/modules/app/components/sidebar/regularSaleSubmenu.vue'
import saleSubmenu from '@/modules/app/components/sidebar/saleSubmenu.vue'
import settingsSubmenu from './settingsSubmenu.vue'
import stockSubmenu from './stockSubmenu.vue'
import workRecordSubmenu from '@/modules/app/components/sidebar/workRecordSubmenu.vue'
import Vue from 'vue'

@Component({
  components: { companySubmenu, financeSubmenu, purchaseSubmenu, regularSaleSubmenu, saleSubmenu, settingsSubmenu, stockSubmenu, workRecordSubmenu }
})
export default class Sidebar extends Vue {
  @State('owned', { namespace: 'company' }) company? : Company

  mounted () {
    this.$store.dispatch('company/getOwned')
    // need to init main side bar manually - do not work automatically after login
    const element: any = $(this.$refs.sidebar)
    // eslint-disable-next-line no-unused-vars
    const treeview = element.Treeview('init')
  }
}
</script>
