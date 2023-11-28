<template>
  <li role="button" class="nav-item has-treeview" v-if="hasPermission('STOCK')" :class="{ 'menu-open': isOpen }">
    <a class="nav-link">
      <i class="nav-icon fas fa-warehouse"></i>
      <p> {{ 'stocks.label' | translate }} <i class="right fas fa-angle-left"></i></p>
    </a>
    <ul class="nav nav-treeview">
      <li class="nav-item">
        <router-link class="nav-link" active-class="active" to="/stock/item-overview">
          <p>{{ 'common.overview' | translate }}</p></router-link>
        <router-link class="nav-link" active-class="active" to="/stock/item">
          <p>{{ 'stock-items.label' | translate }}</p></router-link>
        <router-link class="nav-link" active-class="active" to="/stock/movements">
          <p>{{ 'stock-movements.label.2' | translate }}</p></router-link>
      </li>
    </ul>
  </li>
</template>

<script lang="ts">
import Component, { mixins } from 'vue-class-component'
import { Getter } from 'vuex-class'
import submenuMixin from '@/modules/app/components/sidebar/submenuMixin'

@Component
export default class StockSubmenu extends mixins(submenuMixin) {
  pageGroup = 'stock'

  @Getter('hasPermission', { namespace: 'auth' }) hasPermission!: (permission: string) => boolean
}
</script>
