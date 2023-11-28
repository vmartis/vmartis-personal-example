<template>
  <li role="button" class="nav-item has-treeview"
      v-if="hasAnyPermission('FINANCE_OVERVIEW', 'FINANCE_MONEY_BOX', 'FINANCE_MOVEMENT_IMPORT')"
      :class="{ 'menu-open': isOpen }">
    <a class="nav-link">
      <i class="nav-icon fas fa-dollar-sign"></i>
      <p> {{ 'finance.label' | translate }} <i class="right fas fa-angle-left"></i></p>
    </a>
    <ul class="nav nav-treeview">
      <li class="nav-item">
        <router-link class="nav-link" active-class="active" to="/finance/overview/EUR"
                     v-if="hasPermission('FINANCE_OVERVIEW')"><p>{{ 'common.overview' | translate }}</p></router-link>
        <router-link class="nav-link" active-class="active" to="/money-box" v-if="hasPermission('FINANCE_MONEY_BOX')">
          <p>{{ 'money-boxes.label' | translate }}</p></router-link>
        <router-link class="nav-link" active-class="active" to="/bank-transaction-import"
                     v-if="hasPermission('FINANCE_MOVEMENT_IMPORT')">
          <p>{{ 'money-movement.import.label.short' | translate }}</p></router-link>
      </li>
    </ul>
  </li>
</template>

<script lang="ts">
import Component, { mixins } from 'vue-class-component'
import { Getter } from 'vuex-class'
import submenuMixin from '@/modules/app/components/sidebar/submenuMixin'

@Component
export default class FinanceSubmenu extends mixins(submenuMixin) {
  pageGroup = 'finance'

  @Getter('hasPermission', { namespace: 'auth' }) hasPermission!: (permission: string) => boolean
  @Getter('hasAnyPermission', { namespace: 'auth' }) hasAnyPermission!: (...permission: string[]) => boolean
}
</script>
