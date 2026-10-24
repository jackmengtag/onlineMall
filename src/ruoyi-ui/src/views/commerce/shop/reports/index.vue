<template>
  <div class="app-container">
    <el-form :inline="true">
      <el-form-item label="统计范围">
        <el-select v-model="days" style="width: 160px" @change="getReport">
          <el-option label="近 7 天" :value="7" />
          <el-option label="近 30 天" :value="30" />
          <el-option label="近 90 天" :value="90" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="getReport">查询</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="16" v-loading="loading">
      <el-col v-for="item in metrics" :key="item.label" :xs="12" :sm="6" class="metric-col">
        <el-card shadow="hover" class="metric-card">
          <div class="metric-label">{{ item.label }}</div>
          <div class="metric-value" :style="{ color: item.color }">{{ item.value }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16">
      <el-col :xs="24" :lg="15">
        <el-card shadow="never" class="chart-card">
          <template #header>
            <span>日销售趋势</span>
          </template>
          <div ref="salesChartRef" class="chart-box"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="9">
        <el-card shadow="never" class="chart-card">
          <template #header>
            <span>订单状态分布</span>
          </template>
          <div ref="statusChartRef" class="chart-box"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="ShopReport">
import * as echarts from 'echarts'
import { getShopReport } from '@/api/commerce/shop/report'
import { getShopSummary } from '@/api/commerce/shop/dashboard'
import { formatMoney, fromMinor } from '@/utils/commerce'

const { proxy } = getCurrentInstance()
const { commerce_order_status } = useDict('commerce_order_status')

const loading = ref(false)
const days = ref(7)
const report = ref({})
const statusDistribution = ref([])
const salesChartRef = ref(null)
const statusChartRef = ref(null)
let salesChart = null
let statusChart = null

const num = (value) => {
  const result = Number(value)
  return isNaN(result) ? 0 : result
}

const metrics = computed(() => [
  { label: '销售额', value: formatMoney(report.value.salesAmount || 0), color: '#409eff' },
  { label: '订单数', value: num(report.value.orderCount), color: '#67c23a' },
  { label: '客单价', value: formatMoney(report.value.avgOrderAmount || 0), color: '#e6a23c' },
  { label: '退款额', value: formatMoney(report.value.refundAmount || 0), color: '#f56c6c' }
])

function getReport() {
  loading.value = true
  getShopReport({ days: days.value }).then(response => {
    report.value = response.data || {}
    nextTick(() => renderSalesChart())
  }).finally(() => {
    loading.value = false
  })
}

function getStatusDistribution() {
  if (!proxy.$auth.hasPermi('commerce:shop:dashboard:view')) {
    statusDistribution.value = []
    nextTick(() => renderStatusChart())
    return
  }
  getShopSummary().then(response => {
    const data = response.data || {}
    statusDistribution.value = Array.isArray(data.statusDistribution) ? data.statusDistribution : []
    nextTick(() => renderStatusChart())
  }).catch(() => {
    statusDistribution.value = []
    nextTick(() => renderStatusChart())
  })
}

function renderSalesChart() {
  if (!salesChartRef.value) {
    return
  }
  if (!salesChart) {
    salesChart = echarts.init(salesChartRef.value)
  }
  const trend = Array.isArray(report.value.dailyTrend) ? report.value.dailyTrend : []
  salesChart.setOption({
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['销售额', '订单数'],
      top: 0
    },
    grid: {
      left: 50,
      right: 50,
      top: 45,
      bottom: 30
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: trend.map(item => item.statDate)
    },
    yAxis: [
      {
        type: 'value',
        name: '销售额'
      },
      {
        type: 'value',
        name: '订单数',
        minInterval: 1
      }
    ],
    series: [
      {
        name: '销售额',
        type: 'line',
        smooth: true,
        areaStyle: { opacity: 0.15 },
        itemStyle: { color: '#409eff' },
        data: trend.map(item => fromMinor(item.salesAmount || 0))
      },
      {
        name: '订单数',
        type: 'line',
        smooth: true,
        yAxisIndex: 1,
        itemStyle: { color: '#67c23a' },
        data: trend.map(item => num(item.orderCount))
      }
    ]
  }, true)
}

function renderStatusChart() {
  if (!statusChartRef.value) {
    return
  }
  if (!statusChart) {
    statusChart = echarts.init(statusChartRef.value)
  }
  const pieData = statusDistribution.value.map(item => ({
    name: proxy.selectDictLabel(commerce_order_status.value, item.status) || item.status,
    value: num(item.orderCount)
  }))
  statusChart.setOption({
    tooltip: {
      trigger: 'item',
      formatter: '{b}<br/>{c} 单（{d}%）'
    },
    legend: {
      bottom: 0,
      type: 'scroll'
    },
    graphic: pieData.length ? [] : [
      {
        type: 'text',
        left: 'center',
        top: 'middle',
        style: { text: '暂无数据', fill: '#909399', fontSize: 14 }
      }
    ],
    series: [
      {
        name: '订单状态',
        type: 'pie',
        radius: ['40%', '65%'],
        center: ['50%', '45%'],
        avoidLabelOverlap: true,
        label: { formatter: '{b}\n{c}' },
        data: pieData
      }
    ]
  }, true)
}

function resizeCharts() {
  if (salesChart) {
    salesChart.resize()
  }
  if (statusChart) {
    statusChart.resize()
  }
}

onMounted(() => {
  getReport()
  getStatusDistribution()
  window.addEventListener('resize', resizeCharts)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', resizeCharts)
  if (salesChart) {
    salesChart.dispose()
    salesChart = null
  }
  if (statusChart) {
    statusChart.dispose()
    statusChart = null
  }
})
</script>

<style scoped>
.metric-col {
  margin-bottom: 16px;
}
.metric-card {
  border-radius: 6px;
}
.metric-label {
  font-size: 13px;
  color: #909399;
  margin-bottom: 10px;
}
.metric-value {
  font-size: 24px;
  font-weight: 600;
}
.chart-card {
  margin-bottom: 16px;
}
.chart-box {
  height: 360px;
}
</style>
