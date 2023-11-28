<template>
  <li role="button" class="nav-item has-treeview" v-if="hasAnyPermission('WORK_RECORD_PERSONAL', 'WORK_RECORD_MANAGEMENT')" :class="{ 'menu-open': isOpen }">
    <a class="nav-link">
      <i class="nav-icon fas fa-user-clock"></i>
      <p> {{ 'work-record.label' | translate }} <i class="right fas fa-angle-left"></i></p>
    </a>
    <ul class="nav nav-treeview">
      <li class="nav-item">
        <router-link class="nav-link" active-class="active" to="/work-record/personal" v-if="hasPermission('WORK_RECORD_PERSONAL')"><p>{{ 'work-record.personal.label.short' | translate }}</p></router-link>
        <router-link class="nav-link" active-class="active" to="/work-record/summary/activity" v-if="hasPermission('WORK_RECORD_MANAGEMENT')"><p>{{ 'work-record.summary.period.label.short' | translate }}</p></router-link>
        <router-link class="nav-link" active-class="active" to="/work-record/summary/worker" v-if="hasPermission('WORK_RECORD_MANAGEMENT')"><p>{{ 'work-record.summary.worker.label' | translate }}</p></router-link>
        <router-link class="nav-link" active-class="active" to="/work-record/summary/all" v-if="hasPermission('WORK_RECORD_MANAGEMENT')"><p>{{ 'work-record.overview.records.label' | translate }}</p></router-link>
      </li>
    </ul>
  </li>
</template>

<script lang="ts">
import Component, { mixins } from 'vue-class-component'
import { Getter } from 'vuex-class'
import submenuMixin from '@/modules/app/components/sidebar/submenuMixin'

@Component
export default class WorkRecordSubmenu extends mixins(submenuMixin) {
  pageGroup = 'workRecord'

  @Getter('hasPermission', { namespace: 'auth' }) hasPermission!: (permission: string) => boolean
  @Getter('hasAnyPermission', { namespace: 'auth' }) hasAnyPermission!: (...permission: string[]) => boolean
}
</script>
