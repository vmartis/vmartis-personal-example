import chai, { expect } from 'chai'
import chaiThings from 'chai-things'
import { enumToStaticValue } from '@/utils'
import { RecordType } from '@/modules/record/type/RecordType'

chai.use(chaiThings)
chai.should()

describe('utils', () => {
  describe('enumToStaticValue', () => {
    it('all values from enum should be in result', () => {
      const list = enumToStaticValue(RecordType, 'record-type')
      expect(list).to.be.an('array')
      expect(list).to.have.lengthOf(Object.keys(RecordType).length)
    })
  })
})
