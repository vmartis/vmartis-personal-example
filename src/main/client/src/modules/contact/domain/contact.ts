import { Entity } from '@/modules/common/domain/entity'
import Subject from '@/modules/subject/domain/subject'
import subjectService from '@/modules/subject/services/subjectService'

class Contact extends Entity {
  name?: string
  email?: string
  phoneNumber?: string
  subject?: Subject | null
  note?: string
  position?: string
  active?: boolean

  constructor (data: any) {
    super(data, 'id', 'name')

    this.subject = subjectService.getSubject(data.subject)

    this.extendSearchString()
  }

  private extendSearchString () {
    this.searchString = this.name || ''
    if (this.phoneNumber) {
      this.searchString += ' ' + this.phoneNumber
    }
    if (this.email) {
      this.searchString += ' ' + this.email
    }
    if (this.subject) {
      this.searchString += ' ' + this.subject.label
    }
    // concat string without whitespaces
    this.searchString += this.searchString.replace(/\s/g, '')
  }
}

export default Contact
