import { Route } from 'vue-router'

// resets document title
export default (to: Route, from: Route, next: Function) => {
  window.document.title = 'Qesu'
  next()
}
