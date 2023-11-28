import contacts from '../components/contactsPage.vue'

export default [
  {
    path: '/contacts',
    component: contacts,
    name: 'contacts',
    meta: {
      requiresLoggedIn: true,
      pageGroup: 'company'
    }
  }
]
