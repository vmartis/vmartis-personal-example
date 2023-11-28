import BreadCrumbLink from '@/modules/common/components/breadcrumb/breadcrumbLink'

export default class BreadCrumbData {
  links: Array<BreadCrumbLink> = []
  closeRoute?: string
  closeRouteParams?: any

  constructor (links: Array<BreadCrumbLink>, closeRoute?: string, closeRouteParams? :any) {
    this.links = links || []
    this.closeRoute = closeRoute
    this.closeRouteParams = closeRouteParams
  }
}
