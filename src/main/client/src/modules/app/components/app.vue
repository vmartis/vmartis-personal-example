<template>
  <div v-if="configuration && loggedIn" class="wrapper">
    <navbar></navbar>
    <sidebar></sidebar>
    <router-view/>
    <footer class="main-footer text-right d-print-none">
      <strong>{{ 'footer.copyright.label' | translate }}
        <a :href="smartbrainsUrl" target="_blank">{{ 'footer.company' | translate }}</a>.
      </strong>
    </footer>
  </div>
  <div v-else-if="configuration || configurationFetchFailed">
    <router-view/>
  </div>
  <div v-else>
    <loading :id="['configuration', 'logout']" class="mt-4"></loading>
  </div>
</template>
<script lang="ts">
import Component from 'vue-class-component'
import Configuration from '@/modules/configuration/domain/configuration'
import EntityFetchParams from '@/modules/common/store/entityFetchParams'
import { Getter } from 'vuex-class'
import i18nService from '@/i18n'
import Loading from '@/modules/common/components/loading.vue'
import navbar from './navbar.vue'
import sidebar from './sidebar/sidebar.vue'
import { SMARTBRAINS_URL } from '@/config'
import Vue from 'vue'

@Component({
  components: { Loading, navbar, sidebar }
})
export default class App extends Vue {
  configurationFetchFailed = false // need to check case, when configuration fetch failed. Logout page need to be accessible
  smartbrainsUrl = SMARTBRAINS_URL

  @Getter('loggedIn', { namespace: 'auth' }) loggedIn!: boolean
  @Getter('first', { namespace: 'configuration' }) configuration?: Configuration

  async beforeCreate () {
    try {
      await this.$store.dispatch('configuration/getAll', new EntityFetchParams(true))
      i18nService.changeLocale(this.configuration!.application.localization!.toLowerCase())
    } catch (e) {
      this.configurationFetchFailed = true
    }
  }
}
</script>
<style lang="scss">
/* must be here because we need to initiate to use scss sources */
</style>
