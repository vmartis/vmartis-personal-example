<template>
  <div class="wrapper">
    <div class="row justify-content-around">
      <div class="col-auto">
        <b-spinner variant="primary" class="mt-5"></b-spinner>
      </div>
    </div>
  </div>
</template>

<script lang="ts">

import Component from 'vue-class-component'
import Vue from 'vue'
import { Getter } from 'vuex-class'

@Component
export default class LogoutPage extends Vue {
  @Getter('loggedIn', { namespace: 'auth' }) loggedIn!: boolean

  created () {
    this.$nextTick(async () => {
      // page is created several times, because of conditions in app.vue to checking configuration object
      try {
        await this.$store.dispatch('auth/logOut')
      } finally {
        window.location.href = '/' // need to hard reload, because of cleaning the store cache
      }
    })
  }
}
</script>
