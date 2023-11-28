<template>
  <li role="button" class="nav-item has-treeview"
      v-if="hasAnyPermission('SETTING_COMPANY', 'SETTING_FINANCE', 'SETTING_INVOICE', 'SETTING_ITEM', 'SETTING_USER', 'SETTING_WORKER', 'SETTING_STOCK')"
      :class="{ 'menu-open': isOpen }">
    <a class="nav-link">
      <i class="nav-icon fas fa-cog"></i>
      <p> {{ 'settings.label' | translate }} <i class="right fas fa-angle-left"></i></p>
    </a>
    <ul class="nav nav-treeview">
      <li class="nav-item">
        <router-link class="nav-link" active-class="active" to="/settings/company"
                     v-if="hasPermission('SETTING_COMPANY')"><p>{{ 'company.label' | translate }}</p></router-link>
        <router-link class="nav-link" active-class="active" to="/settings/finance"
                     v-if="hasPermission('SETTING_FINANCE')"><p>{{ 'finance.label' | translate }}</p></router-link>
        <router-link class="nav-link" active-class="active" to="/settings/sale" v-if="hasPermission('SETTING_SALE')">
          <p>{{ 'common.sale' | translate }}</p></router-link>
        <router-link class="nav-link" active-class="active" to="/settings/regular-sale"
                     v-if="hasPermission('SETTING_REGULAR_SALE')"><p>{{ 'regular-sale.label' | translate }}</p>
        </router-link>
        <router-link class="nav-link" active-class="active" to="/settings/invoice"
                     v-if="hasPermission('SETTING_INVOICE')"><p>{{ 'invoicing.label' | translate }}</p></router-link>
        <router-link class="nav-link" active-class="active" to="/settings/worker"
                     v-if="hasPermission('SETTING_WORKER')"><p>{{ 'workers.label' | translate }}</p></router-link>
        <router-link class="nav-link" active-class="active" to="/settings/working-activity"
                     v-if="hasPermission('SETTING_WORKER')"><p>{{ 'working-activities.label' | translate }}</p>
        </router-link>
        <router-link class="nav-link" active-class="active" to="/settings/stock" v-if="hasPermission('SETTING_STOCK')">
          <p>{{ 'stocks.label' | translate }}</p></router-link>
        <router-link class="nav-link" active-class="active" to="/settings/item" v-if="hasPermission('SETTING_ITEM')">
          <p>{{ 'items.label' | translate }}</p></router-link>
        <router-link class="nav-link" active-class="active" to="/settings/users" v-if="hasPermission('SETTING_USER')">
          <p>{{ 'users.label' | translate }}</p></router-link>
      </li>
    </ul>
  </li>
</template>

<script lang="ts">
import Component, { mixins } from 'vue-class-component'
import { Getter } from 'vuex-class'
import submenuMixin from '@/modules/app/components/sidebar/submenuMixin'

@Component
export default class SettingsSubmenu extends mixins(submenuMixin) {
  pageGroup = 'setting'

  @Getter('hasPermission', { namespace: 'auth' }) hasPermission!: (permission: string) => boolean
  @Getter('hasAnyPermission', { namespace: 'auth' }) hasAnyPermission!: (...permission: string[]) => boolean
}
</script>
