// @ts-ignore
import { expect } from 'chai'
import sk, { validations as skValidations } from '@/i18n/sk'
import cs, { validations as csValidations } from '@/i18n/cs'

describe('i18n.keys', () => {
  it('cs locale should have same keys number as sk locale', () => {
    expect(Object.keys(cs).length).to.equal(Object.keys(sk).length)
    expect(Object.keys(csValidations).length).to.equal(Object.keys(skValidations).length)
  })

  it('cs locale should have same keys as sk locale', () => {
    expect(Object.keys(cs)).to.eql(Object.keys(sk))
    expect(Object.keys(csValidations)).to.eql(Object.keys(csValidations))
  })
})
