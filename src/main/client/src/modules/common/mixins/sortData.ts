export default class SortData {
  by?: string
  asc?: boolean

  constructor (by?: string, asc?: boolean) {
    this.by = by
    this.asc = asc
  }
}
