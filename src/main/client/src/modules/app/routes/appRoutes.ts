import loginView from '../components/login.vue'
import homeView from '../components/home.vue'
import logoutPage from '../components/logoutPage.vue'

export default [
  {
    path: '/',
    name: 'root',
    redirect: '/home',
    meta: {
      requiresLoggedIn: true
    }
  },
  {
    path: '/home',
    name: 'home',
    component: homeView,
    meta: {
      requiresLoggedIn: true
    }
  },
  {
    path: '/logout',
    name: 'logout',
    component: logoutPage,
    meta: {
      requiresLoggedIn: true
    }
  },
  {
    name: 'login',
    path: '/login',
    component: loginView,
    meta: {
      bodyClass: 'login-page'
    }
  }
]
