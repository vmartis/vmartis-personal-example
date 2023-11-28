import partners from '../components/partnersPage.vue'
import partnerDetail from '../components/partnerDetailPage.vue'

export default [
  {
    path: '/partners',
    component: partners,
    name: 'partners',
    meta: {
      requiresLoggedIn: true,
      requiresPermission: 'COMPANY_PARTNER',
      pageGroup: 'company'
    }
  },
  {
    name: 'partnerDetail',
    path: '/partners/:partnerId',
    component: partnerDetail,
    props: true,
    meta: {
      requiresLoggedIn: true,
      requiresPermission: 'COMPANY_PARTNER',
      pageGroup: 'company'
    }
  }
]
