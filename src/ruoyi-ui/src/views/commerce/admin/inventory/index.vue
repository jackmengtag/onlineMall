<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
      <el-form-item label="SKU" prop="skuId">
        <el-select
          v-model="queryParams.skuId"
          filterable
          remote
          clearable
          :remote-method="searchSkus"
          :loading="skuLoading"
          placeholder="输入 SKU 编码搜索"
          style="width: 220px"
          @focus="searchSkus('')"
        >
          <el-option
            v-for="sku in skuOptions"
            :key="sku.skuId"
            :label="sku.skuCode + ' ' + (sku.spuName || '')"
            :value="sku.skuId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="仓库" prop="warehouseCode">
        <el-input v-model="queryParams.warehouseCode" placeholder="请输入仓库编码" clearable style="width: 180px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="国家" prop="countryCode">
        <el-input v-model="queryParams.countryCode" placeholder="请输入国家编码" clearable style="width: 160px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="店铺 ID" prop="shopId">
        <el-input-number v-model="queryParams.shopId" :min="1" :controls="false" placeholder="请输入店铺 ID" style="width: 160px" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="inventoryList">
      <el-table-column label="SKU 编码" align="center" prop="skuCode" min-width="150" show-overflow-tooltip />
      <el-table-column label="商品名称" align="center" prop="spuName" min-width="160" show-overflow-tooltip />
      <el-table-column label="仓库" align="center" prop="warehouseCode" width="120" />
      <el-table-column label="国家" align="center" prop="countryCode" width="90" />
      <el-table-column label="店铺 ID" align="center" prop="shopId" width="100" />
      <el-table-column label="总库存" align="center" prop="totalQty" width="100" />
      <el-table-column label="锁定" align="center" prop="lockedQty" width="100" />
      <el-table-column label="可售" align="center" prop="availableQty" width="100" />
      <el-table-column label="安全库存" align="center" prop="safetyQty" width="110" />
      <el-table-column label="更新时间" align="center" prop="updateTime" width="160">
        <template #default="scope">{{ parseTime(scope.row.updateTime) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="170" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="openAdjust(scope.row)" v-hasPermi="['commerce:admin:inventory:adjust']">调整</el-button>
          <el-button link type="primary" icon="List" @click="openTx(scope.row)">流水</el-button>
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

    <!-- 库存调整 -->
    <el-dialog title="库存调整" v-model="adjustOpen" width="500px" append-to-body>
      <el-form ref="adjustRef" :model="adjustForm" :rules="adjustRules" label-width="100px">
        <el-form-item label="SKU ID" prop="skuId">
          <el-input-number v-model="adjustForm.skuId" :min="1" :controls="false" placeholder="请输入 SKU ID" style="width: 100%" />
        </el-form-item>
        <el-form-item label="仓库" prop="warehouseCode">
          <el-input v-model="adjustForm.warehouseCode" placeholder="请输入仓库编码" />
        </el-form-item>
        <el-form-item label="调整数量" prop="delta">
          <el-input-number v-model="adjustForm.delta" :precision="0" controls-position="right" style="width: 100%" />
          <div class="form-tip">正数增加库存，负数扣减库存</div>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="adjustForm.remark" type="textarea" :rows="2" placeholder="请输入调整原因" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitAdjust">确 定</el-button>
          <el-button @click="adjustOpen = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 库存流水 -->
    <el-dialog title="库存变动流水" v-model="txOpen" width="900px" append-to-body>
      <el-form :model="txParams" :inline="true" class="mb8">
        <el-form-item label="流水类型">
          <el-select v-model="txParams.txType" placeholder="请选择流水类型" clearable style="width: 160px" @change="getTxList">
            <el-option v-for="dict in commerce_inventory_tx_type" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="getTxList">查询</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="txLoading" :data="txList" size="small" border>
        <el-table-column label="SKU 编码" align="center" prop="skuCode" min-width="140" />
        <el-table-column label="仓库" align="center" prop="warehouseCode" width="100" />
        <el-table-column label="类型" align="center" width="90">
          <template #default="scope">
            <dict-tag :options="commerce_inventory_tx_type" :value="scope.row.txType" />
          </template>
        </el-table-column>
        <el-table-column label="变动数量" align="center" prop="quantity" width="100" />
        <el-table-column label="变动前" align="center" prop="beforeQty" width="90" />
        <el-table-column label="变动后" align="center" prop="afterQty" width="90" />
        <el-table-column label="业务类型" align="center" prop="bizType" width="110" />
        <el-table-column label="业务单号" align="center" prop="bizNo" min-width="150" show-overflow-tooltip />
        <el-table-column label="操作人" align="center" prop="operatorId" width="110" />
        <el-table-column label="时间" align="center" width="160">
          <template #default="scope">{{ parseTime(scope.row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="备注" align="center" prop="remark" min-width="120" show-overflow-tooltip />
      </el-table>
      <pagination
        v-show="txTotal > 0"
        :total="txTotal"
        v-model:page="txParams.pageNum"
        v-model:limit="txParams.pageSize"
        @pagination="getTxList"
      />
      <template #footer>
        <el-button @click="txOpen = false">关 闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="CommerceAdminInventory">
import { listInventory, listInventoryTx, adjustInventory } from '@/api/commerce/admin/inventory'
import { listSkuOptions } from '@/api/commerce/admin/product'

const { proxy } = getCurrentInstance()
const { commerce_inventory_tx_type } = useDict('commerce_inventory_tx_type')

const inventoryList = ref([])
const loading = ref(true)
const showSearch = ref(true)
const total = ref(0)
const skuOptions = ref([])
const skuLoading = ref(false)
const adjustOpen = ref(false)
const txOpen = ref(false)
const txLoading = ref(false)
const txList = ref([])
const txTotal = ref(0)

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    skuId: undefined,
    warehouseCode: undefined,
    countryCode: undefined,
    shopId: undefined
  },
  adjustForm: {},
  adjustRules: {
    skuId: [{ required: true, message: 'SKU 不能为空', trigger: 'change' }],
    warehouseCode: [{ required: true, message: '仓库不能为空', trigger: 'blur' }],
    delta: [{ required: true, message: '调整数量不能为空', trigger: 'blur' }]
  },
  txParams: {
    pageNum: 1,
    pageSize: 10,
    skuId: undefined,
    warehouseCode: undefined,
    txType: undefined
  }
})

const { queryParams, adjustForm, adjustRules, txParams } = toRefs(data)

function getList() {
  loading.value = true
  listInventory(queryParams.value).then(response => {
    inventoryList.value = response.rows
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

function searchSkus(keyword) {
  skuLoading.value = true
  listSkuOptions({ keyword: keyword || undefined })
    .then(response => {
      skuOptions.value = response.data || []
    })
    .finally(() => {
      skuLoading.value = false
    })
}

function openAdjust(row) {
  adjustForm.value = {
    skuId: row.skuId,
    warehouseCode: row.warehouseCode,
    delta: undefined,
    remark: undefined
  }
  adjustOpen.value = true
  nextTick(() => proxy.$refs.adjustRef?.clearValidate())
}

function submitAdjust() {
  proxy.$refs['adjustRef'].validate(valid => {
    if (!valid) {
      return
    }
    adjustInventory({
      skuId: adjustForm.value.skuId,
      warehouseCode: adjustForm.value.warehouseCode,
      delta: adjustForm.value.delta,
      remark: adjustForm.value.remark
    }).then(() => {
      proxy.$modal.msgSuccess('调整成功')
      adjustOpen.value = false
      getList()
    })
  })
}

function openTx(row) {
  txParams.value = {
    pageNum: 1,
    pageSize: 10,
    skuId: row.skuId,
    warehouseCode: row.warehouseCode,
    txType: undefined
  }
  txList.value = []
  txTotal.value = 0
  txOpen.value = true
  getTxList()
}

function getTxList() {
  txLoading.value = true
  listInventoryTx(txParams.value).then(response => {
    txList.value = response.rows
    txTotal.value = response.total
    txLoading.value = false
  })
}

getList()
</script>

<style scoped>
.form-tip {
  color: #909399;
  font-size: 12px;
  line-height: 20px;
}
</style>
