<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-date-picker v-model="date" type="date" value-format="YYYY-MM-DD" placeholder="请选择对账日期" :clearable="false" style="width: 180px" @change="getData" />
      </el-col>
      <el-col :span="1.5">
        <el-button type="primary" icon="Search" @click="getData">查询</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Download" :disabled="!details.length" @click="handleExport">导出 CSV</el-button>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="mb8">
      <el-col :xs="12" :md="6">
        <el-card shadow="never" class="summary-card">
          <div class="summary-label">系统金额</div>
          <div class="summary-value">{{ formatMoney(summary.systemAmount) }}</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :md="6">
        <el-card shadow="never" class="summary-card">
          <div class="summary-label">渠道金额</div>
          <div class="summary-value">{{ formatMoney(summary.channelAmount) }}</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :md="6">
        <el-card shadow="never" class="summary-card">
          <div class="summary-label">差异金额</div>
          <div class="summary-value" :class="summary.differenceAmount ? 'text-danger' : ''">{{ formatMoney(summary.differenceAmount) }}</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :md="6">
        <el-card shadow="never" class="summary-card">
          <div class="summary-label">差异率</div>
          <div class="summary-value" :class="differenceRate ? 'text-danger' : ''">{{ differenceRateText }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="details">
      <el-table-column label="支付单号" align="center" prop="paymentNo" min-width="170" show-overflow-tooltip />
      <el-table-column label="订单号" align="center" prop="orderNo" min-width="170" show-overflow-tooltip />
      <el-table-column label="系统金额" align="center" width="140">
        <template #default="scope">{{ formatMoney(scope.row.systemAmount, scope.row.currencyCode) }}</template>
      </el-table-column>
      <el-table-column label="渠道金额" align="center" width="140">
        <template #default="scope">{{ formatMoney(scope.row.channelAmount, scope.row.currencyCode) }}</template>
      </el-table-column>
      <el-table-column label="差异" align="center" width="140">
        <template #default="scope">
          <span :class="scope.row.difference ? 'text-danger' : ''">{{ formatMoney(scope.row.difference, scope.row.currencyCode) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" width="160">
        <template #default="scope">
          <el-tag :type="statusTagType(scope.row.status)">{{ statusLabel(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
    </el-table>
    <el-empty v-if="!loading && !details.length" description="当日暂无对账数据" :image-size="80" />
  </div>
</template>

<script setup name="CommerceAdminReconciliation">
import { saveAs } from 'file-saver'
import { getReconciliationSummary, listReconciliation } from '@/api/commerce/admin/reconciliation'
import { formatMoney } from '@/utils/commerce'

const loading = ref(false)
const date = ref(formatDate(new Date()))
const summary = ref({})
const details = ref([])

const statusMap = {
  MATCHED: { label: '一致', type: 'success' },
  AMOUNT_MISMATCH: { label: '金额不一致', type: 'danger' },
  ORDER_NOT_PAID: { label: '订单未支付', type: 'warning' },
  PAYMENT_NOT_SUCCEEDED: { label: '支付未成功', type: 'warning' },
  ORDER_MISSING: { label: '订单缺失', type: 'danger' },
  PENDING: { label: '待处理', type: 'info' }
}

const differenceRate = computed(() => {
  const systemAmount = Number(summary.value.systemAmount || 0)
  const difference = Number(summary.value.differenceAmount || 0)
  return systemAmount ? Math.abs((difference / systemAmount) * 100) : 0
})

const differenceRateText = computed(() => differenceRate.value.toFixed(2) + '%')

function formatDate(value) {
  const year = value.getFullYear()
  const month = String(value.getMonth() + 1).padStart(2, '0')
  const day = String(value.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

function statusLabel(status) {
  return statusMap[status]?.label || status || '-'
}

function statusTagType(status) {
  return statusMap[status]?.type || 'info'
}

function getData() {
  loading.value = true
  const query = { date: date.value }
  Promise.all([getReconciliationSummary(query), listReconciliation(query)])
    .then(([summaryResponse, listResponse]) => {
      summary.value = summaryResponse.data || {}
      details.value = listResponse.rows || []
    })
    .finally(() => {
      loading.value = false
    })
}

function csvCell(value) {
  const text = value === null || value === undefined ? '' : String(value)
  return '"' + text.replace(/"/g, '""') + '"'
}

function toDecimal(amount) {
  return (Number(amount || 0) / 100).toFixed(2)
}

function handleExport() {
  const headers = ['支付单号', '订单号', '系统金额', '渠道金额', '差异', '状态']
  const rows = details.value.map(item => [
    item.paymentNo,
    item.orderNo,
    toDecimal(item.systemAmount),
    toDecimal(item.channelAmount),
    toDecimal(item.difference),
    statusLabel(item.status)
  ])
  const csv = '\ufeff' + [headers, ...rows].map(row => row.map(csvCell).join(',')).join('\r\n')
  saveAs(new Blob([csv], { type: 'text/csv;charset=utf-8' }), `reconciliation_${date.value}.csv`)
}

getData()
</script>

<style scoped>
.summary-card {
  margin-bottom: 16px;
}

.summary-label {
  color: #909399;
  font-size: 13px;
}

.summary-value {
  margin-top: 8px;
  font-size: 22px;
  font-weight: 600;
  color: #303133;
}

.text-danger {
  color: #f56c6c;
}
</style>
