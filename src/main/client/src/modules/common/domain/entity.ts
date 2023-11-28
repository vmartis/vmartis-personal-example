import _ from 'lodash'
import moment from 'moment'
import Labeled from '@/modules/common/values/labeled'

class Entity implements Labeled {
  id?: number | null
  created?: moment.Moment | null
  updated?: moment.Moment | null
  value: any
  label: string
  labelDescription?: string
  searchString?: string
  order?: number
  _enriched = false
  _updateRelations: Array<string> = []

  constructor (data?: any, idProp = 'id', labelProp = 'label', searchProps?: Array<string>) {
    _.merge(this, data)
    if (data && data.created) {
      this.created = this.convertDate(data.created)
    }
    if (data && data.updated) {
      this.updated = this.convertDate(data.updated)
    }

    this.id = data.id ? parseInt(data.id) : null
    this.value = (this as any)[idProp] || ''
    this.label = (this as any)[labelProp] || ''
    this.buildSearchString(searchProps || ['label'])
  }

  convertDate (date: any): moment.Moment | null {
    return date ? moment(date) : null
  }

  // set relations -> entities, which are transferred for create/update
  relations (...args: string[]) {
    this._updateRelations.length = 0
    this._updateRelations.push(...args)
  }

  private buildSearchString (args: Array<string> = []) {
    this.searchString = ' '
    args.forEach(arg => {
      this.searchString += (((this as any)[arg] || '') + ' ')
    })
    // concat string without whitespaces
    this.searchString += this.searchString.replace(/\s/g, '')
  }

  // to be override
  enrich (data: any) {
    this[ENRICHED] = true
  }
}

export { Entity }
export const RELATIONS = '_updateRelations'
export const ENRICHED = '_enriched'
