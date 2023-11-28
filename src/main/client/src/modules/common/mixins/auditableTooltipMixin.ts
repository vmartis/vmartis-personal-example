import AuditableEntity from '../domain/auditableEntity'
import Component from 'vue-class-component'
import i18n from '@/i18n'
import { FORMAT_DATE_TIME } from '@/utils'
import Vue from 'vue'

@Component
export default class AuditableTooltipMixin extends Vue {
  auditableTooltip (item?: AuditableEntity) {
    if (!item || !item.createdBy) {
      return null
    }

    const title = item.updatedBy
      ? i18n.message('auditable.tooltip.updated', [item.createdBy!.fullName, FORMAT_DATE_TIME(item.created!), item.updatedBy!.fullName, FORMAT_DATE_TIME(item.updated!)])
      : i18n.message('auditable.tooltip.created', [item.createdBy!.fullName, FORMAT_DATE_TIME(item.created!)])

    return { title: title, html: true, customClass: 'text-nowrap', interactive: true }
  }
}
