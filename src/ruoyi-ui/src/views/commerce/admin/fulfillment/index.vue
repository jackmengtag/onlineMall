<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
      <el-form-item label="履约单号" prop="fulfillmentNo">
        <el-input v-model="queryParams.fulfillmentNo" placeholder="请输入履约单号" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="订单 ID" prop="orderId">
        <el-input-number v-model="queryParams.orderId" :min="1" :controls="false" placeholder="请输入订单 ID" style="width: 170px" />
      </el-form-item>
      <el-form-item label="仓库" prop="warehouseCode">
        <el-input v-model="queryParams.warehouseCode" placeholder="请输入仓库编码" clearable style="width: 180px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="履约状态" prop="fulfillmentStatus">
        <el-select v-model="queryParams.fulfillmentStatus" placeholder="请选择履约状态" clearable style="width: 180px">
          <el-option v-for="dict in commerce_fulfillment_order_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="openSplit" v-hasPermi="['commerce:admin:fulfillment:split']">按订单拆分</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="fulfillmentList">
      <el-table-column label="履约单号" align="center" prop="fulfillmentNo" min-width="170" show-overflow-tooltip />
      <el-table-column label="订单 ID" align="center" prop="orderId" width="100" />
      <el-table-column label="店铺 ID" align="center" prop="shopId" width="100" />
      <el-table-column label="仓库" align="center" prop="warehouseCode" width="120" />
      <el-table-column label="履约状态" align="center" width="110">
        <template #default="scope">
          <dict-tag :options="commerce_fulfillment_order_status" :value="scope.row.fulfillmentStatus" />
        </template>
      </el-table-column>
      <el-table-column label="分配原因" align="center" prop="allocationReason" min-width="140" show-overflow-tooltip />
      <el-table-column label="预计发货时间" align="center" prop="estimatedShipAt" width="160">
        <template #default="scope">{{ parseTime(scope.row.estimatedShipAt) }}</template>
      </el-table-column>
      <el-table-column label="实际发货时间" align="center" prop="actualShipAt" width="160">
        <template #default="scope">{{ parseTime(scope.row.actualShipAt) }}</template>
      </el-table-column>
      <el-table-column label="完成时间" align="center" prop="completedAt" width="160">
        <template #default="scope">{{ parseTime(scope.row.completedAt) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="260" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="View" @click="handleView(scope.row)" v-hasPermi="['commerce:admin:fulfillment:query']">详情</el-button>
          <el-button
            v-if="scope.row.fulfillmentStatus === 'CREATED'"
            link
            type="warning"
            icon="Share"
            @click="handleStatus(scope.row, 'ALLOCATED')"
            v-hasPermi="['commerce:admin:fulfillment:edit']"
          >分配</el-button>
          <el-button
            v-if="scope.row.fulfillmentStatus === 'ALLOCATED'"
            link
            type="warning"
            icon="Box"
            @click="handleStatus(scope.row, 'PICKING')"
            v-hasPermi="['commerce:admin:fulfillment:edit']"
          >拣货</el-button>
          <el-button
            v-if="scope.row.fulfillmentStatus === 'PICKING'"
            link
            type="warning"
            icon="TakeawayBox"
            @click="handleStatus(scope.row, 'PACKED')"
            v-hasPermi="['commerce:admin:fulfillment:edit']"
          >打包</el-button>
          <el-button
            v-if="scope.row.fulfillmentStatus === 'PACKED'"
            link
            type="success"
            icon="Van"
            @click="openShip(scope.row)"
            v-hasPermi="['commerce:admin:fulfillment:ship']"
          >发货</el-button>
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

    <!-- 按订单拆分 -->
    <el-dialog title="按订单拆分履约单" v-model="splitOpen" width="460px" append-to-body>
      <el-form ref="splitRef" :model="splitForm" :rules="splitRules" label-width="100px">
        <el-form-item label="订单 ID" prop="orderId">
          <el-input-number v-model="splitForm.orderId" :min="1" :controls="false" placeholder="请输入订单 ID" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitSplit">确 定</el-button>
          <el-button @click="splitOpen = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 发货 -->
    <el-dialog title="履约单发货" v-model="shipOpen" width="460px" append-to-body>
      <el-form ref="shipRef" :model="shipForm" :rules="shipRules" label-width="100px">
        <el-form-item label="履约单号">
          <el-input v-model="shipForm.fulfillmentNo" disabled />
        </el-form-item>
        <el-form-item label="承运商" prop="carrierCode">
          <el-input v-model="shipForm.carrierCode" placeholder="请输入承运商编码" />
        </el-form-item>
        <el-form-item label="物流单号" prop="trackingNo">
          <el-input v-model="shipForm.trackingNo" placeholder="请输入物流单号" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitShip">确 定</el-button>
          <el-button @click="shipOpen = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 履约详情 -->
    <el-dialog title="履约单详情" v-model="detailOpen" width="860px" append-to-body>
      <el-descriptions title="履约信息" :column="3" border>
        <el-descriptions-item label="履约单号">{{ fulfillmentInfo.fulfillmentNo }}</el-descriptions-item>
        <el-descriptions-item label="订单 ID">{{ fulfillmentInfo.orderId }}</el-descriptions-item>
        <el-descriptions-item label="店铺 ID">{{ fulfillmentInfo.shopId }}</el-descriptions-item>
        <el-descriptions-item label="仓库">{{ fulfillmentInfo.warehouseCode }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <dict-tag :options="commerce_fulfillment_order_status" :value="fulfillmentInfo.fulfillmentStatus" />
        </el-descriptions-item>
        <el-descriptions-item label="分配原因">{{ fulfillmentInfo.allocationReason || '-' }}</el-descriptions-item>
        <el-descriptions-item label="预计发货时间">{{ parseTime(fulfillmentInfo.estimatedShipAt) }}</el-descriptions-item>
        <el-descriptions-item label="实际发货时间">{{ parseTime(fulfillmentInfo.actualShipAt) }}</el-descriptions-item>
        <el-descriptions-item label="完成时间">{{ parseTime(fulfillmentInfo.completedAt) }}</el-descriptions-item>
      </el-descriptions>

      <el-divider content-position="left">履约明细</el-divider>
      <el-table :data="detailItems" size="small" border>
        <el-table-column label="SKU 编码" align="center" prop="skuCode" min-width="140" />
        <el-table-column label="商品名称" align="center" prop="productName" min-width="160" show-overflow-tooltip />
        <el-table-column label="订单明细 ID" align="center" prop="orderItemId" width="120" />
        <el-table-column label="数量" align="center" prop="quantity" width="80" />
      </el-table>

      <el-divider content-position="left">运单信息</el-divider>
      <el-table :data="detailShipments" size="small" border>
        <el-table-column label="运单号" align="center" prop="shipmentNo" min-width="160" />
        <el-table-column label="承运商" align="center" prop="carrierCode" width="110" />
        <el-table-column label="物流单号" align="center" prop="trackingNo" min-width="150" />
        <el-table-column label="状态" align="center" width="100">
          <template #default="scope">
            <dict-tag :options="commerce_shipment_status" :value="scope.row.shipmentStatus" />
          </template>
        </el-table-column>
        <el-table-column label="发货时间" align="center" width="160">
          <template #default="scope">{{ parseTime(scope.row.shippedAt) }}</template>
        </el-table-column>
        <el-table-column label="签收时间" align="center" width="160">
          <template #default="scope">{{ parseTime(scope.row.deliveredAt) }}</template>
        </el-table-column>
      </el-table>

      <template #footer>
        <el-button @click="detailOpen = false">关 闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="CommerceAdminFulfillment">
import {
  listFulfillment,
  getFulfillment,
  splitFulfillment,
  changeFulfillmentStatus,
  shipFulfillment
} from '@/api/commerce/admin/fulfillment'

const { proxy } = getCurrentInstance()
const { commerce_fulfillment_order_status, commerce_shipment_status } = useDict(
  'commerce_fulfillment_order_status',
  'commerce_shipment_status'
)

const fulfillmentList = ref([])
const loading = ref(true)
const showSearch = ref(true)
const total = ref(0)
const splitOpen = ref(false)
const shipOpen = ref(false)
const detailOpen = ref(false)
const detail = ref({})

const fulfillmentInfo = computed(() => detail.value.fulfillment || {})
const detailItems = computed(() => detail.value.items || [])
const detailShipments = computed(() => detail.value.shipments || [])

const statusActions = {
  ALLOCATED: '分配',
  PICKING: '拣货',
  PACKED: '打包'
}

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    fulfillmentNo: undefined,
    orderId: undefined,
    warehouseCode: undefined,
    fulfillmentStatus: undefined
  },
  splitForm: {
    orderId: undefined
  },
  shipForm: {
    fulfillmentId: undefined,
    fulfillmentNo: undefined,
    carrierCode: undefined,
    trackingNo: undefined
  },
  splitRules: {
    orderId: [{ required: true, message: '订单 ID 不能为空', trigger: 'blur' }]
  },
  shipRules: {
    carrierCode: [{ required: true, message: '承运商不能为空', trigger: 'blur' }]
  }
})

const { queryParams, splitForm, shipForm, splitRules, shipRules } = toRefs(data)

function getList() {
  loading.value = true
  listFulfillment(queryParams.value).then(response => {
    fulfillmentList.value = response.rows
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

function openSplit() {
  splitForm.value = { orderId: undefined }
  splitOpen.value = true
  nextTick(() => proxy.$refs.splitRef?.clearValidate())
}

function submitSplit() {
  proxy.$refs['splitRef'].validate(valid => {
    if (!valid) {
      return
    }
    splitFulfillment(splitForm.value.orderId).then(() => {
      proxy.$modal.msgSuccess('拆分成功')
      splitOpen.value = false
      getList()
    })
  })
}

function handleStatus(row, toStatus) {
  proxy.$modal.confirm('是否确认将履约单"' + row.fulfillmentNo + '"变更为' + statusActions[toStatus] + '？').then(() => {
    return changeFulfillmentStatus(row.fulfillmentId, toStatus)
  }).then(() => {
    proxy.$modal.msgSuccess('操作成功')
    getList()
  }).catch(() => {})
}

function openShip(row) {
  shipForm.value = {
    fulfillmentId: row.fulfillmentId,
    fulfillmentNo: row.fulfillmentNo,
    carrierCode: undefined,
    trackingNo: undefined
  }
  shipOpen.value = true
  nextTick(() => proxy.$refs.shipRef?.clearValidate())
}

function submitShip() {
  proxy.$refs['shipRef'].validate(valid => {
    if (!valid) {
      return
    }
    shipFulfillment(shipForm.value.fulfillmentId, shipForm.value.carrierCode, shipForm.value.trackingNo).then(() => {
      proxy.$modal.msgSuccess('发货成功')
      shipOpen.value = false
      getList()
    })
  })
}

function handleView(row) {
  getFulfillment(row.fulfillmentId).then(response => {
    detail.value = response.data || {}
    detailOpen.value = true
  })
}

getList()
</script>
