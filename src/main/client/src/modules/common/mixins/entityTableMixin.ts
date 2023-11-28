import _ from 'lodash'
import { compareFunction, normalize } from '@/utils'
import Component from 'vue-class-component'
import { Entity } from '@/modules/common/domain/entity'
import EntityFetchParams from '@/modules/common/store/entityFetchParams'
import notificationService from '@/modules/common/services/notificationService'
import { Prop } from 'vue-property-decorator'
import SortData from '@/modules/common/mixins/sortData'
import Vue from 'vue'

@Component
export default class EntityTableMixin<T extends Entity> extends Vue {
  showCreateModal: boolean = false
  selectedItem?: T | null = null
  selectedItems: Array<T> = []
  query: string | null = null
  showInactive: boolean = false
  activeSupport: boolean = true
  itemsFetchAction: string = 'getAll'
  itemsGetter: string = 'all'
  itemsActiveGetter: string = 'active'
  entityModuleName?: string
  sortData: SortData | null = null
  fetchActionParams = new EntityFetchParams(true)
  newItemTemplateDefault: Function = (data?: any) => new Entity({})

  @Prop(String) title?: String | undefined
  @Prop({ type: Boolean, required: false, default: false }) showQueryFilter?: boolean
  @Prop({ type: Function, required: false, default: (item: T) => true }) filterFnc!: Function
  @Prop({ type: Function, required: false }) newItemTemplate?: Function

  get items (): Array<T> {
    let items: Array<T>
    items = this.$store.getters[this.entityModuleName + '/' + ((this.activeSupport && !this.showInactive) ? this.itemsActiveGetter : this.itemsGetter)]
    items = this.transformItems(items)
    if (this.query) {
      const regexp = new RegExp(normalize(this.query), 'g')
      return this.sortEntities(items.filter(item => !!normalize(item.searchString).match(regexp)).filter(this.filterFnc as any))
    } else {
      return this.sortEntities(_.filter(items, this.filterFnc as any))
    }
  }

  transformItems (items: T[]): any[] {
    return items
  }

  sortEntities (entities: T[]) {
    if (!this.sortData || !this.sortData.by) {
      return entities
    } else {
      const sorted = entities.sort(compareFunction(this.sortData.by!))
      return this.sortData.asc ? sorted : sorted.reverse()
    }
  }

  sort (sortData: SortData) {
    if (!this.sortData) {
      return
    }
    this.sortData.by = sortData.by
    this.sortData.asc = sortData.asc
  }

  get selectedIds (): Array<number> {
    return _.map(this.selectedItems, 'id') as Array<number>
  }

  createShow (data?: any) {
    this.selectedItem = this.newItemTemplate ? this.newItemTemplate(data) : this.newItemTemplateDefault(data)
    this.showCreateModal = true
  }

  createHide () {
    this.showCreateModal = false
  }

  editItem (item: T) {
    this.selectedItem = _.cloneDeep(item)
    this.showCreateModal = true
  }

  positionDown (item: T) {
    // because of filters we need to set position of next item in order
    const indexOfNextItem = this.items.indexOf(item) + 1
    // last index check
    if (this.items.length <= indexOfNextItem) {
      return
    }
    const position = this.items[indexOfNextItem].order

    this.updatePosition(item, position as number)
  }

  positionUp (item: T) {
    // because of filters we need to set position of next item in order
    const indexOfPrevItem = this.items.indexOf(item) - 1
    // first index check
    if (indexOfPrevItem < 0) {
      return
    }
    const position = this.items[indexOfPrevItem].order

    this.updatePosition(item, position as number)
  }

  updatePosition (item: T, position: number) {
    this.$store.dispatch(this.entityModuleName + '/updatePosition', { id: item.id, position }).then((result: any) => {
      if (result) {
        notificationService.success('entity.edit.success')
      } else {
        notificationService.error('entity.edit.error')
      }
    })
  }

  // can be overridden
  async beforeFetch () {
  }

  async fetch () {
    await this.beforeFetch()
    await this.$store.dispatch(this.entityModuleName + '/' + this.itemsFetchAction, this.fetchActionParams)
  }

  async beforeMount () {
    await this.fetch()
  }

  select (item: T) {
    if (this.isSelected(item)) {
      // @ts-ignore
      this.selectedItems = _.reject(this.selectedItems, { id: item.id })
    } else {
      this.selectedItems = [...this.selectedItems, item]
    }
  }

  selectAll () {
    // all are selected
    if (this.items.length && this.items.length === this.selectedItems.length) {
      this.selectedItems = []
    } else {
      this.selectedItems = [...this.items]
    }
  }

  isSelected (item: any) {
    return _.some(this.selectedItems, { id: item.id })
  }
}
