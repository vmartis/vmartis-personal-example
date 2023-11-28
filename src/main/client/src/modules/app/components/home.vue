<template>
  <page title="">
  </page>
</template>

<script lang="ts">

import Vue from 'vue'
import Component from 'vue-class-component'
import page from '@/modules/common/components/page.vue'

@Component({
  components: { page }
})
export default class Home extends Vue {
}
</script>
