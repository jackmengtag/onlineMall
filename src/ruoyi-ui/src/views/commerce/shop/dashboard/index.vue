<template>
  <div class="app-container">
    <el-row :gutter="16" v-loading="loading">
      <el-col v-for="item in metrics" :key="item.label" :xs="12" :sm="8" :md="6" class="metric-col">
        <el-card shadow="hover" class="metric-card">
          <div class="metric-inner">
            <div class="metric-icon" :style="{ backgroundColor: item.color }">
              <el-icon><component :is="item.icon" /></el-icon>
            </div>
            <div class="metric-content">
              <div class="metric-label">{{ item.label }}</div>
              <div class="metric-value">{{ item.value }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16">
      <el-col :xs="24" :lg="16">
        <el-card shadow="never" class="chart-card">
          <template #header>
            <div class="card-header">
              <span>近 7 日销售趋势</span>
              <el-button link type="primary" icon="Refresh" @click="getSummary">刷新</el-button>
            </div>
          </template>
          <div ref="trendChartRef" class="chart-box"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="8">
        <el-card shadow="never" class="chart-card">
          <template #header>
            <div class="card-header">
              <span>待处理事项</span>
            </div>
          </template>
          <div v-if="pendingItems.length" class="pending-list">
            <div v-for="item in pendingItems" :key="item.label" class="pending-item">
              <div class="pending-text">
                <el-tag :type="item.type" effect="plain">{{ item.label }}</el-tag>
                <span class="pending-desc">{{ item.desc }}</span>
              </div>
              <div class="pending-right">
                <span class="pending-count">{{ item.count }}</span>
                <el-button v-if="item.path" link type="primary" @click="handleJump(item.path)">去处理</el-button>
              </div>
            </div>
          </div>
          <el-empty v-else description="暂无待处理事项" :image-size="80" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="ShopDashboard">
import * as echarts from 'echarts'
import { getShopSummary } from '@/api/commerce/shop/dashboard'
import { formatMoney, fromMinor } from '@/utils/commerce'

const { proxy } = getCurrentInstance()

const loading = ref(false)
const summary = ref({})
const trendChartRef = ref(null)
let trendChart = null

const num = (value) => {
  const result = Number(value)
  return isNaN(result) ? 0 : result
}

const trend = computed(() => Array.isArray(summary.value.trend) ? summary.value.trend : [])
const todayPoint = computed(() => trend.value.length ? trend.value[trend.value.length - 1] : {})

const metrics = computed(() => [
  { label: '今日销售额', value: formatMoney(summary.value.todaySalesAmount ?? todayPoint.value.amount ?? 0), icon: 'Money', color: '#409eff' },
  { label: '累计销售额', value: formatMoney(summary.value.salesAmount || 0), icon: 'Wallet', color: '#67c23a' },
  { label: '今日订单数', value: num(summary.value.todayOrderCount ?? todayPoint.value.orders), icon: 'Document', color: '#e6a23c' },
  { label: '累计订单数', value: num(summary.value.orderCount), icon: 'Tickets', color: '#909399' },
  { label: '待发货', value: num(summary.value.pendingShipmentCount), icon: 'Van', color: '#f56c6c' },
  { label: '退款额', value: formatMoney(summary.value.refundAmount || 0), icon: 'RefreshLeft', color: '#b37feb' },
  { label: '商品数', value: num(summary.value.productCount), icon: 'Goods', color: '#36cfc9' },
  { label: '客户数', value: num(summary.value.customerCount ?? summary.value.customerTotal), icon: 'User', color: '#597ef7' }
])

const pendingItems = computed(() => {
  const items = []
  const pendingShipment = num(summary.value.pendingShipmentCount)
  if (pendingShipment > 0) {
    items.push({ label: '待发货订单', count: pendingShipment, type: 'warning', desc: '请尽快安排发货', path: '/shop/orders' })
  }
  const pendingRefund = num(summary.value.refundCount)
  if (pendingRefund > 0) {
    items.push({ label: '待审核退款', count: pendingRefund, type: 'danger', desc: '退款申请待处理' })
  }
  return items
})

function getSummary() {
  loading.value = true
  getShopSummary().then(response => {
    summary.value = response.data || {}
    nextTick(() => renderTrendChart())
  }).finally(() => {
    loading.value = false
  })
}

function renderTrendChart() {
  if (!trendChartRef.value) {
    return
  }
  if (!trendChart) {
    trendChart = echarts.init(trendChartRef.value)
  }
  const days = trend.value.map(item => item.d)
  const amounts = trend.value.map(item => fromMinor(item.amount || 0))
  const orders = trend.value.map(item => num(item.orders))
  trendChart.setOption({
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
      data: days,
      boundaryGap: false
    },
    yAxis: [
      {
        type: 'value',
        name: '销售额',
        axisLabel: { formatter: '{value}' }
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
        data: amounts,
        areaStyle: { opacity: 0.15 },
        itemStyle: { color: '#409eff' }
      },
      {
        name: '订单数',
        type: 'line',
        smooth: true,
        yAxisIndex: 1,
        data: orders,
        itemStyle: { color: '#67c23a' }
      }
    ]
  }, true)
}

function handleJump(path) {
  proxy.$router.push(path)
}

function resizeChart() {
  if (trendChart) {
    trendChart.resize()
  }
}

onMounted(() => {
  getSummary()
  window.addEventListener('resize', resizeChart)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', resizeChart)
  if (trendChart) {
    trendChart.dispose()
    trendChart = null
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
.metric-inner {
  display: flex;
  align-items: center;
}
.metric-icon {
  width: 46px;
  height: 46px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  color: #ffffff;
  font-size: 22px;
}
.metric-content {
  flex: 1;
  min-width: 0;
}
.metric-label {
  font-size: 13px;
  color: #909399;
  margin-bottom: 6px;
}
.metric-value {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  word-break: break-all;
}
.chart-card {
  margin-bottom: 16px;
}
.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.chart-box {
  height: 360px;
}
.pending-list {
  padding: 4px 0;
}
.pending-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px 4px;
  border-bottom: 1px solid #ebeef5;
}
.pending-item:last-child {
  border-bottom: none;
}
.pending-text {
  display: flex;
  align-items: center;
}
.pending-desc {
  margin-left: 10px;
  color: #909399;
  font-size: 13px;
}
.pending-right {
  display: flex;
  align-items: center;
}
.pending-count {
  font-size: 22px;
  font-weight: 600;
  color: #f56c6c;
  margin-right: 8px;
}
</style>
