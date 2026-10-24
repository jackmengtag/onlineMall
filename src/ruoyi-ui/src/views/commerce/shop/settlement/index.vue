<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
      <el-form-item label="结算状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="结算状态" clearable style="width: 180px">
          <el-option
            v-for="dict in commerce_settlement_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleGenerate"
          v-hasPermi="['commerce:shop:settlement:generate']"
        >生成结算单</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="pageRows">
      <el-table-column label="结算单号" align="center" prop="settlementNo" width="190" />
      <el-table-column label="账期" align="center" width="220">
        <template #default="scope">
          <span>{{ parseTime(scope.row.periodStart, '{y}-{m}-{d}') }} ~ {{ parseTime(scope.row.periodEnd, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="订单数" align="center" prop="orderCount" width="100" />
      <el-table-column label="成交额" align="right" width="150">
        <template #default="scope">
          <span>{{ formatMoney(scope.row.grossAmount || 0) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="佣金" align="right" width="150">
        <template #default="scope">
          <span>{{ formatMoney(scope.row.commissionAmount || 0) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="应结金额" align="right" width="150">
        <template #default="scope">
          <span class="net-amount">{{ formatMoney(scope.row.netAmount || 0) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="100">
        <template #default="scope">
          <dict-tag :options="commerce_settlement_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="打款时间" align="center" prop="paidAt" width="170">
        <template #default="scope">
          <span>{{ parseTime(scope.row.paidAt) || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="120" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button
            v-if="scope.row.status === 'PENDING'"
            link
            type="success"
            icon="Money"
            @click="handlePay(scope.row)"
            v-hasPermi="['commerce:shop:settlement:pay']"
          >打款</el-button>
          <span v-else>-</span>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="pageNum"
      v-model:limit="pageSize"
    />

    <!-- 生成结算单 -->
    <el-dialog v-model="generateOpen" title="生成结算单" width="520px" append-to-body>
      <el-form ref="generateRef" :model="generateForm" :rules="generateRules" label-width="90px">
        <el-form-item label="账期" prop="period">
          <el-date-picker
            v-model="generateForm.period"
            type="daterange"
            value-format="YYYY-MM-DD"
            range-separator="~"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" :loading="generateLoading" @click="submitGenerate">确 定</el-button>
          <el-button @click="generateOpen = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="ShopSettlement">
import { listShopSettlement, generateShopSettlement, payShopSettlement } from '@/api/commerce/shop/settlement'
import { formatMoney } from '@/utils/commerce'

const { proxy } = getCurrentInstance()
const { commerce_settlement_status } = useDict('commerce_settlement_status')

const loading = ref(false)
const showSearch = ref(true)
const allRows = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const queryParams = ref({ status: undefined })

const generateOpen = ref(false)
const generateLoading = ref(false)
const generateForm = ref({ period: [] })
const generateRules = {
  period: [{ required: true, message: '请选择账期', trigger: 'change' }]
}

const total = computed(() => allRows.value.length)
const pageRows = computed(() => {
  const start = (pageNum.value - 1) * pageSize.value
  return allRows.value.slice(start, start + pageSize.value)
})

function getList() {
  loading.value = true
  listShopSettlement({ status: queryParams.value.status }).then(response => {
    allRows.value = response.rows || []
    if ((pageNum.value - 1) * pageSize.value >= allRows.value.length) {
      pageNum.value = 1
    }
  }).finally(() => {
    loading.value = false
  })
}

function handleQuery() {
  pageNum.value = 1
  getList()
}

function resetQuery() {
  proxy.resetForm('queryRef')
  queryParams.value.status = undefined
  handleQuery()
}

function handleGenerate() {
  generateForm.value = { period: [] }
  generateOpen.value = true
  nextTick(() => proxy.$refs['generateRef'] && proxy.$refs['generateRef'].clearValidate())
}

function submitGenerate() {
  proxy.$refs['generateRef'].validate(valid => {
    if (!valid) {
      return
    }
    const period = generateForm.value.period || []
    generateLoading.value = true
    generateShopSettlement(period[0], period[1]).then(() => {
      proxy.$modal.msgSuccess('结算单生成成功')
      generateOpen.value = false
      getList()
    }).finally(() => {
      generateLoading.value = false
    })
  })
}

function handlePay(row) {
  proxy.$modal.confirm(`是否确认将结算单"${row.settlementNo}"标记为已打款？`).then(() => {
    return payShopSettlement(row.settlementId)
  }).then(() => {
    proxy.$modal.msgSuccess('打款成功')
    getList()
  }).catch(() => {})
}

getList()
</script>

<style scoped>
.net-amount {
  font-weight: 600;
  color: #67c23a;
}
</style>
