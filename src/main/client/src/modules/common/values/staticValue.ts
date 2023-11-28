import Labeled from '@/modules/common/values/labeled'

class StaticValue implements Labeled {
  value: any
  label: string
  description?: string

  constructor (value: any, label: string, description?: string) {
    this.value = value
    this.label = label
    this.description = description
  }
}

export default StaticValue
