import { expect } from 'chai'
import optimize from '@/interceptors/dataOptimalizationInterceptor'
import { ENRICHED, Entity, RELATIONS } from '@/modules/common/domain/entity'
import Address from '@/modules/common/domain/address'
import CodeList from '@/modules/common/domain/codeList'

describe('dataOptimalizationInterceptor', () => {
  describe('optimize function', () => {
    let entity: any
    let request: any
    beforeEach(() => {
      entity = new Entity({
        id: 1,
        '@type': 'Entity1',
        foo: 1,
        foo2: new Entity({
          id: 2,
          '@type': 'Entity2',
          boo: 8
        }),
        items: [new Entity({
          id: 3,
          '@type': 'Entity3',
          boo: 10
        })],
        address: new Address({
          region: new CodeList({
            id: 3,
            '@type': 'Entity4',
            value: 10
          })
        })
      })

      request = {
        method: 'POST',
        body: entity
      }
    })

    it('nested objects should be optimized', () => {
      optimize()(request)

      expect(request.body).to.have.keys('id', '@type', 'foo', 'foo2', 'items', 'address', 'label', 'value', 'searchString', RELATIONS)
      expect(request.body.foo2).to.have.keys('id', '@type')
      expect(request.body.items).to.have.lengthOf(1)
      expect(request.body.items[0]).to.have.keys('id', '@type')
    })

    it('defined relation should not be optimized', () => {
      entity.relations('items', 'foo2')

      optimize()(request)

      expect(request.body).to.have.keys('id', '@type', 'foo', 'foo2', 'items', 'address', 'label', 'value', 'searchString', RELATIONS)
      expect(request.body.foo2).to.have.keys('id', '@type', 'boo', 'label', 'value', 'searchString', RELATIONS, ENRICHED)
      expect(request.body.items).to.have.lengthOf(1)
      expect(request.body.items[0]).to.have.keys('id', '@type', 'boo', 'label', 'value', 'searchString', RELATIONS, ENRICHED)
    })

    it('all objects in array should be optimized', () => {
      request.body = [entity]
      optimize()(request)

      expect(request.body[0]).to.have.keys('id', '@type')
    })

    it('entity in non-entity object should be optimized', () => {
      optimize()(request)

      expect(request.body.address.region).to.have.keys('id', '@type')
    })
  })
})
