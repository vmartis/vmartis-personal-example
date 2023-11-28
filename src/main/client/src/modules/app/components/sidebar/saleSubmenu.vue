<template>
  <li role="button" class="nav-item has-treeview" v-if="hasAnyPermission('INVOICE', 'TAKINGS', 'TAKINGS_TRANSFER')" :class="{ 'menu-open': isOpen }">
    <a class="nav-link">
      <i class="nav-icon fas fa-coins"></i>
      <p> {{ 'common.sale' | translate }} <i class="right fas fa-angle-left"></i></p>
    </a>
    <ul class="nav nav-treeview">
      <li class="nav-item">
        <router-link class="nav-link" active-class="active" to="/invoices/OUTCOME" v-if="hasPermission('INVOICE')">
          <p>{{ 'invoices.outcome.label' | translate }}</p></router-link>
        <router-link class="nav-link" active-class="active" to="/takings-overview"
                     v-if="hasAnyPermission('TAKINGS_OVERVIEW')"><p>{{ 'takings.overview.label' | translate }}</p>
        </router-link>
        <router-link class="nav-link" active-class="active" to="/takings"
                     v-if="hasAnyPermission('TAKINGS', 'TAKINGS_TRANSFER')">
          <p>{{ 'takings.daily.label.short' | translate }}</p></router-link>
      </li>
    </ul>
  </li>
</template>

<script lang="ts">
import Component, { mixins } from 'vue-class-component'
import { Getter } from 'vuex-class'
import submenuMixin from '@/modules/app/components/sidebar/submenuMixin'

@Component
export default class SaleSubmenu extends mixins(submenuMixin) {
  pageGroup = 'sale'

  @Getter('hasPermission', { namespace: 'auth' }) hasPermission!: (permission: string) => boolean
  @Getter('hasAnyPermission', { namespace: 'auth' }) hasAnyPermission!: (...permission: string[]) => boolean
}
</script>
