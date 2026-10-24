<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
      <el-form-item label="订单号" prop="orderNo">
        <el-input v-model="queryParams.orderNo" placeholder="请输入订单号" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="店铺 ID" prop="shopId">
        <el-input-number v-model="queryParams.shopId" :min="1" :controls="false" placeholder="请输入店铺 ID" style="width: 160px" />
      </el-form-item>
      <el-form-item label="订单状态" prop="orderStatus">
        <el-select v-model="queryParams.orderStatus" placeholder="请选择订单状态" clearable style="width: 180px">
          <el-option v-for="dict in commerce_order_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="支付状态" prop="paymentStatus">
        <el-select v-model="queryParams.paymentStatus" placeholder="请选择支付状态" clearable style="width: 180px">
          <el-option v-for="dict in commerce_payment_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="渠道" prop="channelCode">
        <el-input v-model="queryParams.channelCode" placeholder="请输入渠道编码" clearable style="width: 160px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="下单时间">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          value-format="YYYY-MM-DD"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="width: 260px"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['commerce:admin:order:add']">创建订单</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderList">
      <el-table-column label="订单号" align="center" prop="orderNo" min-width="170" show-overflow-tooltip />
      <el-table-column label="店铺" align="center" min-width="120" show-overflow-tooltip>
        <template #default="scope">{{ scope.row.shopName || scope.row.shopId }}</template>
      </el-table-column>
      <el-table-column label="买家" align="center" min-width="110" show-overflow-tooltip>
        <template #default="scope">{{ scope.row.customerName || scope.row.customerId }}</template>
      </el-table-column>
      <el-table-column label="渠道" align="center" prop="channelCode" width="100" />
      <el-table-column label="国家" align="center" prop="countryCode" width="80" />
      <el-table-column label="币种" align="center" prop="currencyCode" width="80" />
      <el-table-column label="应付金额" align="center" width="130">
        <template #default="scope">{{ formatMoney(scope.row.totalAmount, scope.row.currencyCode) }}</template>
      </el-table-column>
      <el-table-column label="订单状态" align="center" width="110">
        <template #default="scope">
          <dict-tag :options="commerce_order_status" :value="scope.row.orderStatus" />
        </template>
      </el-table-column>
      <el-table-column label="支付状态" align="center" width="110">
        <template #default="scope">
          <dict-tag :options="commerce_payment_status" :value="scope.row.paymentStatus" />
        </template>
      </el-table-column>
      <el-table-column label="履约状态" align="center" width="110">
        <template #default="scope">
          <dict-tag :options="commerce_fulfillment_status" :value="scope.row.fulfillmentStatus" />
        </template>
      </el-table-column>
      <el-table-column label="下单时间" align="center" prop="placedAt" width="160">
        <template #default="scope">{{ parseTime(scope.row.placedAt) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="210" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="View" @click="handleView(scope.row)" v-hasPermi="['commerce:admin:order:query']">详情</el-button>
          <el-button
            v-if="canOperate(scope.row)"
            link
            type="warning"
            icon="CircleClose"
            @click="handleCancel(scope.row)"
            v-hasPermi="['commerce:admin:order:cancel', 'commerce:admin:order:edit']"
          >取消</el-button>
          <el-button
            v-if="canOperate(scope.row)"
            link
            type="success"
            icon="Finished"
            @click="handleComplete(scope.row)"
            v-hasPermi="['commerce:admin:order:edit']"
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

    <!-- 创建订单 -->
    <el-dialog title="创建订单" v-model="open" width="760px" append-to-body>
      <el-form ref="orderRef" :model="form" :rules="rules" label-width="110px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="客户 ID" prop="customerId">
              <el-input-number v-model="form.customerId" :min="1" :controls="false" placeholder="请输入客户 ID" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="地址 ID" prop="addressId">
              <el-input-number v-model="form.addressId" :min="1" :controls="false" placeholder="请输入地址 ID" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="店铺 ID" prop="shopId">
              <el-input-number v-model="form.shopId" :min="1" :controls="false" placeholder="请输入店铺 ID" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="国家" prop="countryCode">
              <el-input v-model="form.countryCode" placeholder="如 US / CN" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="渠道" prop="channelCode">
              <el-input v-model="form.channelCode" placeholder="请输入渠道编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="币种" prop="currencyCode">
              <el-input v-model="form.currencyCode" placeholder="如 USD / CNY" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="优惠券" prop="couponCode">
              <el-input v-model="form.couponCode" placeholder="请输入优惠券编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="运费(元)" prop="shippingAmount">
              <el-input-number v-model="form.shippingAmount" :min="0" :precision="2" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="商品明细" required>
              <div class="order-items">
                <div v-for="(item, index) in form.items" :key="index" class="order-item-row">
                  <el-select
                    v-model="item.skuId"
                    filterable
                    remote
                    clearable
                    :remote-method="searchSkus"
                    :loading="skuLoading"
                    placeholder="输入 SKU 编码搜索"
                    class="sku-select"
                    @focus="searchSkus('')"
                  >
                    <el-option
                      v-for="sku in skuOptions"
                      :key="sku.skuId"
                      :label="sku.skuCode + ' ' + (sku.spuName || '')"
                      :value="sku.skuId"
                    />
                  </el-select>
                  <el-input-number v-model="item.quantity" :min="1" :precision="0" controls-position="right" class="qty-input" />
                  <el-button link type="danger" icon="Delete" :disabled="form.items.length === 1" @click="removeItem(index)" />
                </div>
                <el-button type="primary" plain icon="Plus" @click="addItem">添加明细</el-button>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 订单详情 -->
    <el-dialog title="订单详情" v-model="detailOpen" width="900px" append-to-body>
      <el-descriptions title="订单信息" :column="3" border>
        <el-descriptions-item label="订单号">{{ orderInfo.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="店铺">{{ orderInfo.shopName || orderInfo.shopId }}</el-descriptions-item>
        <el-descriptions-item label="买家">{{ orderInfo.customerName || orderInfo.customerId }}</el-descriptions-item>
        <el-descriptions-item label="渠道">{{ orderInfo.channelCode }}</el-descriptions-item>
        <el-descriptions-item label="国家">{{ orderInfo.countryCode }}</el-descriptions-item>
        <el-descriptions-item label="币种">{{ orderInfo.currencyCode }}</el-descriptions-item>
        <el-descriptions-item label="商品金额">{{ formatMoney(orderInfo.subtotalAmount, orderInfo.currencyCode) }}</el-descriptions-item>
        <el-descriptions-item label="优惠金额">{{ formatMoney(orderInfo.discountAmount, orderInfo.currencyCode) }}</el-descriptions-item>
        <el-descriptions-item label="运费">{{ formatMoney(orderInfo.shippingAmount, orderInfo.currencyCode) }}</el-descriptions-item>
        <el-descriptions-item label="税费">{{ formatMoney(orderInfo.taxAmount, orderInfo.currencyCode) }}</el-descriptions-item>
        <el-descriptions-item label="应付金额">{{ formatMoney(orderInfo.totalAmount, orderInfo.currencyCode) }}</el-descriptions-item>
        <el-descriptions-item label="优惠券">{{ orderInfo.couponCode || '-' }}</el-descriptions-item>
        <el-descriptions-item label="订单状态">
          <dict-tag :options="commerce_order_status" :value="orderInfo.orderStatus" />
        </el-descriptions-item>
        <el-descriptions-item label="支付状态">
          <dict-tag :options="commerce_payment_status" :value="orderInfo.paymentStatus" />
        </el-descriptions-item>
        <el-descriptions-item label="履约状态">
          <dict-tag :options="commerce_fulfillment_status" :value="orderInfo.fulfillmentStatus" />
        </el-descriptions-item>
        <el-descriptions-item label="下单时间">{{ parseTime(orderInfo.placedAt) }}</el-descriptions-item>
        <el-descriptions-item label="支付时间">{{ parseTime(orderInfo.paidAt) }}</el-descriptions-item>
        <el-descriptions-item label="完成时间">{{ parseTime(orderInfo.completedAt) }}</el-descriptions-item>
        <el-descriptions-item label="取消原因" :span="3">{{ orderInfo.cancelReason || '-' }}</el-descriptions-item>
      </el-descriptions>

      <el-divider content-position="left">商品明细</el-divider>
      <el-table :data="detailItems" size="small" border>
        <el-table-column label="SKU 编码" align="center" prop="skuCode" min-width="140" />
        <el-table-column label="数量" align="center" prop="quantity" width="80" />
        <el-table-column label="单价" align="center" width="120">
          <template #default="scope">{{ formatMoney(scope.row.unitPriceAmount, scope.row.currencyCode) }}</template>
        </el-table-column>
        <el-table-column label="优惠" align="center" width="110">
          <template #default="scope">{{ formatMoney(scope.row.discountAmount, scope.row.currencyCode) }}</template>
        </el-table-column>
        <el-table-column label="税费" align="center" width="110">
          <template #default="scope">{{ formatMoney(scope.row.taxAmount, scope.row.currencyCode) }}</template>
        </el-table-column>
        <el-table-column label="小计" align="center" width="120">
          <template #default="scope">{{ formatMoney(scope.row.lineTotalAmount, scope.row.currencyCode) }}</template>
        </el-table-column>
      </el-table>

      <el-divider content-position="left">订单快照</el-divider>
      <el-descriptions :column="3" border>
        <el-descriptions-item label="买家">{{ snapshot.buyerName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="买家邮箱">{{ snapshot.buyerEmail || '-' }}</el-descriptions-item>
        <el-descriptions-item label="收件人">{{ snapshot.recipientName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ snapshot.recipientPhone || '-' }}</el-descriptions-item>
        <el-descriptions-item label="国家/省州">{{ (snapshot.countryCode || '-') + ' / ' + (snapshot.stateCode || '-') }}</el-descriptions-item>
        <el-descriptions-item label="城市/区">{{ (snapshot.city || '-') + ' / ' + (snapshot.district || '-') }}</el-descriptions-item>
        <el-descriptions-item label="地址" :span="2">{{ [snapshot.addressLine1, snapshot.addressLine2, snapshot.postalCode].filter(Boolean).join(' ') || '-' }}</el-descriptions-item>
        <el-descriptions-item label="汇率">{{ snapshot.exchangeRate || '-' }}</el-descriptions-item>
      </el-descriptions>
      <el-table v-if="itemSnapshots.length" :data="itemSnapshots" size="small" border class="mt10">
        <el-table-column label="商品名称" align="center" prop="productName" min-width="150" show-overflow-tooltip />
        <el-table-column label="SKU 编码" align="center" prop="skuCode" min-width="140" />
        <el-table-column label="申报品名" align="center" prop="declaredName" min-width="120" />
        <el-table-column label="HS 编码" align="center" prop="hsCode" width="110" />
        <el-table-column label="申报价值" align="center" width="120">
          <template #default="scope">{{ formatMoney(scope.row.declaredValueAmount, scope.row.declaredCurrencyCode) }}</template>
        </el-table-column>
        <el-table-column label="销售单价" align="center" width="120">
          <template #default="scope">{{ formatMoney(scope.row.unitPriceAmount, scope.row.currencyCode) }}</template>
        </el-table-column>
      </el-table>

      <el-divider content-position="left">状态时间线</el-divider>
      <el-timeline v-if="histories.length">
        <el-timeline-item
          v-for="history in histories"
          :key="history.historyId"
          :timestamp="parseTime(history.occurredAt)"
          placement="top"
        >
          <div>{{ history.statusType || '状态' }}：{{ history.fromStatus || '-' }} → {{ history.toStatus }}</div>
          <div v-if="history.reason" class="timeline-reason">原因：{{ history.reason }}</div>
        </el-timeline-item>
      </el-timeline>
      <el-empty v-else description="暂无状态记录" :image-size="60" />

      <template #footer>
        <el-button @click="detailOpen = false">关 闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="CommerceAdminOrder">
import { ElMessageBox } from 'element-plus'
import { listOrder, getOrder, addOrder, cancelOrder, completeOrder } from '@/api/commerce/admin/order'
import { listSkuOptions } from '@/api/commerce/admin/product'
import { formatMoney, toMinor } from '@/utils/commerce'

const { proxy } = getCurrentInstance()
const { commerce_order_status, commerce_payment_status, commerce_fulfillment_status } = useDict(
  'commerce_order_status',
  'commerce_payment_status',
  'commerce_fulfillment_status'
)

const orderList = ref([])
const open = ref(false)
const detailOpen = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const total = ref(0)
const dateRange = ref([])
const detail = ref({})
const skuOptions = ref([])
const skuLoading = ref(false)

const orderInfo = computed(() => detail.value.order || {})
const snapshot = computed(() => detail.value.snapshot || {})
const detailItems = computed(() => detail.value.items || [])
const itemSnapshots = computed(() => detail.value.itemSnapshots || [])
const histories = computed(() => detail.value.histories || [])

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    orderNo: undefined,
    shopId: undefined,
    orderStatus: undefined,
    paymentStatus: undefined,
    channelCode: undefined
  },
  rules: {
    customerId: [{ required: true, message: '客户 ID 不能为空', trigger: 'blur' }],
    shopId: [{ required: true, message: '店铺 ID 不能为空', trigger: 'blur' }],
    countryCode: [{ required: true, message: '国家不能为空', trigger: 'blur' }],
    channelCode: [{ required: true, message: '渠道不能为空', trigger: 'blur' }],
    currencyCode: [{ required: true, message: '币种不能为空', trigger: 'blur' }]
  }
})

const { queryParams, form, rules } = toRefs(data)

function getList() {
  loading.value = true
  const params = { ...queryParams.value }
  if (dateRange.value && dateRange.value.length === 2) {
    params.beginTime = dateRange.value[0] + ' 00:00:00'
    params.endTime = dateRange.value[1] + ' 23:59:59'
  }
  listOrder(params).then(response => {
    orderList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

function cancel() {
  open.value = false
  reset()
}

function reset() {
  form.value = {
    customerId: undefined,
    addressId: undefined,
    shopId: undefined,
    countryCode: undefined,
    channelCode: undefined,
    currencyCode: 'USD',
    couponCode: undefined,
    shippingAmount: undefined,
    remark: undefined,
    items: [{ skuId: undefined, quantity: 1 }]
  }
  proxy.resetForm('orderRef')
}

function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

function resetQuery() {
  proxy.resetForm('queryRef')
  dateRange.value = []
  handleQuery()
}

function canOperate(row) {
  return row.orderStatus !== 'CANCELLED' && row.orderStatus !== 'COMPLETED'
}

function handleAdd() {
  reset()
  skuOptions.value = []
  searchSkus('')
  open.value = true
}

function addItem() {
  form.value.items.push({ skuId: undefined, quantity: 1 })
}

function removeItem(index) {
  form.value.items.splice(index, 1)
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

function submitForm() {
  proxy.$refs['orderRef'].validate(valid => {
    if (!valid) {
      return
    }
    const items = form.value.items
      .filter(item => item.skuId)
      .map(item => ({ skuId: item.skuId, quantity: Number(item.quantity || 1) }))
    if (!items.length) {
      proxy.$modal.msgWarning('请至少添加一条商品明细')
      return
    }
    const payload = {
      customerId: form.value.customerId,
      addressId: form.value.addressId,
      shopId: form.value.shopId,
      countryCode: form.value.countryCode,
      channelCode: form.value.channelCode,
      currencyCode: form.value.currencyCode,
      couponCode: form.value.couponCode,
      remark: form.value.remark,
      items: items
    }
    if (form.value.shippingAmount !== undefined && form.value.shippingAmount !== null && form.value.shippingAmount !== '') {
      payload.shippingAmount = toMinor(form.value.shippingAmount)
    }
    addOrder(payload).then(() => {
      proxy.$modal.msgSuccess('创建成功')
      open.value = false
      getList()
    })
  })
}

function handleView(row) {
  getOrder(row.orderId).then(response => {
    detail.value = response.data || {}
    detailOpen.value = true
  })
}

function handleCancel(row) {
  ElMessageBox.prompt('请输入取消原因', '取消订单', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputType: 'textarea',
    inputValidator: value => (value ? true : '取消原因不能为空')
  }).then(({ value }) => {
    return cancelOrder(row.orderId, value)
  }).then(() => {
    proxy.$modal.msgSuccess('订单已取消')
    getList()
  }).catch(() => {})
}

function handleComplete(row) {
  proxy.$modal.confirm('是否确认完成订单"' + row.orderNo + '"？').then(() => {
    return completeOrder(row.orderId)
  }).then(() => {
    proxy.$modal.msgSuccess('订单已完成')
    getList()
  }).catch(() => {})
}

getList()
</script>

<style scoped>
.order-items {
  width: 100%;
}

.order-item-row {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 8px;
}

.sku-select {
  flex: 1;
}

.qty-input {
  width: 120px;
}

.mt10 {
  margin-top: 10px;
}

.timeline-reason {
  color: #909399;
  font-size: 12px;
}
</style>
