import './classComponentHooks' // need to be at first place before any component is initialized
// init resource lib need to be done before other imports
import './resourceInit'
import '@/modules/common/directives'
import router from './router'
import './store/init'
import store from './store'
import interceptors from '@/interceptors'
import hooks from '@/hooks'
import 'bootstrap'
import BootstrapVue from 'bootstrap-vue'
import 'admin-lte/plugins/jquery/jquery'
import 'admin-lte/dist/js/adminlte' // it must be here for loading adminLte
import App from '@/modules/app/components/app.vue'
import validationsInit from '@/modules/common/validation'
import notificationService from '@/modules/common/services/notificationService'
import filters from '@/modules/common/filters'
import moment from 'moment'
import { extendMoment } from 'moment-range'
import 'moment-duration-format'
// @ts-ignore
import momentSk from 'moment/locale/sk'
import numeral from 'numeral'
import 'numeral/locales/sk'
import urlCleanHook from '@/hooks/urlCleanHook'
import i18n from '@/i18n/i18n'
import Vue from 'vue'
import VueMeta from 'vue-meta'
import vatCalculator from '@/modules/finance/services/vatCalculator'

// first step, clean invalid URL, e.g. facebook fbclid etc. COntinue only if url was not cleared
if (!urlCleanHook()) {
  // override moment default toJSON which use UTC time zone, because of using LocalDate in BE
  moment.fn.toJSON = function () {
    return this.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]')
  }

  vatCalculator.init(store)

  // set moment locale to slovak
  moment.locale('sk')

  // extend moment with moment-range
  extendMoment(moment as any)
  // set locale to numeral
  numeral.locale('sk')

  validationsInit()

  filters()
  // register http interceptor
  interceptors(store, router)

  // register hooks
  hooks(store, router)

  // init bootstrap-vue
  Vue.use(BootstrapVue)

  Vue.use(VueMeta)

  Vue.config.productionTip = false

  new Vue({
    router,
    store,
    i18n,
    render: h => h(App)
  }).$mount('#app')

  notificationService.init()
}
