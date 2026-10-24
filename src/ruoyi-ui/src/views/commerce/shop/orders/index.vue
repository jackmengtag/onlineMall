<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
      <el-form-item label="订单号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入订单号"
          clearable
          style="width: 200px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="订单状态" prop="orderStatus">
        <el-select v-model="queryParams.orderStatus" placeholder="订单状态" clearable style="width: 180px">
          <el-option
            v-for="dict in commerce_order_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="支付状态" prop="paymentStatus">
        <el-select v-model="queryParams.paymentStatus" placeholder="支付状态" clearable style="width: 180px">
          <el-option
            v-for="dict in commerce_payment_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="下单时间">
        <el-date-picker
          v-model="dateRange"
          type="datetimerange"
          value-format="YYYY-MM-DD HH:mm:ss"
          range-separator="-"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          style="width: 360px"
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

    <el-table v-loading="loading" :data="orderList">
      <el-table-column label="订单号" align="center" prop="orderNo" width="180" />
      <el-table-column label="客户" align="center" prop="customerName" :show-overflow-tooltip="true" />
      <el-table-column label="订单金额" align="right" width="140">
        <template #default="scope">
          <span>{{ formatMoney(scope.row.totalAmount || 0, scope.row.currencyCode || 'CNY') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="订单状态" align="center" prop="orderStatus" width="110">
        <template #default="scope">
          <dict-tag :options="commerce_order_status" :value="scope.row.orderStatus" />
        </template>
      </el-table-column>
      <el-table-column label="支付状态" align="center" prop="paymentStatus" width="110">
        <template #default="scope">
          <dict-tag :options="commerce_payment_status" :value="scope.row.paymentStatus" />
        </template>
      </el-table-column>
      <el-table-column label="履约状态" align="center" prop="fulfillmentStatus" width="110">
        <template #default="scope">
          <dict-tag :options="commerce_fulfillment_status" :value="scope.row.fulfillmentStatus" />
        </template>
      </el-table-column>
      <el-table-column label="下单时间" align="center" prop="placedAt" width="170">
        <template #default="scope">
          <span>{{ parseTime(scope.row.placedAt || scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="170" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button
            link
            type="primary"
            icon="View"
            @click="handleDetail(scope.row)"
            v-hasPermi="['commerce:shop:order:query']"
          >详情</el-button>
          <el-button
            v-if="canCancel(scope.row)"
            link
            type="danger"
            icon="CircleClose"
            @click="handleCancel(scope.row)"
            v-hasPermi="['commerce:shop:order:edit']"
          >取消</el-button>
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

    <!-- 订单详情 -->
    <el-dialog v-model="detailOpen" title="订单详情" width="960px" append-to-body>
      <div v-loading="detailLoading">
        <el-descriptions :column="3" border class="mb8">
          <el-descriptions-item label="订单号">{{ detail.order.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="客户">{{ detail.order.customerName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="下单时间">{{ parseTime(detail.order.placedAt || detail.order.createTime) }}</el-descriptions-item>
          <el-descriptions-item label="订单状态">
            <dict-tag :options="commerce_order_status" :value="detail.order.orderStatus" />
          </el-descriptions-item>
          <el-descriptions-item label="支付状态">
            <dict-tag :options="commerce_payment_status" :value="detail.order.paymentStatus" />
          </el-descriptions-item>
          <el-descriptions-item label="履约状态">
            <dict-tag :options="commerce_fulfillment_status" :value="detail.order.fulfillmentStatus" />
          </el-descriptions-item>
          <el-descriptions-item label="商品小计">{{ formatMoney(detail.order.subtotalAmount || 0, detail.order.currencyCode || 'CNY') }}</el-descriptions-item>
          <el-descriptions-item label="优惠金额">{{ formatMoney(detail.order.discountAmount || 0, detail.order.currencyCode || 'CNY') }}</el-descriptions-item>
          <el-descriptions-item label="运费">{{ formatMoney(detail.order.shippingAmount || 0, detail.order.currencyCode || 'CNY') }}</el-descriptions-item>
          <el-descriptions-item label="税费">{{ formatMoney(detail.order.taxAmount || 0, detail.order.currencyCode || 'CNY') }}</el-descriptions-item>
          <el-descriptions-item label="订单总额">{{ formatMoney(detail.order.totalAmount || 0, detail.order.currencyCode || 'CNY') }}</el-descriptions-item>
          <el-descriptions-item label="支付时间">{{ parseTime(detail.order.paidAt) || '-' }}</el-descriptions-item>
          <el-descriptions-item label="取消原因" :span="3">{{ detail.order.cancelReason || '-' }}</el-descriptions-item>
        </el-descriptions>

        <el-tabs v-model="detailTab">
          <el-tab-pane label="订单明细" name="items">
            <el-table :data="detail.items || []" border>
              <el-table-column label="SKU 编码" align="center" prop="skuCode" width="150" />
              <el-table-column label="数量" align="center" prop="quantity" width="80" />
              <el-table-column label="单价" align="right" width="130">
                <template #default="scope">
                  <span>{{ formatMoney(scope.row.unitPriceAmount || 0, scope.row.currencyCode || 'CNY') }}</span>
                </template>
              </el-table-column>
              <el-table-column label="优惠" align="right" width="120">
                <template #default="scope">
                  <span>{{ formatMoney(scope.row.discountAmount || 0, scope.row.currencyCode || 'CNY') }}</span>
                </template>
              </el-table-column>
              <el-table-column label="税费" align="right" width="120">
                <template #default="scope">
                  <span>{{ formatMoney(scope.row.taxAmount || 0, scope.row.currencyCode || 'CNY') }}</span>
                </template>
              </el-table-column>
              <el-table-column label="小计" align="right" width="130">
                <template #default="scope">
                  <span>{{ formatMoney(scope.row.lineTotalAmount || 0, scope.row.currencyCode || 'CNY') }}</span>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>

          <el-tab-pane label="订单快照" name="snapshot">
            <template v-if="detail.snapshot">
              <el-descriptions :column="3" border class="mb8">
                <el-descriptions-item label="买家">{{ detail.snapshot.buyerName || '-' }}</el-descriptions-item>
                <el-descriptions-item label="买家邮箱">{{ detail.snapshot.buyerEmail || '-' }}</el-descriptions-item>
                <el-descriptions-item label="收件人">{{ detail.snapshot.recipientName || '-' }}</el-descriptions-item>
                <el-descriptions-item label="联系电话">{{ detail.snapshot.recipientPhone || '-' }}</el-descriptions-item>
                <el-descriptions-item label="国家/地区">{{ detail.snapshot.countryCode || '-' }}</el-descriptions-item>
                <el-descriptions-item label="省/州">{{ detail.snapshot.stateCode || '-' }}</el-descriptions-item>
                <el-descriptions-item label="城市">{{ detail.snapshot.city || '-' }}</el-descriptions-item>
                <el-descriptions-item label="区/县">{{ detail.snapshot.district || '-' }}</el-descriptions-item>
                <el-descriptions-item label="邮编">{{ detail.snapshot.postalCode || '-' }}</el-descriptions-item>
                <el-descriptions-item label="地址" :span="3">
                  {{ [detail.snapshot.addressLine1, detail.snapshot.addressLine2].filter(Boolean).join(' ') || '-' }}
                </el-descriptions-item>
                <el-descriptions-item label="汇率版本">{{ detail.snapshot.exchangeRateVersion || '-' }}</el-descriptions-item>
                <el-descriptions-item label="汇率">{{ detail.snapshot.exchangeRate || '-' }}</el-descriptions-item>
                <el-descriptions-item label="税则版本">{{ detail.snapshot.taxRuleVersion || '-' }}</el-descriptions-item>
              </el-descriptions>
            </template>
            <el-table :data="detail.itemSnapshots || []" border>
              <el-table-column label="商品名称" align="left" prop="productName" :show-overflow-tooltip="true" />
              <el-table-column label="SKU 编码" align="center" prop="skuCode" width="150" />
              <el-table-column label="销售属性" align="center" prop="attributesJson" :show-overflow-tooltip="true" />
              <el-table-column label="单价" align="right" width="130">
                <template #default="scope">
                  <span>{{ formatMoney(scope.row.unitPriceAmount || 0, scope.row.currencyCode || 'CNY') }}</span>
                </template>
              </el-table-column>
              <el-table-column label="申报价值" align="right" width="130">
                <template #default="scope">
                  <span>{{ formatMoney(scope.row.declaredValueAmount || 0, scope.row.declaredCurrencyCode || 'CNY') }}</span>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>

          <el-tab-pane label="状态历史" name="history">
            <el-timeline v-if="(detail.histories || []).length">
              <el-timeline-item
                v-for="item in detail.histories"
                :key="item.historyId"
                :timestamp="parseTime(item.occurredAt || item.createTime)"
                placement="top"
              >
                <div class="history-title">
                  <span>{{ statusLabel(item.fromStatus) || '-' }}</span>
                  <el-icon><Right /></el-icon>
                  <span>{{ statusLabel(item.toStatus) || '-' }}</span>
                  <el-tag size="small" effect="plain" class="history-tag">{{ item.statusType }}</el-tag>
                </div>
                <div class="history-desc">{{ item.reason || '无备注' }}</div>
                <div class="history-desc">操作方：{{ item.operatorType || '-' }} {{ item.operatorId || '' }}</div>
              </el-timeline-item>
            </el-timeline>
            <el-empty v-else description="暂无状态历史" :image-size="70" />
          </el-tab-pane>
        </el-tabs>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="detailOpen = false">关 闭</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 取消订单 -->
    <el-dialog v-model="cancelOpen" title="取消订单" width="500px" append-to-body>
      <el-form ref="cancelRef" :model="cancelForm" :rules="cancelRules" label-width="80px">
        <el-form-item label="订单号">
          <span>{{ cancelForm.orderNo }}</span>
        </el-form-item>
        <el-form-item label="取消原因" prop="reason">
          <el-input v-model="cancelForm.reason" type="textarea" :rows="4" placeholder="请输入取消原因" maxlength="200" show-word-limit />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="danger" :loading="cancelLoading" @click="submitCancel">确 定</el-button>
          <el-button @click="cancelOpen = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="ShopOrder">
import { listShopOrder, getShopOrder, cancelShopOrder } from '@/api/commerce/shop/order'
import { formatMoney } from '@/utils/commerce'

const { proxy } = getCurrentInstance()
const { commerce_order_status, commerce_payment_status, commerce_fulfillment_status } =
  useDict('commerce_order_status', 'commerce_payment_status', 'commerce_fulfillment_status')

const loading = ref(false)
const showSearch = ref(true)
const orderList = ref([])
const total = ref(0)
const dateRange = ref([])

const detailOpen = ref(false)
const detailLoading = ref(false)
const detailTab = ref('items')
const detail = ref({ order: {}, items: [], itemSnapshots: [], snapshot: null, histories: [] })

const cancelOpen = ref(false)
const cancelLoading = ref(false)
const cancelForm = ref({ orderId: undefined, orderNo: '', reason: '' })
const cancelRules = {
  reason: [{ required: true, message: '取消原因不能为空', trigger: 'blur' }]
}

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  orderNo: undefined,
  orderStatus: undefined,
  paymentStatus: undefined,
  beginTime: undefined,
  endTime: undefined
})

const cancelableStatus = ['CREATED', 'PENDING_PAYMENT', 'PAID', 'CONFIRMED', 'FULFILLING']

function canCancel(row) {
  return cancelableStatus.includes(row.orderStatus)
}

function statusLabel(value) {
  if (!value) {
    return ''
  }
  return proxy.selectDictLabel(commerce_order_status.value, value)
}

function getList() {
  loading.value = true
  queryParams.value.beginTime = dateRange.value && dateRange.value.length ? dateRange.value[0] : undefined
  queryParams.value.endTime = dateRange.value && dateRange.value.length ? dateRange.value[1] : undefined
  listShopOrder(queryParams.value).then(response => {
    orderList.value = response.rows || []
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
  dateRange.value = []
  queryParams.value.orderNo = undefined
  queryParams.value.orderStatus = undefined
  queryParams.value.paymentStatus = undefined
  handleQuery()
}

function handleDetail(row) {
  detail.value = { order: row, items: [], itemSnapshots: [], snapshot: null, histories: [] }
  detailTab.value = 'items'
  detailOpen.value = true
  detailLoading.value = true
  getShopOrder(row.orderId).then(response => {
    detail.value = {
      order: {},
      items: [],
      itemSnapshots: [],
      snapshot: null,
      histories: [],
      ...(response.data || {})
    }
  }).finally(() => {
    detailLoading.value = false
  })
}

function handleCancel(row) {
  cancelForm.value = { orderId: row.orderId, orderNo: row.orderNo, reason: '' }
  cancelOpen.value = true
  nextTick(() => proxy.$refs['cancelRef'] && proxy.$refs['cancelRef'].clearValidate())
}

function submitCancel() {
  proxy.$refs['cancelRef'].validate(valid => {
    if (!valid) {
      return
    }
    cancelLoading.value = true
    cancelShopOrder(cancelForm.value.orderId, cancelForm.value.reason).then(() => {
      proxy.$modal.msgSuccess('取消成功')
      cancelOpen.value = false
      getList()
    }).finally(() => {
      cancelLoading.value = false
    })
  })
}

getList()
</script>

<style scoped>
.history-title {
  display: flex;
  align-items: center;
  font-weight: 500;
}
.history-title .el-icon {
  margin: 0 6px;
}
.history-tag {
  margin-left: 10px;
}
.history-desc {
  margin-top: 6px;
  color: #909399;
  font-size: 13px;
}
</style>
