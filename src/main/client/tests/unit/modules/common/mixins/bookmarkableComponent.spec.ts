import Vue from 'vue'
import { createLocalVue, shallowMount, Wrapper } from '@vue/test-utils'
import bookmarkableComponent from '@/modules/common/mixins/bookmarkableComponent'
import VueRouter from 'vue-router'
import moment from 'moment'
import Range from '@/modules/common/components/form/range'
import { expect } from 'chai'
import { FORMAT_SYSTEM_DATE } from '@/utils'
import FilterData from '@/modules/common/mixins/filterData'
import SortData from '@/modules/common/mixins/sortData'

describe('bookmarkableComponent', () => {
  let wrapper: Wrapper<any>
  const localVue = createLocalVue()
  localVue.use(VueRouter)
  const router = new VueRouter()
  const now = moment()
  const monthAgo = moment().subtract(30, 'month')

  afterEach(() => {
    router.push({ query: {} })
  })

  describe('component with empty default filter/sort/query data', () => {
    beforeEach(function () {
      const component = Vue.component('mixin-test',
        {
          template: '<div></div>',
          mixins: [bookmarkableComponent]
        })
      wrapper = shallowMount(component,
        {
          localVue,
          router
        })
    })

    it('should reflect filter data change to route query', (done) => {
      wrapper.setData({
        filter: new FilterData({
          prop1: 'prop1 & value X///',
          prop2: 12345,
          prop3: [1, 2, 3],
          prop4: ['code1', 'code2'],
          prop5: new Range(monthAgo, now)
        })
      })

      wrapper.vm.$nextTick(() => {
        expect(wrapper.vm.filter.prop1).to.equal('prop1 & value X///')
        expect(wrapper.vm.$route.query).to.have.property('fs_prop1')
        expect(wrapper.vm.$route.query).to.have.property('fn_prop2')
        expect(wrapper.vm.$route.query).to.have.property('fa_prop3')
        expect(wrapper.vm.$route.query).to.have.property('fa_prop4')
        expect(wrapper.vm.$route.query.fs_prop1).to.equal(encodeURI('prop1 & value X///'))
        expect(wrapper.vm.$route.query.fn_prop2).to.equal(encodeURI(12345 + ''))
        expect(wrapper.vm.$route.query.fa_prop3).to.equal(encodeURI([1, 2, 3] + ''))
        expect(wrapper.vm.$route.query.fa_prop4).to.equal(encodeURI(['code1', 'code2'] + ''))
        expect(wrapper.vm.$route.query.frf_prop5).to.equal(encodeURI(FORMAT_SYSTEM_DATE(monthAgo) + ''))
        expect(wrapper.vm.$route.query.frt_prop5).to.equal(encodeURI(FORMAT_SYSTEM_DATE(now) + ''))
        done()
      })
    })

    it('should reflect sort data change to route query', (done) => {
      wrapper.setData({ sortData: { asc: false, by: 'orderTo   xx' } })

      wrapper.vm.$nextTick(() => {
        expect(wrapper.vm.$route.query).to.have.property('o_asc')
        expect(wrapper.vm.$route.query.o_asc).to.equal(encodeURI('false'))

        expect(wrapper.vm.$route.query).to.have.property('o_by')
        expect(wrapper.vm.$route.query.o_by).to.equal(encodeURI('orderTo   xx'))
        done()
      })
    })

    it('should reflect activeTabIndex change to route query', (done) => {
      wrapper.setData({
        activeTabIndex: 5
      })

      wrapper.vm.$nextTick(() => {
        expect(wrapper.vm.$route.query.tab).to.equal(encodeURI(5 + ''))
        done()
      })
    })

    it('should parse query with filter/sort/query parameters', async () => {
      await router.push('/?fs_prop1=abc&fn_prop2=11111&o_by=name&o_asc=false&fs_query=abcd&fa_prop3=1,2,3&fa_prop4=code1,code2&frf_prop5=2019-01-05&frt_prop5=2019-01-30&tab=8')
      expect(wrapper.vm.sortData.asc).to.equal(false)
      expect(wrapper.vm.sortData.by).to.equal(encodeURI('name'))
      expect(wrapper.vm.filter.prop1).to.equal(encodeURI('abc'))
      expect(wrapper.vm.filter.prop2).to.equal(11111)
      expect(wrapper.vm.filter.query).to.equal(encodeURI('abcd'))
      expect(wrapper.vm.filter.prop3).to.eql([1, 2, 3])
      expect(wrapper.vm.filter.prop4).to.eql(['code1', 'code2'])
      expect(wrapper.vm.activeTabIndex).to.eql(8)
      expect(FORMAT_SYSTEM_DATE(wrapper.vm.filter.prop5.from)).to.equal('2019-01-05')
      expect(FORMAT_SYSTEM_DATE(wrapper.vm.filter.prop5.to)).to.equal('2019-01-30')
    })
  })

  describe('component with defined filter and sort data', () => {
    beforeEach(async () => {
      const component = await Vue.extend({
        mixins: [bookmarkableComponent],
        template: '<div></div>'
      })
      wrapper = await shallowMount(
        component,
        {
          methods: {
            defaultFilter () {
              // @ts-ignore
              this.sortData = new SortData('orderTo', true)
              // @ts-ignore
              this.filter = new FilterData({
                year: null,
                month: 0,
                status: 'OPEN',
                query: 'ABCDEF',
                prop3: [1, 2, 3],
                prop4: ['code1', 'code2'],
                prop5: new Range(monthAgo, now)
              })
            }
          },
          localVue,
          router
        })
    })

    it('should initial reflect filter/sort/query data change to route query', () => {
      expect(wrapper.vm.$route.query).to.have.property('fs_status')
      expect(wrapper.vm.$route.query).to.have.property('fn_month')
      expect(wrapper.vm.$route.query).to.not.have.property('fs_year')
      expect(wrapper.vm.$route.query).to.not.have.property('fn_year')
      expect(wrapper.vm.$route.query).to.have.property('fa_prop3')
      expect(wrapper.vm.$route.query).to.have.property('fa_prop4')
      expect(wrapper.vm.$route.query.fs_status).to.equal(encodeURI('OPEN'))
      expect(wrapper.vm.$route.query.fn_month).to.equal(encodeURI('0'))
      expect(wrapper.vm.$route.query.o_by).to.equal(encodeURI('orderTo'))
      expect(wrapper.vm.$route.query.o_asc).to.equal(encodeURI('true'))
      expect(wrapper.vm.$route.query.fa_prop3).to.eql(encodeURI([1, 2, 3].toString()))
      expect(wrapper.vm.$route.query.fa_prop4).to.eql(encodeURI(['code1', 'code2'].toString()))
      expect(wrapper.vm.$route.query.frf_prop5).to.equal(encodeURI(FORMAT_SYSTEM_DATE(monthAgo) + ''))
      expect(wrapper.vm.$route.query.frt_prop5).to.equal(encodeURI(FORMAT_SYSTEM_DATE(now) + ''))
    })
  })
})
