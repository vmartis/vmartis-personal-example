export default class EntityDeleteParams {
  id: number
  doFetchAfter: boolean
  doFetchLatestAfter: boolean

  constructor (id: number, doFetchAfter = true, doFetchLatestAfter = false) {
    this.id = id
    this.doFetchAfter = doFetchAfter
    this.doFetchLatestAfter = doFetchLatestAfter
  }
}
