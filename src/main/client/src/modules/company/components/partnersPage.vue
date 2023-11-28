<template>
  <page title="partners.label" :bread-crumb-data="breadCrumbData">
    <partners-table></partners-table>
  </page>
</template>

<script lang="ts">

import BreadCrumbData from '@/modules/common/components/breadcrumb/breadcrumbData'
import Component from 'vue-class-component'
import page from '@/modules/common/components/page.vue'
import partnersTable from './partnersTable.vue'
import Vue from 'vue'

@Component({
  components: { page, partnersTable }
})
export default class PartnersPage extends Vue {
  breadCrumbData = new BreadCrumbData([{ textKey: 'contacts.label' }, { textKey: 'partners.label' }])
}
</script>
