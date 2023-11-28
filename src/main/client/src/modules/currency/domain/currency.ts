import CodeList from '@/modules/common/domain/codeList'

class Currency extends CodeList {
  code?: string

  constructor (data: any) {
    super(data)
    this.value = data.code
  }
}

export default Currency
