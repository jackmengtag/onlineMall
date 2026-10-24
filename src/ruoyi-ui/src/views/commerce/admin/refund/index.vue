<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
      <el-form-item label="退款单号" prop="refundNo">
        <el-input v-model="queryParams.refundNo" placeholder="请输入退款单号" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="订单 ID" prop="orderId">
        <el-input-number v-model="queryParams.orderId" :min="1" :controls="false" placeholder="请输入订单 ID" style="width: 180px" />
      </el-form-item>
      <el-form-item label="退款状态" prop="refundStatus">
        <el-select v-model="queryParams.refundStatus" placeholder="请选择退款状态" clearable style="width: 200px">
          <el-option v-for="dict in commerce_refund_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="refundList">
      <el-table-column label="退款单号" align="center" prop="refundNo" min-width="160" show-overflow-tooltip />
      <el-table-column label="订单号" align="center" min-width="150" show-overflow-tooltip>
        <template #default="scope">{{ scope.row.orderNo || scope.row.orderId }}</template>
      </el-table-column>
      <el-table-column label="退款金额" align="center" width="130">
        <template #default="scope">{{ formatMoney(scope.row.amount, scope.row.currencyCode) }}</template>
      </el-table-column>
      <el-table-column label="币种" align="center" prop="currencyCode" width="80" />
      <el-table-column label="状态" align="center" prop="refundStatus" width="100">
        <template #default="scope">
          <dict-tag :options="commerce_refund_status" :value="scope.row.refundStatus" />
        </template>
      </el-table-column>
      <el-table-column label="退款原因" align="center" prop="reason" min-width="160" show-overflow-tooltip />
      <el-table-column label="申请时间" align="center" prop="requestedAt" width="160">
        <template #default="scope">{{ parseTime(scope.row.requestedAt) }}</template>
      </el-table-column>
      <el-table-column label="完成时间" align="center" prop="completedAt" width="160">
        <template #default="scope">{{ parseTime(scope.row.completedAt) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="270" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="View" @click="handleView(scope.row)" v-hasPermi="['commerce:admin:refund:query']">详情</el-button>
          <el-button
            v-if="canApprove(scope.row)"
            link
            type="success"
            icon="CircleCheck"
            @click="handleApprove(scope.row)"
            v-hasPermi="['commerce:admin:refund:approve', 'commerce:admin:refund:edit']"
          >审核</el-button>
          <el-button
            v-if="canReject(scope.row)"
            link
            type="warning"
            icon="Close"
            @click="handleReject(scope.row)"
            v-hasPermi="['commerce:admin:refund:edit']"
          >驳回</el-button>
          <el-button
            v-if="canComplete(scope.row)"
            link
            type="primary"
            icon="Finished"
            @click="handleComplete(scope.row)"
            v-hasPermi="['commerce:admin:refund:edit']"
          >完成</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog title="退款单详情" v-model="detailOpen" width="720px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="退款单号">{{ detail.refundNo }}</el-descriptions-item>
        <el-descriptions-item label="订单号">{{ detail.orderNo || detail.orderId }}</el-descriptions-item>
        <el-descriptions-item label="退款金额">{{ formatMoney(detail.amount, detail.currencyCode) }}</el-descriptions-item>
        <el-descriptions-item label="币种">{{ detail.currencyCode }}</el-descriptions-item>
        <el-descriptions-item label="退款状态">
          <dict-tag :options="commerce_refund_status" :value="detail.refundStatus" />
        </el-descriptions-item>
        <el-descriptions-item label="支付单 ID">{{ detail.paymentId }}</el-descriptions-item>
        <el-descriptions-item label="渠道退款 ID">{{ detail.providerRefundId || '-' }}</el-descriptions-item>
        <el-descriptions-item label="申请时间">{{ parseTime(detail.requestedAt) }}</el-descriptions-item>
        <el-descriptions-item label="完成时间">{{ parseTime(detail.completedAt) }}</el-descriptions-item>
        <el-descriptions-item label="退款原因" :span="2">{{ detail.reason || '-' }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="detailOpen = false">关 闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="CommerceAdminRefund">
import { ElMessageBox } from 'element-plus'
import { listRefund, getRefund, approveRefund, rejectRefund, completeRefund } from '@/api/commerce/admin/refund'
import { formatMoney } from '@/utils/commerce'

const { proxy } = getCurrentInstance()
const { commerce_refund_status } = useDict('commerce_refund_status')

const refundList = ref([])
const loading = ref(true)
const showSearch = ref(true)
const total = ref(0)
const detailOpen = ref(false)
const detail = ref({})

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    refundNo: undefined,
    orderId: undefined,
    refundStatus: undefined
  }
})

const { queryParams } = toRefs(data)

function getList() {
  loading.value = true
  listRefund(queryParams.value).then(response => {
    refundList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

function resetQuery() {
  proxy.resetForm('queryRef')
  handleQuery()
}

function canApprove(row) {
  return row.refundStatus === 'REQUESTED' || row.refundStatus === 'PROCESSING'
}

function canReject(row) {
  return row.refundStatus !== 'SUCCEEDED' && row.refundStatus !== 'REJECTED'
}

function canComplete(row) {
  return row.refundStatus === 'APPROVED' || row.refundStatus === 'PROCESSING'
}

function handleView(row) {
  getRefund(row.refundId).then(response => {
    detail.value = response.data || {}
    detailOpen.value = true
  })
}

function handleApprove(row) {
  proxy.$modal.confirm('是否审核通过退款单"' + row.refundNo + '"？').then(() => {
    return approveRefund(row.refundId)
  }).then(() => {
    proxy.$modal.msgSuccess('审核通过')
    getList()
  }).catch(() => {})
}

function handleReject(row) {
  ElMessageBox.prompt('请输入驳回原因', '驳回退款', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputType: 'textarea',
    inputValidator: value => (value ? true : '驳回原因不能为空')
  }).then(({ value }) => {
    return rejectRefund(row.refundId, value)
  }).then(() => {
    proxy.$modal.msgSuccess('已驳回')
    getList()
  }).catch(() => {})
}

function handleComplete(row) {
  proxy.$modal.confirm('完成退款后将按订单明细回补库存，是否继续？').then(() => {
    return completeRefund(row.refundId)
  }).then(() => {
    proxy.$modal.msgSuccess('退款已完成')
    getList()
  }).catch(() => {})
}

getList()
</script>
