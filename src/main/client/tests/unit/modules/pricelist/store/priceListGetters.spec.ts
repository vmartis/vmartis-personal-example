/* eslint-disable no-unused-expressions */
import Company from '@/modules/company/domain/company'
import EntityState from '@/modules/common/store/entityState'
import chai, { expect } from 'chai'
import chaiThings from 'chai-things'
import moment from 'moment'
import priceListGetters from '@/modules/pricelist/store/priceListGetters'
import PriceList from '@/modules/pricelist/domain/priceList'
import PriceListItem from '@/modules/pricelist/domain/priceListItem'
import Item from '@/modules/item/domain/Item'

chai.use(chaiThings)
chai.should()

describe('priceListGetters', () => {
  const companyId = 1
  const itemId = 2
  const specificCompanyPriceList = new PriceList({
    item: new Item({ id: itemId }),
    validFrom: moment().startOf('day').subtract(1, 'day'),
    company: new Company({ id: companyId }),
    items: [new PriceListItem({ currency: 'CZK' })]
  })
  const globalPriceList = new PriceList({
    item: new Item({ id: itemId }),
    validFrom: moment().startOf('day').subtract(5, 'day'),
    items: [new PriceListItem({ currency: 'CZK' }), new PriceListItem({ currency: 'EUR' })]
  })
  const entityState = new EntityState<PriceList>()
  entityState.items = [specificCompanyPriceList, globalPriceList]

  describe('validForDate', () => {
    const validForDateF = (priceListGetters as any).validForDate(entityState)

    it('should return specific company price list', () => {
      const priceList = validForDateF(moment(), itemId, companyId, 'CZK')
      expect(priceList).to.be.equal(specificCompanyPriceList)
    })

    it('should return global price list because of missing price list item for EUR currency', () => {
      const priceList = validForDateF(moment(), itemId, companyId, 'EUR')
      expect(priceList).to.be.equal(globalPriceList)
    })

    it('should return global price list', () => {
      const priceList = validForDateF(moment().subtract(3, 'day'), itemId, companyId, 'CZK')
      expect(priceList).to.be.equal(globalPriceList)
    })

    it('should return null because no price list exists for date', () => {
      const priceList = validForDateF(moment().subtract(6, 'day'), itemId, companyId, 'CZK')
      expect(priceList).to.be.null
    })

    it('should return null because of unknown currency', () => {
      const priceList = validForDateF(moment(), itemId, companyId, 'KKK')
      expect(priceList).to.be.null
    })
  })

  describe('hasValidPriceList', () => {
    const hasValidPriceList = (priceListGetters as any).hasValidPriceList(entityState)

    it('should return true for existing specific company price list with CZK currency item', () => {
      const exists = hasValidPriceList(moment(), itemId, companyId, 'CZK')
      expect(exists).to.be.true
    })

    it('should return true for existing specific company price list with EUR currency item', () => {
      const exists = hasValidPriceList(moment(), itemId, companyId, 'EUR')
      expect(exists).to.be.true
    })

    it('should return true for existing global price list with CZK currency item', () => {
      const nonExistingCompanyId = 99999
      const exists = hasValidPriceList(moment(), itemId, nonExistingCompanyId, 'CZK')
      expect(exists).to.be.true
    })

    it('should return false for non-existing price list with PLN currency item', () => {
      const exists = hasValidPriceList(moment(), itemId, companyId, 'PLN')
      expect(exists).to.be.false
    })

    it('should return false for non-existing price list for date', () => {
      const exists = hasValidPriceList(moment().subtract(6, 'day'), itemId, companyId, 'CZK')
      expect(exists).to.be.false
    })
  })
})
