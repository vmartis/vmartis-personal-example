import { createLocalVue, mount, Wrapper } from '@vue/test-utils'
import the from '@/modules/common/components/the.vue'
import { expect } from 'chai'
import translateFilter from '@/modules/common/filters/translateFilter'

describe('the.vue', () => {
  const localVue = createLocalVue()
  localVue.filter('translate', translateFilter) // the component depends on translate filter

  const sortData = {
    by: 'name',
    asc: true
  }

  let wrapper: Wrapper<any>

  beforeEach(function () {
    wrapper = mount(the, {
      localVue,
      propsData: {
        label: 'common.name',
        property: 'name',
        sortData: sortData
      }
    })
  })

  it('should render correct label', () => {
    expect(wrapper.text()).to.equal(translateFilter('common.name'))
  })

  it('should has correct order asc class', () => {
    const icon = wrapper.get('i')
    expect(icon.classes()).to.contains('fa-sort-amount-down-alt')
  })

  it('should has correct order desc class', (done) => {
    wrapper.setProps({
      sortData: {
        by: 'name',
        asc: false
      }
    })
    wrapper.vm.$nextTick(() => {
      expect(wrapper.get('i').classes()).to.contains('fa-sort-amount-down')
      done()
    })
  })

  it('should has correct order inactive class', (done) => {
    wrapper.setProps({
      sortData: {
        by: 'other',
        asc: true
      }
    })
    wrapper.vm.$nextTick(() => {
      expect(wrapper.get('i').classes()).to.contains('fa-sort')
      done()
    })
  })
})
