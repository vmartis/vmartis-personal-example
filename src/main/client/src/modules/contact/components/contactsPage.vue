<template>
  <page title="partner.contacts.label" :bread-crumb-data="breadCrumbData">
    <contacts-table></contacts-table>
  </page>
</template>

<script lang="ts">

import BreadCrumbData from '@/modules/common/components/breadcrumb/breadcrumbData'
import Component from 'vue-class-component'
import contactsTable from '@/modules/contact/components/contactsTable.vue'
import page from '@/modules/common/components/page.vue'
import Vue from 'vue'

@Component({
  components: { contactsTable, page }
})
export default class ContactsPage extends Vue {
  breadCrumbData = new BreadCrumbData([{ textKey: 'contacts.label' }, { textKey: 'partner.contacts.label' }])
}
</script>
