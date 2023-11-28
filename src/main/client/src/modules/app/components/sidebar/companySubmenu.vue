<template>
  <li role="button" class="nav-item has-treeview" v-if="hasPermission('COMPANY_PARTNER')" :class="{ 'menu-open': isOpen }">
    <a class="nav-link">
      <i class="nav-icon fas fa-address-book"></i>
      <p> {{ 'contacts.label' | translate }} <i class="right fas fa-angle-left"></i></p>
    </a>
    <ul class="nav nav-treeview" >
      <li class="nav-item">
        <router-link class="nav-link" active-class="active" to="/partners"><p>{{ 'partners.label' | translate }}</p></router-link>
        <router-link class="nav-link" active-class="active" to="/contacts"><p>{{ 'contacts.label' | translate }}</p></router-link>
        <router-link class="nav-link" active-class="active" to="/subject-records"><p>{{ 'records.overview.label' | translate }}</p></router-link>
      </li>
    </ul>
  </li>
</template>

<script lang="ts">
import Component, { mixins } from 'vue-class-component'
import { Getter } from 'vuex-class'
import submenuMixin from '@/modules/app/components/sidebar/submenuMixin'

@Component
export default class CompanySubmenu extends mixins(submenuMixin) {
  pageGroup = 'company'

  @Getter('hasPermission', { namespace: 'auth' }) hasPermission!: (permission: string) => boolean
}
</script>
