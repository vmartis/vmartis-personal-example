<script lang="ts">
import _ from 'lodash'
import { Bar, mixins as vueChartMixins } from 'vue-chartjs'
import Component, { mixins } from 'vue-class-component'
import { FORMAT_NUMBER } from '@/utils'
import { ChartData, ChartTooltipItem, InteractionMode } from 'chart.js'
import { Prop } from 'vue-property-decorator'

const { reactiveProp } = vueChartMixins

@Component
export default class BarChart extends mixins(Bar, reactiveProp) {
  @Prop({ type: Boolean, default: true }) responsive!: boolean
  @Prop({ type: Boolean, default: false }) maintainAspectRatio!: boolean
  @Prop({ type: Function, default: (tooltipItem: ChartTooltipItem, data: ChartData) => null }) beforeLabelCallback!: any
  @Prop({ type: Function, default: (tooltipItem: ChartTooltipItem, data: ChartData) => null }) afterLabelCallback!: any
  @Prop({ type: Function }) labelCallback!: any
  @Prop({ default: 'nearest' }) tooltipMode!: InteractionMode

  createChartOptions () {
    return {
      responsive: this.responsive,
      maintainAspectRatio: this.maintainAspectRatio,
      tooltips: {
        mode: this.tooltipMode,
        callbacks: {
          label: this.labelCallback,
          beforeLabel: this.beforeLabelCallback,
          afterLabel: this.afterLabelCallback
        }
      },
      scales: {
        yAxes: [{
          display: true,
          ticks: {
            beginAtZero: true,
            // format numbers on y-axes
            callback: function (value: any, index: number, values: any) {
              return _.isNumber(value) ? FORMAT_NUMBER(value) : value
            }
          }
        }]
      }
    }
  }

  mounted () {
    this.renderChart(this.chartData, this.createChartOptions())
  }
}
</script>
