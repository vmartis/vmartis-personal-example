import { expect } from 'chai'
import { calculateTotalWithoutVat, calculateTotalWithVat, calculateVat } from '@/modules/finance/services/financeUtils'
import { VatCalculationType } from '@/modules/configuration/type/VatCalculationType'

describe('financeUtils', () => {
  describe('calculateVat', () => {
    it('SUM calculateType should return value', () => {
      const value = calculateVat(VatCalculationType.SUM, 1.12, 16, 20)
      expect(value).to.be.equal(3.58)
    })
    it('ITEM calculateType should return value', () => {
      const value = calculateVat(VatCalculationType.ITEM, 1.12, 16, 20)
      expect(value).to.be.equal(3.57)
    })
  })

  describe('calculateTotalWithoutVat', () => {
    it('SUM calculateType should return value', () => {
      const value = calculateTotalWithoutVat(VatCalculationType.SUM, 1.12, 16, 20)
      expect(value).to.be.equal(17.92)
    })
    it('ITEM calculateType should return value', () => {
      const value = calculateTotalWithoutVat(VatCalculationType.ITEM, 1.12, 16, 20)
      expect(value).to.be.equal(17.87)
    })
  })

  describe('calculateTotalWithVat', () => {
    it('SUM calculateType should return value', () => {
      const value = calculateTotalWithVat(VatCalculationType.SUM, 1.12, 16, 20)
      expect(value).to.be.equal(21.5)
    })
    it('ITEM calculateType should return value', () => {
      const value = calculateTotalWithVat(VatCalculationType.ITEM, 1.12, 16, 20)
      expect(value).to.be.equal(21.44)
    })
  })
})
