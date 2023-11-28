<template>
  <page title="delivery-routes.region.label">
    <delivery-routes-table></delivery-routes-table>
  </page>
</template>

<script lang="ts">
import Component from 'vue-class-component'
import deliveryRoutesTable from '@/modules/delivery/route/components/deliveryRoutesTable.vue'
import page from '@/modules/common/components/page.vue'
import Vue from 'vue'

@Component({
  components: { deliveryRoutesTable, page }
})
export default class DeliveryRoutesPage extends Vue {
}
</script>
