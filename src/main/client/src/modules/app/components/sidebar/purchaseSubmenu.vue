<template>
  <li role="button" class="nav-item has-treeview" v-if="hasPermission('INVOICE')" :class="{ 'menu-open': isOpen }">
    <a class="nav-link">
      <i class="nav-icon fas fa-shopping-cart"></i>
      <p> {{ 'common.purchase' | translate }} <i class="right fas fa-angle-left"></i></p>
    </a>
    <ul class="nav nav-treeview" v-if="hasPermission('INVOICE')">
      <li class="nav-item">
        <router-link class="nav-link" active-class="active" to="/invoices/INCOME">
          <p>{{ 'invoices.income.label' | translate }}</p></router-link>
      </li>
    </ul>
  </li>
</template>

<script lang="ts">
import Component, { mixins } from 'vue-class-component'
import { Getter } from 'vuex-class'
import submenuMixin from '@/modules/app/components/sidebar/submenuMixin'

@Component
export default class PurchaseSubmenu extends mixins(submenuMixin) {
  pageGroup = 'purchase'

  @Getter('hasPermission', { namespace: 'auth' }) hasPermission!: (permission: string) => boolean
}
</script>
