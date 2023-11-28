import Component from 'vue-class-component'
import i18n from '@/i18n'
import { normalizeFilename } from '@/utils'
import XLSX from 'xlsx'
import Vue from 'vue'

const DATE_FORMAT = 'dd"."mm"."yyyy'

@Component
export default class ExportMixin extends Vue {
  exportToExcel (data: Array<any>, fileNameKey: string) {
    const options = {
      dateNF: DATE_FORMAT,
      cellDates: true,
      sheetStubs: false
    }
    /* convert state to workbook */
    const ws = XLSX.utils.aoa_to_sheet(data, options)
    const wb = XLSX.utils.book_new()
    XLSX.utils.book_append_sheet(wb, ws)
    /* generate file and send to client */
    XLSX.writeFile(wb, normalizeFilename(i18n.message(fileNameKey)) + '.xlsx')
  }
}
