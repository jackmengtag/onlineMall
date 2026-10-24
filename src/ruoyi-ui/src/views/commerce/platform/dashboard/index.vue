<template>
  <div class="app-container" v-loading="loading">
    <el-row :gutter="12" class="mb8">
      <el-col v-for="card in cards" :key="card.key" :xs="12" :sm="8" :md="4">
        <el-card shadow="hover" class="metric-card">
          <div class="metric-label">{{ card.label }}</div>
          <div class="metric-value">{{ card.value }}</div>
          <div class="metric-desc">{{ card.desc }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" class="mb8">
      <template #header>
        <span>近 7 日 GMV 趋势</span>
      </template>
      <div ref="trendRef" style="height: 340px" />
    </el-card>

    <el-card shadow="never">
      <template #header>
        <span>店铺排行</span>
      </template>
      <el-table :data="summary.shopRanking || []">
        <el-table-column label="店铺名" min-width="220">
          <template #default="scope">
            <span>{{ scope.row.shopName }}</span>
            <span class="shop-code">{{ scope.row.shopCode }}</span>
          </template>
        </el-table-column>
        <el-table-column label="GMV" align="right" width="180">
          <template #default="scope">{{ formatMoney(scope.row.gmv) }}</template>
        </el-table-column>
        <el-table-column label="订单数" align="center" prop="orderCount" width="120" />
        <el-table-column label="状态" align="center" width="120">
          <template #default="scope">
            <dict-tag :options="commerce_shop_status" :value="scope.row.status" />
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup name="PlatformDashboard">
import * as echarts from 'echarts'
import { getPlatformSummary } from '@/api/commerce/platform/dashboard'
import { formatMoney, toMinor } from '@/utils/commerce'

const { commerce_shop_status } = useDict('commerce_shop_status')

const loading = ref(false)
const summary = ref({})
const trendRef = ref(null)
let trendChart = null

const cards = computed(() => [
  { key: 'gmv', label: 'GMV', value: formatMoney(summary.value.gmv), desc: '累计成交额' },
  { key: 'orderCount', label: '订单数', value: formatCount(summary.value.orderCount), desc: '累计订单量' },
  { key: 'shopCount', label: '店铺数', value: formatCount(summary.value.shopCount), desc: '平台店铺总数' },
  { key: 'activeShopCount', label: '活跃店铺', value: formatCount(summary.value.activeShopCount), desc: '状态为正常' },
  { key: 'channelCount', label: '渠道数', value: formatCount(summary.value.channelCount), desc: '启用中的渠道' },
  { key: 'refundAmount', label: '退款额', value: summary.value.refundAmount == null ? '—' : formatMoney(summary.value.refundAmount), desc: '累计退款金额' }
])

/** 数量格式化 */
function formatCount(value) {
  if (value === null || value === undefined || value === '') {
    return '0'
  }
  return Number(value).toLocaleString('en-US')
}

/** 渲染趋势图 */
function renderTrend() {
  if (!trendRef.value) {
    return
  }
  const trend = summary.value.trend || []
  if (!trendChart) {
    trendChart = echarts.init(trendRef.value)
  }
  trendChart.setOption({
    tooltip: {
      trigger: 'axis',
      formatter: params => {
        const item = params[0]
        return `${item.axisValue}<br/>GMV：${formatMoney(toMinor(item.value))}`
      }
    },
    grid: { left: 16, right: 24, top: 40, bottom: 16, containLabel: true },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: trend.map(item => item.d),
      axisTick: { show: false }
    },
    yAxis: {
      type: 'value',
      axisLabel: { formatter: value => Number(value).toLocaleString('en-US') }
    },
    series: [
      {
        name: 'GMV',
        type: 'line',
        smooth: true,
        showSymbol: true,
        symbolSize: 6,
        data: trend.map(item => Number(item.amount || 0) / 100),
        lineStyle: { width: 3, color: '#409eff' },
        itemStyle: { color: '#409eff' },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(64, 158, 255, 0.35)' },
            { offset: 1, color: 'rgba(64, 158, 255, 0.02)' }
          ])
        }
      }
    ]
  })
  trendChart.resize()
}

/** 查询驾驶舱数据 */
function getSummary() {
  loading.value = true
  getPlatformSummary()
    .then(response => {
      summary.value = response.data || {}
      nextTick(() => renderTrend())
    })
    .finally(() => {
      loading.value = false
    })
}

function handleResize() {
  trendChart && trendChart.resize()
}

onMounted(() => {
  getSummary()
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  if (trendChart) {
    trendChart.dispose()
    trendChart = null
  }
})
</script>

<style scoped>
.metric-card {
  margin-bottom: 12px;
}
.metric-label {
  font-size: 13px;
  color: var(--el-text-color-secondary);
}
.metric-value {
  margin-top: 8px;
  font-size: 24px;
  font-weight: 600;
  color: var(--el-text-color-primary);
  word-break: break-all;
}
.metric-desc {
  margin-top: 6px;
  font-size: 12px;
  color: var(--el-text-color-placeholder);
}
.shop-code {
  margin-left: 10px;
  font-size: 12px;
  color: var(--el-text-color-secondary);
}
</style>
