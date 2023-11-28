<template>
  <div class="hold-transition login-page">
    <div class="login-box">
      <div class="login-logo">
        <img src="./../../../img/logo-color.svg"/>
      </div>
      <div class="card">
        <div class="card-body login-card-body">
          <v-form ref="form">
            <text-field field-id="username"
                        label="user.username.label"
                        placeholder="user.username.placeholder"
                        :maxlength="100"
                        v-model="credentials.username"
                        validation="required|min:5|max:100"
                        autocomplete="username"
                        :focus="true"></text-field>
            <text-field field-id="password"
                        label="user.password.label"
                        placeholder="user.password.placeholder"
                        v-model="credentials.password"
                        :maxlength="20"
                        autocomplete="current-password"
                        type="password"
                        validation="required|min:5|max:20"></text-field>
            <div class="row">
              <div class="col-12">
                <div class="pull-right">
                  <button class="btn btn-primary btn-block btn-flat" type="submit" :disabled="protected()"
                          @click.prevent="submit($refs.form)">
                    {{ 'login.submit' | translate }}
                  </button>
                </div>
              </div>
            </div>
          </v-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">

import authService from '@/modules/auth/services/authService'
import Component, { mixins } from 'vue-class-component'
import { Getter, State } from 'vuex-class'
import textField from '@/modules/common/components/form/textField.vue'
import User from '@/modules/user/domain/user'
import validationMixin from '@/modules/common/components/form/validationMixin'
import vForm from '@/modules/common/components/form/vForm.vue'

@Component({
  components: { vForm, textField }
})
export default class Login extends mixins(validationMixin) {
  credentials = { username: '', password: '' }

  @Getter('loggedIn', { namespace: 'auth' }) loggedIn!: boolean
  @State('user', { namespace: 'auth' }) user!: User

  onSubmit () {
    return this.$store.dispatch('auth/logIn', this.credentials).then((loggedIn: boolean) => {
      if (loggedIn) {
        this.$router.push(authService.mainPath(this.$router.currentRoute, this.user))
      }
    })
  }
}
</script>
