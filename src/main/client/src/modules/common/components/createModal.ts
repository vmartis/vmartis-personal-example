import _ from 'lodash'
import Component, { mixins } from 'vue-class-component'
import confirmMixin from '@/modules/common/mixins/confirmMixin'
import EntityCreateParams from '@/modules/common/store/entityCreateParams'
import EntityDeleteParams from '@/modules/common/store/entityDeleteParams'
import { Entity } from '@/modules/common/domain/entity'
import notificationService from '@/modules/common/services/notificationService'
import { Prop, Watch } from 'vue-property-decorator'
import submitProtectionMixin from '@/modules/common/mixins/submitProtectionMixin'

@Component
export default class EntityCreateModal<T extends Entity> extends mixins(submitProtectionMixin, confirmMixin) {
  item: T | null = null
  show: boolean = false
  moduleName?: string
  doFetchAfterDefault = true
  doFetchAfterDelete = true
  doFetchLatestAfterDelete = false
  modelInitialized = false

  @Prop({ type: Object }) model!: T
  @Prop({ type: Boolean, default: false }) showModal!: boolean
  @Prop({ type: Boolean, default: true }) doFetchAfter!: boolean
  @Prop({ type: Boolean, default: false }) doFetchLatestAfter!: boolean

  onSubmit () {
    this.beforeSave()

    if (!this.validate()) {
      return this.unprotect()
    }

    let operation = this.moduleName || ''
    if (this.isEdit()) {
      operation += '/update'
    } else {
      operation += '/create'
    }

    this.$store.dispatch(operation, new EntityCreateParams(this.item!, this.doFetchAfter && this.doFetchAfterDefault, this.doFetchLatestAfter)).then((item) => {
      notificationService.success('entity.edit.success')
      this.afterSuccessSave(item)
      this.onClose()
    }).finally(this.unprotect)
  }

  afterSuccessSave (item: T) {
  }

  afterSuccessDelete () {
  }

  beforeSave () {
  }

  validate () {
    return true
  }

  isEdit () {
    return !_.isNil(this.item) && !_.isNil(this.item.id)
  }

  beforeClose () {
  }

  afterModelSet () {
  }

  onClose () {
    this.beforeClose()
    this.$emit('close')
  }

  @Watch('model')
  onModelChange (newValue: any) {
    this.modelInitialized = false
    this.item = _.cloneDeep(newValue)
    // mark that model is initialized
    this.$nextTick(() => {
      this.afterModelSet()
      this.modelInitialized = true
    })
  }

  @Watch('showModal')
  onShowModalChange (showModal: boolean) {
    this.show = showModal
  }

  deleteItem () {
    this.confirm('entity.delete.confirmation').then((value) => {
      if (value) {
        this.$store.dispatch(this.moduleName + '/delete', new EntityDeleteParams(this.item!.id!, this.doFetchAfterDelete, this.doFetchLatestAfterDelete)).then((result) => {
          if (result) {
            this.$emit('close')
            notificationService.success('entity.delete.success')
            this.afterSuccessDelete()
          }
        }).finally(this.unprotect)
      }
    })
  }
}
