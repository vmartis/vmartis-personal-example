import { Entity } from './entity'
import moment from 'moment'

class CodeList extends Entity {
  validFrom?: moment.Moment | null
  validTo?: moment.Moment | null

  constructor (data: any) {
    super(data)

    this.validFrom = this.convertDate(this.validFrom)
    this.validTo = this.convertDate(this.validTo)
    this.value = this.id
  }

  validInDate (date: moment.Moment) {
    return !this.validTo || this.validTo.isSameOrAfter(date || moment())
  }

  get valid () {
    return !this.validTo || this.validTo.isSameOrAfter(moment())
  }

  set valid (val) {
    if (val) {
      this.validTo = null
    } else {
      this.validTo = moment().subtract(1, 'day')
    }
  }
}

export default CodeList
