import Configuration from '@/modules/configuration/domain/configuration'
import PageDataSet from '@/modules/app/store/pageDataSet'

export default class AppState {
  submitProtection: boolean = false
  configuration: Configuration | null = null
  loadingData: Array<string> = []
  pageDataSet: PageDataSet = new PageDataSet()
  pageGroup: string | null = null
}
