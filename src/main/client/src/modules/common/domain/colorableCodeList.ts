import CodeList from '@/modules/common/domain/codeList'

class ColorableCodeList extends CodeList {
  color: string

  constructor (data: any) {
    super(data)
    this.color = data.color || ''
  }
}

export default ColorableCodeList
