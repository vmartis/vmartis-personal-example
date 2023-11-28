import moment from 'moment'

class Range {
  from?: moment.Moment
  to?: moment.Moment

  constructor (from?: moment.Moment, to?: moment.Moment) {
    this.from = from
    this.to = to
  }
}

export default Range
