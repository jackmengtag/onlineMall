<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
      <el-form-item label="运单号" prop="shipmentNo">
        <el-input
          v-model="queryParams.shipmentNo"
          placeholder="请输入运单号"
          clearable
          style="width: 200px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="物流单号" prop="trackingNo">
        <el-input
          v-model="queryParams.trackingNo"
          placeholder="请输入物流单号"
          clearable
          style="width: 200px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="运单状态" prop="shipmentStatus">
        <el-select v-model="queryParams.shipmentStatus" placeholder="运单状态" clearable style="width: 180px">
          <el-option
            v-for="dict in commerce_shipment_status"
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
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="shipmentList">
      <el-table-column label="运单号" align="center" prop="shipmentNo" width="180" />
      <el-table-column label="物流单号" align="center" prop="trackingNo" width="180" />
      <el-table-column label="承运商" align="center" prop="carrierCode" width="120" />
      <el-table-column label="订单 ID" align="center" prop="orderId" width="120" />
      <el-table-column label="运单状态" align="center" prop="shipmentStatus" width="110">
        <template #default="scope">
          <dict-tag :options="commerce_shipment_status" :value="scope.row.shipmentStatus" />
        </template>
      </el-table-column>
      <el-table-column label="发货时间" align="center" prop="shippedAt" width="170">
        <template #default="scope">
          <span>{{ parseTime(scope.row.shippedAt) || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="签收时间" align="center" prop="deliveredAt" width="170">
        <template #default="scope">
          <span>{{ parseTime(scope.row.deliveredAt) || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="220" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="View" @click="handleDetail(scope.row)">详情</el-button>
          <el-button
            link
            type="success"
            icon="Position"
            @click="handleTrack(scope.row)"
            v-hasPermi="['commerce:shop:logistics:edit']"
          >录入轨迹</el-button>
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

    <!-- 运单详情 -->
    <el-dialog v-model="detailOpen" title="运单详情" width="880px" append-to-body>
      <div v-loading="detailLoading">
        <el-descriptions :column="3" border class="mb8">
          <el-descriptions-item label="运单号">{{ detail.shipmentNo }}</el-descriptions-item>
          <el-descriptions-item label="物流单号">{{ detail.trackingNo || '-' }}</el-descriptions-item>
          <el-descriptions-item label="承运商">{{ detail.carrierCode || '-' }}</el-descriptions-item>
          <el-descriptions-item label="订单 ID">{{ detail.orderId || '-' }}</el-descriptions-item>
          <el-descriptions-item label="履约单 ID">{{ detail.fulfillmentId || '-' }}</el-descriptions-item>
          <el-descriptions-item label="运单状态">
            <dict-tag :options="commerce_shipment_status" :value="detail.shipmentStatus" />
          </el-descriptions-item>
          <el-descriptions-item label="发货时间">{{ parseTime(detail.shippedAt) || '-' }}</el-descriptions-item>
          <el-descriptions-item label="签收时间">{{ parseTime(detail.deliveredAt) || '-' }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ parseTime(detail.createTime) || '-' }}</el-descriptions-item>
        </el-descriptions>

        <div class="section-title">物流轨迹</div>
        <el-timeline v-if="trackingEvents.length">
          <el-timeline-item
            v-for="item in trackingEvents"
            :key="item.trackingEventId"
            :timestamp="parseTime(item.occurredAt || item.receivedAt)"
            placement="top"
            :type="eventTagType(item.eventCode)"
          >
            <div class="event-title">
              <span>{{ eventLabel(item.eventCode) }}</span>
              <span v-if="item.location" class="event-location">{{ item.location }}</span>
            </div>
            <div class="event-desc">{{ item.eventDescription || '-' }}</div>
          </el-timeline-item>
        </el-timeline>
        <el-empty v-else description="暂无物流轨迹" :image-size="70" />
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="detailOpen = false">关 闭</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 录入轨迹 -->
    <el-dialog v-model="trackOpen" title="录入物流轨迹" width="520px" append-to-body>
      <el-form ref="trackRef" :model="trackForm" :rules="trackRules" label-width="90px">
        <el-form-item label="运单号">
          <span>{{ trackForm.shipmentNo }}</span>
        </el-form-item>
        <el-form-item label="事件类型" prop="eventCode">
          <el-select v-model="trackForm.eventCode" placeholder="请选择事件类型" style="width: 100%">
            <el-option v-for="item in eventOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="事件描述" prop="eventDescription">
          <el-input v-model="trackForm.eventDescription" placeholder="请输入事件描述" maxlength="200" show-word-limit />
        </el-form-item>
        <el-form-item label="位置" prop="location">
          <el-input v-model="trackForm.location" placeholder="请输入轨迹位置" maxlength="120" />
        </el-form-item>
        <el-form-item label="发生时间" prop="occurredAt">
          <el-date-picker
            v-model="trackForm.occurredAt"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择发生时间"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" :loading="trackLoading" @click="submitTrack">确 定</el-button>
          <el-button @click="trackOpen = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="ShopLogistics">
import { listShopShipment, getShopShipment, addShopTracking } from '@/api/commerce/shop/logistics'

const { proxy } = getCurrentInstance()
const { commerce_shipment_status } = useDict('commerce_shipment_status')

const loading = ref(false)
const showSearch = ref(true)
const shipmentList = ref([])
const total = ref(0)

const detailOpen = ref(false)
const detailLoading = ref(false)
const detail = ref({})
const trackingEvents = ref([])

const trackOpen = ref(false)
const trackLoading = ref(false)
const trackForm = ref({ shipmentId: undefined, shipmentNo: '', eventCode: 'IN_TRANSIT', eventDescription: '', location: '', occurredAt: '' })
const trackRules = {
  eventCode: [{ required: true, message: '事件类型不能为空', trigger: 'change' }],
  eventDescription: [{ required: true, message: '事件描述不能为空', trigger: 'blur' }]
}

const eventOptions = [
  { value: 'IN_TRANSIT', label: '运输中' },
  { value: 'DELIVERED', label: '已签收' },
  { value: 'EXCEPTION', label: '异常' },
  { value: 'RETURNED', label: '已退回' }
]

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  shipmentNo: undefined,
  trackingNo: undefined,
  shipmentStatus: undefined
})

function eventLabel(code) {
  const item = eventOptions.find(option => option.value === code)
  return item ? item.label : (code || '-')
}

function eventTagType(code) {
  if (code === 'DELIVERED') {
    return 'success'
  }
  if (code === 'EXCEPTION') {
    return 'danger'
  }
  if (code === 'RETURNED') {
    return 'warning'
  }
  return 'primary'
}

function getList() {
  loading.value = true
  listShopShipment(queryParams.value).then(response => {
    shipmentList.value = response.rows || []
    total.value = response.total || 0
  }).finally(() => {
    loading.value = false
  })
}

function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

function resetQuery() {
  proxy.resetForm('queryRef')
  queryParams.value.shipmentNo = undefined
  queryParams.value.trackingNo = undefined
  queryParams.value.shipmentStatus = undefined
  handleQuery()
}

function handleDetail(row) {
  detail.value = row
  trackingEvents.value = []
  detailOpen.value = true
  detailLoading.value = true
  getShopShipment(row.shipmentId).then(response => {
    const data = response.data || {}
    detail.value = data.shipment || data
    trackingEvents.value = data.trackingEvents || []
  }).finally(() => {
    detailLoading.value = false
  })
}

function handleTrack(row) {
  trackForm.value = {
    shipmentId: row.shipmentId,
    shipmentNo: row.shipmentNo,
    eventCode: 'IN_TRANSIT',
    eventDescription: '',
    location: '',
    occurredAt: proxy.parseTime(new Date(), '{y}-{m}-{d} {h}:{i}:{s}')
  }
  trackOpen.value = true
  nextTick(() => proxy.$refs['trackRef'] && proxy.$refs['trackRef'].clearValidate())
}

function submitTrack() {
  proxy.$refs['trackRef'].validate(valid => {
    if (!valid) {
      return
    }
    trackLoading.value = true
    const data = {
      shipmentId: trackForm.value.shipmentId,
      eventCode: trackForm.value.eventCode,
      eventDescription: trackForm.value.eventDescription,
      location: trackForm.value.location,
      occurredAt: trackForm.value.occurredAt || undefined
    }
    addShopTracking(data).then(() => {
      proxy.$modal.msgSuccess('轨迹录入成功')
      trackOpen.value = false
      getList()
      if (detailOpen.value && detail.value.shipmentId === data.shipmentId) {
        handleDetail(detail.value)
      }
    }).finally(() => {
      trackLoading.value = false
    })
  })
}

getList()
</script>

<style scoped>
.section-title {
  font-weight: 600;
  margin: 8px 0 16px;
  padding-left: 8px;
  border-left: 3px solid #409eff;
}
.event-title {
  display: flex;
  align-items: center;
  font-weight: 500;
}
.event-location {
  margin-left: 10px;
  color: #909399;
  font-size: 13px;
}
.event-desc {
  margin-top: 6px;
  color: #606266;
  font-size: 13px;
}
</style>
