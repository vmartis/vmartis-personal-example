'use strict'

import _ from 'lodash'
import Labeled from '@/modules/common/values/labeled'
import i18n from '@/i18n'

export default (value?: string, codeList: Array<Labeled> = [], translate: boolean = false) => {
  if (_.isNil(value)) {
    return null
  } else {
    const codeListItem = _.find(codeList, { value })
    const labelValue: string | null = codeListItem ? codeListItem.label : null
    return translate && labelValue ? i18n.message(labelValue) : labelValue
  }
}
