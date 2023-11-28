<template>
  <div>
    <password-change-modal
      :show-modal="showModal"
      @close="hidePasswordChange()"></password-change-modal>
    <b-navbar type="dark" variant="primary" class="main-header">
      <b-navbar-nav>
        <b-nav-item data-widget="pushmenu" role="button" ref="pushmenu"><i class="fas fa-bars"></i></b-nav-item>
      </b-navbar-nav>
      <b-navbar-nav class="ml-auto">
        <b-nav-item-dropdown right>
          <!-- Using 'button-content' slot -->
          <template v-slot:button-content>
            <span v-if="user">{{ user.firstname + ' ' + user.surname }}</span>
          </template>
          <b-dropdown-item @click.prevent="showPasswordChange()">{{ 'navbar.password.change' | translate }}
          </b-dropdown-item>
          <b-dropdown-divider></b-dropdown-divider>
          <b-dropdown-item @click.prevent="logout()">{{ 'navbar.logout' | translate }}</b-dropdown-item>
        </b-nav-item-dropdown>
      </b-navbar-nav>
    </b-navbar>
  </div>
</template>
<script lang="ts">
import Component from 'vue-class-component'
import passwordChangeModal from '@/modules/user/components/passwordChangeModal.vue'
import { State } from 'vuex-class'
import User from '@/modules/user/domain/user'
import Vue from 'vue'

@Component({
  components: { passwordChangeModal }
})
export default class Navbar extends Vue {
  showModal: boolean = false

  @State('user', { namespace: 'auth' }) user!: User

  showPasswordChange () {
    this.showModal = true
  }

  hidePasswordChange () {
    this.showModal = false
  }

  async logout () {
    await this.$router.push({ name: 'logout' })
  }

  mounted () {
    // need to init main push menu manually - do not work automatically after login
    const element: any = $(this.$refs.pushmenu)
    element.PushMenu()
  }
}
</script>
