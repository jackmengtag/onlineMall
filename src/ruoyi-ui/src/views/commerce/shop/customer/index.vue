<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
      <el-form-item label="关键字" prop="keyword">
        <el-input
          v-model="queryParams.keyword"
          placeholder="客户名称 / 邮箱 / 客户编号"
          clearable
          style="width: 260px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="pageRows">
      <el-table-column label="客户编号" align="center" prop="customerNo" width="150" />
      <el-table-column label="客户名称" align="left" prop="displayName" :show-overflow-tooltip="true" />
      <el-table-column label="邮箱" align="left" prop="email" :show-overflow-tooltip="true" />
      <el-table-column label="国家/地区" align="center" prop="countryCode" width="110" />
      <el-table-column label="订单数" align="center" prop="orderCount" width="100" />
      <el-table-column label="消费额" align="right" width="150">
        <template #default="scope">
          <span>{{ formatMoney(scope.row.totalAmount || 0) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="最近下单" align="center" prop="lastOrderTime" width="170">
        <template #default="scope">
          <span>{{ parseTime(scope.row.lastOrderTime) || '-' }}</span>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="pageNum"
      v-model:limit="pageSize"
    />
  </div>
</template>

<script setup name="ShopCustomer">
import { listShopCustomer } from '@/api/commerce/shop/customer'
import { formatMoney } from '@/utils/commerce'

const { proxy } = getCurrentInstance()

const loading = ref(false)
const showSearch = ref(true)
const allRows = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const queryParams = ref({ keyword: undefined })

const total = computed(() => allRows.value.length)
const pageRows = computed(() => {
  const start = (pageNum.value - 1) * pageSize.value
  return allRows.value.slice(start, start + pageSize.value)
})

function getList() {
  loading.value = true
  listShopCustomer({ keyword: queryParams.value.keyword }).then(response => {
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
  queryParams.value.keyword = undefined
  handleQuery()
}

getList()
</script>
