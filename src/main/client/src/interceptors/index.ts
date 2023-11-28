import Vue from 'vue'
import autInterceptor from './authInterceptor'
import unauthorizedInterceptor from './unauthorizedInterceptor'
import errorInterceptor from './errorInterceptor'
import dataOptimalizationInterceptor from './dataOptimalizationInterceptor'
import { Store } from 'vuex'
import { VueRouter } from 'vue-router/types/router'

export default (store: Store<any>, router: VueRouter) => {
  // @ts-ignore
  Vue.http.interceptors.push(autInterceptor(store))
  // @ts-ignore
  Vue.http.interceptors.push(unauthorizedInterceptor(store, router))
  // @ts-ignore
  Vue.http.interceptors.push(errorInterceptor(store, router))
  // @ts-ignore
  Vue.http.interceptors.push(dataOptimalizationInterceptor())
}
