<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <span class="page-title">运营工作台</span>
      </el-col>
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Refresh" :loading="loading" @click="getSummary">刷新</el-button>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="mb8">
      <el-col v-for="card in metrics" :key="card.key" :xs="12" :sm="8" :md="4">
        <el-card shadow="never" class="metric-card">
          <div class="metric-label">{{ card.label }}</div>
          <div class="metric-value">{{ card.value }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16">
      <el-col :xs="24" :md="16">
        <el-card shadow="never" header="近 7 天销售趋势">
          <div ref="trendRef" v-loading="loading" class="trend-chart"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :md="8">
        <el-card shadow="never" header="订单状态分布">
          <div v-if="statusList.length" v-loading="loading">
            <div v-for="item in statusList" :key="item.value" class="status-row">
              <span class="status-name">{{ item.label }}</span>
              <el-progress class="status-progress" :percentage="item.percentage" :stroke-width="8" :show-text="false" />
              <span class="status-count">{{ item.count }}</span>
            </div>
          </div>
          <el-empty v-else description="暂无数据" :image-size="60" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="CommerceAdminDashboard">
import * as echarts from 'echarts'
import { getAdminSummary } from '@/api/commerce/admin/dashboard'
import { formatMoney } from '@/utils/commerce'

const { commerce_order_status } = useDict('commerce_order_status')

const loading = ref(false)
const summary = ref({})
const trendRef = ref(null)
let chart = null

const metrics = computed(() => [
  { key: 'todaySalesAmount', label: '今日销售额', value: formatMoney(summary.value.todaySalesAmount || 0) },
  { key: 'todayOrderCount', label: '今日订单数', value: summary.value.todayOrderCount || 0 },
  { key: 'todayPaidCount', label: '今日支付数', value: summary.value.todayPaidCount || 0 },
  { key: 'pendingFulfillmentCount', label: '待履约订单', value: summary.value.pendingFulfillmentCount || 0 },
  { key: 'refundAmount', label: '退款金额', value: formatMoney(summary.value.refundAmount || 0) }
])

const statusList = computed(() => {
  const distribution = summary.value.statusDistribution
  let counts = []
  if (Array.isArray(distribution)) {
    counts = distribution.map(item => ({
      value: item.status,
      label: dictLabel(item.status),
      count: Number(item.orderCount || 0)
    }))
  } else if (distribution && typeof distribution === 'object') {
    counts = Object.keys(distribution).map(key => ({
      value: key,
      label: dictLabel(key),
      count: Number(distribution[key] || 0)
    }))
  }
  const total = counts.reduce((sum, item) => sum + item.count, 0)
  return counts
    .map(item => ({ ...item, percentage: total ? Math.round((item.count / total) * 100) : 0 }))
    .sort((a, b) => b.count - a.count)
})

function dictLabel(value) {
  const item = commerce_order_status.value.find(dict => dict.value === value)
  return item ? item.label : value
}

function renderChart() {
  const trend = summary.value.salesTrend || []
  if (!trendRef.value) {
    return
  }
  if (!chart) {
    chart = echarts.init(trendRef.value)
  }
  chart.setOption(
    {
      tooltip: { trigger: 'axis' },
      legend: { data: ['销售额', '订单数'], right: 10 },
      grid: { left: 50, right: 40, top: 45, bottom: 30 },
      xAxis: {
        type: 'category',
        boundaryGap: false,
        data: trend.map(item => item.statDate)
      },
      yAxis: [
        { type: 'value', name: '销售额' },
        { type: 'value', name: '订单数', minInterval: 1 }
      ],
      series: [
        {
          name: '销售额',
          type: 'line',
          smooth: true,
          areaStyle: {},
          data: trend.map(item => Number(item.salesAmount || 0) / 100)
        },
        {
          name: '订单数',
          type: 'line',
          smooth: true,
          yAxisIndex: 1,
          data: trend.map(item => Number(item.orderCount || 0))
        }
      ]
    },
    true
  )
}

function getSummary() {
  loading.value = true
  getAdminSummary({})
    .then(response => {
      summary.value = response.data || {}
      nextTick(() => renderChart())
    })
    .finally(() => {
      loading.value = false
    })
}

function resizeChart() {
  if (chart) {
    chart.resize()
  }
}

onMounted(() => {
  getSummary()
  window.addEventListener('resize', resizeChart)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', resizeChart)
  if (chart) {
    chart.dispose()
    chart = null
  }
})
</script>

<style scoped>
.page-title {
  font-size: 16px;
  font-weight: 600;
  line-height: 32px;
}

.metric-card {
  margin-bottom: 16px;
}

.metric-label {
  color: #909399;
  font-size: 13px;
}

.metric-value {
  margin-top: 8px;
  font-size: 22px;
  font-weight: 600;
  color: #303133;
  word-break: break-all;
}

.trend-chart {
  height: 360px;
}

.status-row {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 14px;
}

.status-name {
  width: 72px;
  flex: none;
  color: #606266;
}

.status-progress {
  flex: 1;
}

.status-count {
  width: 42px;
  flex: none;
  text-align: right;
  color: #303133;
}
</style>
