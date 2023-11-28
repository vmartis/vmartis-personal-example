<template>
  <li role="button" class="nav-item has-treeview" v-if="hasPermission('REGULAR_SALE')" :class="{ 'menu-open': isOpen }">
    <a class="nav-link">
      <i class="nav-icon fas fa-retweet"></i>
      <p> {{ 'regular-sale.label' | translate }} <i class="right fas fa-angle-left"></i></p>
    </a>
    <ul class="nav nav-treeview">
      <li class="nav-item">
        <router-link class="nav-link" active-class="active" to="/orders-overview" v-if="hasPermission('REGULAR_SALE')">
          <p>{{ 'common.overview' | translate }}</p></router-link>
        <router-link class="nav-link" active-class="active" to="/orders" v-if="hasPermission('REGULAR_SALE')">
          <p>{{ 'regular-sale.orders.label' | translate }}</p></router-link>
        <router-link class="nav-link" active-class="active" to="/deliveries" v-if="hasPermission('REGULAR_SALE')">
          <p>{{ 'orders.delivered.label' | translate }}</p></router-link>
        <router-link class="nav-link" active-class="active" to="/order-statements" v-if="hasPermission('REGULAR_SALE')">
          <p>{{ 'order-statements.label.short' | translate }}</p></router-link>
      </li>
    </ul>
  </li>
</template>

<script lang="ts">
import Component, { mixins } from 'vue-class-component'
import { Getter } from 'vuex-class'
import submenuMixin from '@/modules/app/components/sidebar/submenuMixin'

@Component
export default class RegularSaleSubmenu extends mixins(submenuMixin) {
  pageGroup = 'regularSale'

  @Getter('hasPermission', { namespace: 'auth' }) hasPermission!: (permission: string) => boolean
}
</script>
