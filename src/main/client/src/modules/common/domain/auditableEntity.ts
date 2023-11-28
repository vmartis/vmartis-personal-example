import moment from 'moment'
import User from '../../user/domain/user'

export default interface AuditableEntity {
  created?: moment.Moment | null
  createdBy?: User | null
  updatedBy?: User | null
  updated?: moment.Moment | null
}
